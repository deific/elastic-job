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

package com.dangdang.ddframe.job.integrate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import lombok.AccessLevel;
import lombok.Getter;

import org.junit.After;
import org.junit.Before;
import org.quartz.SchedulerException;
import org.unitils.util.ReflectionUtils;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.JobConfiguration;
import com.dangdang.ddframe.job.api.JobScheduler;
import com.dangdang.ddframe.job.internal.election.LeaderElectionService;
import com.dangdang.ddframe.job.internal.env.JobNodeService;
import com.dangdang.ddframe.job.internal.env.LocalJobNodeService;
import com.dangdang.ddframe.job.internal.schedule.JobRegistry;
import com.dangdang.ddframe.job.internal.server.ServerStatus;
import com.dangdang.ddframe.job.internal.statistics.ProcessCountStatistics;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperRegistryCenter;
import com.dangdang.ddframe.test.NestedZookeeperServers;
import com.dangdang.ddframe.test.WaitingUtils;

public abstract class AbstractBaseStdJobTest {
    

    
    private final ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(NestedZookeeperServers.ZK_CONNECTION_STRING, "zkRegTestCenter", 1000, 3000, 3);
    
    @Getter(AccessLevel.PROTECTED)
    private final CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(zkConfig);
    
    @Getter(AccessLevel.PROTECTED)
    private final JobNodeService localHostService = new LocalJobNodeService(regCenter);
    
    @Getter(AccessLevel.PROTECTED)
    private final JobConfiguration jobConfig;
    
    private final JobScheduler jobScheduler;
    
    private final boolean disabled;
    
    private final LeaderElectionService leaderElectionService;
    
    protected AbstractBaseStdJobTest(final Class<? extends ElasticJob> elasticJobClass, final boolean disabled) {
        jobConfig = new JobConfiguration("testJob", elasticJobClass, 3, "0/1 * * * * ?");
        jobConfig.setDisabled(disabled);
        jobScheduler = new JobScheduler(regCenter, jobConfig);
        this.disabled = disabled;
        leaderElectionService = new LeaderElectionService(regCenter, jobConfig);
    }
    
    @Before
    public void setUp() {
        ProcessCountStatistics.reset("testJob");
        NestedZookeeperServers.getInstance().startServerIfNotStarted();
        jobConfig.setShardingItemParameters("0=A,1=B,2=C");
        jobConfig.setOverwrite(true);
        regCenter.init();
    }
    
    @After
    public void tearDown() throws SchedulerException, NoSuchFieldException {
        ProcessCountStatistics.reset("testJob");
        JobRegistry.getInstance().getJobScheduler("testJob").shutdown();
        WaitingUtils.waitingLongTime();
        ReflectionUtils.setFieldValue(JobRegistry.getInstance(), "instance", null);
        regCenter.remove("/testJob");
        regCenter.close();
        WaitingUtils.waitingLongTime();
    }
    
    protected void initJob() {
        jobScheduler.init();
    }
    
    protected void assertRegCenterCommonInfo() {
        assertThat(regCenter.get("/testJob/leader/election/host"), is(localHostService.getNodeName()));
        assertThat(regCenter.get("/testJob/config/shardingTotalCount"), is("3"));
        assertThat(regCenter.get("/testJob/config/shardingItemParameters"), is("0=A,1=B,2=C"));
        assertThat(regCenter.get("/testJob/config/cron"), is("0/1 * * * * ?"));
        assertThat(regCenter.get("/testJob/servers/" + localHostService.getNodeName() + "/hostName"), is(localHostService.getHostName()));
        if (disabled) {
            assertTrue(regCenter.isExisted("/testJob/servers/" + localHostService.getNodeName() + "/disabled"));
        } else {
            assertFalse(regCenter.isExisted("/testJob/servers/" + localHostService.getNodeName() + "/disabled"));
        }
        assertFalse(regCenter.isExisted("/testJob/servers/" + localHostService.getNodeName() + "/stoped"));
        assertThat(regCenter.get("/testJob/servers/" + localHostService.getNodeName() + "/status"), is(ServerStatus.READY.name()));
        regCenter.remove("/testJob/leader/election");
        assertTrue(leaderElectionService.isLeader());
    }
}
