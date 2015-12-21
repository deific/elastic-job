/**
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.dangdang.ddframe.job.api;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import com.dangdang.ddframe.job.exception.JobException;
import com.dangdang.ddframe.job.internal.config.ConfigurationService;
import com.dangdang.ddframe.job.internal.election.LeaderElectionService;
import com.dangdang.ddframe.job.internal.execution.ExecutionContextService;
import com.dangdang.ddframe.job.internal.execution.ExecutionService;
import com.dangdang.ddframe.job.internal.failover.FailoverService;
import com.dangdang.ddframe.job.internal.job.AbstractElasticJob;
import com.dangdang.ddframe.job.internal.listener.ListenerManager;
import com.dangdang.ddframe.job.internal.offset.OffsetService;
import com.dangdang.ddframe.job.internal.schedule.JobRegistry;
import com.dangdang.ddframe.job.internal.schedule.JobTriggerListener;
import com.dangdang.ddframe.job.internal.server.ServerService;
import com.dangdang.ddframe.job.internal.sharding.ShardingService;
import com.dangdang.ddframe.job.internal.statistics.StatisticsService;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import com.google.common.base.Joiner;

/**
 * 作业调度器.
 * 
 * @author zhangliang, caohao
 */
@Slf4j
public class JobScheduler {
    
    private static final String SCHEDULER_INSTANCE_NAME_SUFFIX = "Scheduler";
    
    private static final String CRON_TRIGGER_INDENTITY_SUFFIX = "Trigger";
    
    private final JobConfiguration jobConfiguration;
    
    private final ListenerManager listenerManager;
    
    private final ConfigurationService configService;
    
    private final LeaderElectionService leaderElectionService;
    
    private final ServerService serverService;
    
    private final ShardingService shardingService;
    
    private final ExecutionContextService executionContextService;
    
    private final ExecutionService executionService;
    
    private final FailoverService failoverService;
    
    private final StatisticsService statisticsService;
    
    private final OffsetService offsetService;
    
    private Scheduler scheduler;
    
    private JobDetail jobDetail;
    
    /**
     * 创建作业调度器
     * 初始化作业调度器环境
     * @param coordinatorRegistryCenter
     * @param jobConfiguration
     */
    public JobScheduler(final CoordinatorRegistryCenter coordinatorRegistryCenter, final JobConfiguration jobConfiguration) {
        this.jobConfiguration = jobConfiguration;
        // 初始化监听管理
        listenerManager = new ListenerManager(coordinatorRegistryCenter, jobConfiguration);
        // 初始化配置服务，zookeeper节点数据服务
        configService = new ConfigurationService(coordinatorRegistryCenter, jobConfiguration);
        // 初始化主节点选举服务
        leaderElectionService = new LeaderElectionService(coordinatorRegistryCenter, jobConfiguration);
        // 初始化服务器服务
        serverService = new ServerService(coordinatorRegistryCenter, jobConfiguration);
        // 初始化分片服务
        shardingService = new ShardingService(coordinatorRegistryCenter, jobConfiguration);
        // 初始化作业运行时上下文服务
        executionContextService = new ExecutionContextService(coordinatorRegistryCenter, jobConfiguration);
        // 执行作业的服务
        executionService = new ExecutionService(coordinatorRegistryCenter, jobConfiguration);
        // 失效转移服务
        failoverService = new FailoverService(coordinatorRegistryCenter, jobConfiguration);
        // 作业统计服务
        statisticsService = new StatisticsService(coordinatorRegistryCenter, jobConfiguration);
        // 数据处理位置服务
        offsetService = new OffsetService(coordinatorRegistryCenter, jobConfiguration);
    }
    
    /**
     * 初始化作业，作业调度器启动入口.
     */
    public void init() {
        log.debug("Elastic job: job controller init, job name is: {}.", jobConfiguration.getJobName());
        // 初始化环境
        registerElasticEnv();
        // 创建作业数据
        jobDetail = createJobDetail();
        try {
        	// 初始化调度器
            scheduler = initializeScheduler(jobDetail.getKey().toString());
            // 启动任务
            scheduleJob(createTrigger(configService.getCron()));
        } catch (final SchedulerException ex) {
            throw new JobException(ex);
        }
        
        // 将该作业注册到注册表中
        JobRegistry.getInstance().addJob(jobConfiguration.getJobName(), this);
    }
    
    /**
     * 注册环境参数
     */
    private void registerElasticEnv() {
    	// 启动监听
        listenerManager.startAllListeners();
        // 选举主节点
        leaderElectionService.leaderElection();
        // 持久化分布式作业配置信息
        configService.persistJobConfiguration();
        // 持久化服务器上线配置
        serverService.persistServerOnline();
        // 清除作业停止标记
        serverService.clearJobStopedStatus();
        // 启动统计作业
        statisticsService.startProcessCountJob();
        // 设置重新分片标记
        shardingService.setReshardingFlag();
    }
    
    /**
     * 创建作业状态数据
     * 传给scheduler一个JobDetail实例，创建JobDetail时，将要执行的job的类名传给了JobDetail，
     * 所以scheduler就知道了要执行何种类型的job；每次当scheduler执行job时，在调用其execute(…)方法之前会创建该类的一个新的实例；
     * 执行完毕，对该实例的引用就被丢弃了，实例会被垃圾回收；
     * 这种执行策略带来的一个后果是，job必须有一个无参的构造函数（当使用默认的JobFactory时）；
     * 另一个后果是，在job类中，不应该定义有状态的数据属性，因为在job的多次执行中，这些属性的值不会保留.
     * 通过JobDetail的JobDataMap给job实例增加属性或配置
     * @return
     */
    private JobDetail createJobDetail() {
        JobDetail result = JobBuilder.newJob(jobConfiguration.getJobClass())
        		.withIdentity(jobConfiguration.getJobName()).withDescription(jobConfiguration.getJobName())
        		.build();
        result.getJobDataMap().put("configService", configService);
        result.getJobDataMap().put("shardingService", shardingService);
        result.getJobDataMap().put("executionContextService", executionContextService);
        result.getJobDataMap().put("executionService", executionService);
        result.getJobDataMap().put("failoverService", failoverService);
        result.getJobDataMap().put("offsetService", offsetService);
        return result;
    }
    
