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

package com.dangdang.ddframe.job.internal.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.internal.config.ConfigurationService;
import com.dangdang.ddframe.job.internal.execution.ExecutionContextService;
import com.dangdang.ddframe.job.internal.execution.ExecutionService;
import com.dangdang.ddframe.job.internal.failover.FailoverService;
import com.dangdang.ddframe.job.internal.offset.OffsetService;
import com.dangdang.ddframe.job.internal.schedule.JobRegistry;
import com.dangdang.ddframe.job.internal.sharding.ShardingService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 弹性化分布式作业的基类.
 * 
 */
@Slf4j
public abstract class AbstractElasticJob implements ElasticJob {
	@Setter
    @Getter
	private String jobName;
	
    @Getter(AccessLevel.PROTECTED)
    private volatile boolean stoped;
    
    @Getter(AccessLevel.PROTECTED)
    private ConfigurationService configService;
    
    @Setter
    @Getter(AccessLevel.PROTECTED)
    private ShardingService shardingService;
    
    @Setter
    private ExecutionContextService executionContextService;
    
    @Setter
    private ExecutionService executionService;
    
    @Setter
    private FailoverService failoverService;
    
    @Setter
    @Getter(AccessLevel.PROTECTED)
    private OffsetService offsetService;
    
    
    /**
     * 作业执行入口
     */
    @Override
    public final void execute(final JobExecutionContext context) throws JobExecutionException {
        log.trace("Elastic job: job execute begin, job execution context:{}.", context);
        try {
	        // 检测本机与注册中心的时间误差
	        configService.checkMaxTimeDiffSecondsTolerable();
	        shardingService.shardingIfNecessary();
	        // 获取作业执行上下文
	        JobExecutionMultipleShardingContext shardingContext = executionContextService.getJobExecutionShardingContext();
	        
	        // 如果前一作业未执行完毕，放弃该次执行
	        if (executionService.misfireIfNecessary(shardingContext.getShardingItems())) {
	            log.info("Elastic job: previous job is still running, new job will start after previous job completed. Misfired job had recorded.");
	            return;
	        }
	        executionService.cleanPreviousExecutionInfo();
	        
	        // 执行作业
	        executeJobInternal(shardingContext);
	        
	        log.trace("Elastic job: execute normal completed, sharding context:{}.", shardingContext);
	        // 如果开启失效转移配置，并且存在失效转移的作业，本作业也未停止且不需分片时，执行转移到本作业的他分片作业
	        while (configService.isMisfire() && 
	        		!executionService.getMisfiredJobItems(shardingContext.getShardingItems()).isEmpty() 
	        		&& !stoped && !shardingService.isNeedSharding()) {
	            log.trace("Elastic job: execute misfired job, sharding context:{}.", shardingContext);
	            executionService.clearMisfire(shardingContext.getShardingItems());
	            executeJobInternal(shardingContext);
	            log.trace("Elastic job: misfired job completed, sharding context:{}.", shardingContext);
	        }
	        if (configService.isFailover() && !stoped) {
	            failoverService.failoverIfNecessary();
	        }
        } catch (final Exception ex) {
            handleJobExecutionException(new JobExecutionException(ex));
        }
        log.trace("Elastic job: execute all completed, job execution context:{}.", context);
    }
    
    /**
     * 执行作业
     * @param shardingContext
     * @throws JobExecutionException 
     */
    private void executeJobInternal(final JobExecutionMultipleShardingContext shardingContext) throws JobExecutionException {
    	// 如果作业分片为空
        if (shardingContext.getShardingItems().isEmpty()) {
            log.trace("Elastic job: sharding item is empty, job execution context:{}.", shardingContext);
            return;
        }
        // 向zookeeper注册作业启动信息
        executionService.registerJobBegin(shardingContext);
        
        executeJob(shardingContext);
        
        executionService.registerJobCompleted(shardingContext);
        // 如果开启了失效转移，更新失效专业作业信息
        if (configService.isFailover()) {
            failoverService.updateFailoverComplete(shardingContext.getShardingItems());
        }
    }
    
    /**
     * 由子类实现
     * @param shardingContext
     */
    protected abstract void executeJob(final JobExecutionMultipleShardingContext shardingContext);
    
    @Override
    public void handleJobExecutionException(final JobExecutionException jobExecutionException) throws JobExecutionException {
        log.error("任务执行异常：{}", jobExecutionException);
    }
    
    @Override
    public final void stop() {
        stoped = true;
    }
    
    @Override
    public final void resume() {
        stoped = false;
    }
    
    public final void setConfigService(final ConfigurationService configService) {
        this.configService = configService;
        JobRegistry.getInstance().addJobInstance(configService.getJobName(), this);
    }
}