    /**
     * 创建触发器
     * @param cronExpression
     * @return
     */
    private CronTrigger createTrigger(final String cronExpression) {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        if (configService.isMisfire()) {
            cronScheduleBuilder = cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
        } else {
            cronScheduleBuilder = cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
        }
        return TriggerBuilder.newTrigger()
                .withIdentity(Joiner.on("_").join(jobConfiguration.getJobName(), CRON_TRIGGER_INDENTITY_SUFFIX))
                .withSchedule(cronScheduleBuilder).build();
    }
    
    /**
     * 初始化调度器
     * 每个job创建自己的调度器
     * @param jobName
     * @return
     * @throws SchedulerException
     */
    private Scheduler initializeScheduler(final String jobName) throws SchedulerException {
        StdSchedulerFactory factory = new StdSchedulerFactory();
        factory.initialize(getBaseQuartzProperties(jobName));
        Scheduler result = factory.getScheduler();
        // 添加定时任务触发时处理服务
        result.getListenerManager().addTriggerListener(new JobTriggerListener(executionService, shardingService));
        return result;
    }
    
    /**
     * 设置Scheduler配置信息
     * 不同的instanceName名字会生成不同的Scheduler
     * @param jobName
     * @return
     */
    private Properties getBaseQuartzProperties(final String jobName) {
        Properties result = new Properties();
        result.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        result.put("org.quartz.threadPool.threadCount", "1");
        result.put("org.quartz.scheduler.instanceName", Joiner.on("_").join(jobName, SCHEDULER_INSTANCE_NAME_SUFFIX));
        if (!configService.isMisfire()) {
            result.put("org.quartz.jobStore.misfireThreshold", "1");
        }
        prepareEnvironments(result);
        return result;
    }
    
    protected void prepareEnvironments(final Properties props) {
    }
    
    /**
     * 调度任务
     * @param trigger
     * @throws SchedulerException
     */
    private void scheduleJob(final CronTrigger trigger) throws SchedulerException {
        if (!scheduler.checkExists(jobDetail.getKey())) {
            scheduler.scheduleJob(jobDetail, trigger);
        }
        scheduler.start();
    }
    
    /**
     * 获取下次作业触发时间.
     * 
     * @return 下次作业触发时间
     */
    public Date getNextFireTime() {
        Date result = null;
        List<? extends Trigger> triggers;
        try {
            triggers = scheduler.getTriggersOfJob(jobDetail.getKey());
        } catch (final SchedulerException ex) {
            return result;
        }
        for (Trigger each : triggers) {
            Date nextFireTime = each.getNextFireTime();
            if (null == nextFireTime) {
                continue;
            }
            if (null == result) {
                result = nextFireTime;
            } else if (nextFireTime.getTime() < result.getTime()) {
                result = nextFireTime;
            }
        }
        return result;
    }
    
    /**
     * 停止作业.
     */
    public void stopJob() {
        try {
            for (JobExecutionContext each : scheduler.getCurrentlyExecutingJobs()) {
                if (each.getJobInstance() instanceof AbstractElasticJob) {
                    ((AbstractElasticJob) each.getJobInstance()).stop();
                }
            }
            scheduler.pauseAll();
        } catch (final SchedulerException ex) {
            throw new JobException(ex);
        }
    }
    
    /**
     * 恢复手工停止的作业.
     */
    public void resumeManualStopedJob() {
        try {
            scheduler.resumeAll();
        } catch (final SchedulerException ex) {
            throw new JobException(ex);
        }
        serverService.clearJobStopedStatus();
    }
    
    /**
     * 恢复因服务器崩溃而停止的作业.
     * 
     * <p>
     * 不会恢复手工设置停止运行的作业.
     * </p>
     */
    public void resumeCrashedJob() {
        serverService.persistServerOnline();
        executionService.clearRunningInfo(shardingService.getLocalHostShardingItems());
        if (serverService.isJobStopedManually()) {
            return;
        }
        try {
            scheduler.resumeAll();
        } catch (final SchedulerException ex) {
            throw new JobException(ex);
        }
    }
    
    /**
     * 立刻启动作业.
     */
    public void triggerJob() {
        try {
            scheduler.triggerJob(jobDetail.getKey());
        } catch (final SchedulerException ex) {
            throw new JobException(ex);
        }
    }
    
    /**
     * 关闭调度器.
     */
    public void shutdown() {
        try {
            scheduler.shutdown();
        } catch (final SchedulerException ex) {
            throw new JobException(ex);
        }
    }
    
    /**
     * 重新调度作业.
     */
    public void rescheduleJob(final String cronExpression) {
        try {
            scheduler.rescheduleJob(TriggerKey.triggerKey(Joiner.on("_").join(jobConfiguration.getJobName(), CRON_TRIGGER_INDENTITY_SUFFIX)), createTrigger(cronExpression));
        } catch (final SchedulerException ex) {
            throw new JobException(ex);
        } 
    }
}
