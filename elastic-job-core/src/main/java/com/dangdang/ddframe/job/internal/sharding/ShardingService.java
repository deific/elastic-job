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

package com.dangdang.ddframe.job.internal.sharding;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.slf4j.Slf4j;

import com.dangdang.ddframe.job.api.JobConfiguration;
import com.dangdang.ddframe.job.internal.config.ConfigurationService;
import com.dangdang.ddframe.job.internal.election.LeaderElectionService;
import com.dangdang.ddframe.job.internal.env.JobNodeService;
import com.dangdang.ddframe.job.internal.env.LocalJobNodeService;
import com.dangdang.ddframe.job.internal.execution.ExecutionService;
import com.dangdang.ddframe.job.internal.server.ServerService;
import com.dangdang.ddframe.job.internal.sharding.strategy.JobShardingStrategy;
import com.dangdang.ddframe.job.internal.sharding.strategy.JobShardingStrategyFactory;
import com.dangdang.ddframe.job.internal.sharding.strategy.JobShardingStrategyOption;
import com.dangdang.ddframe.job.internal.storage.JobNodeStorage;
import com.dangdang.ddframe.job.internal.util.BlockUtils;
import com.dangdang.ddframe.job.internal.util.ItemUtils;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;

/**
 * 作业分片服务.
 * 
 * @author zhangliang
 */
@Slf4j
public final class ShardingService {
    
    private final String jobName;
    
    private final JobNodeStorage jobNodeStorage;
    
    private final JobNodeService jobNodeService;
    
    private final LeaderElectionService leaderElectionService;
    
    private final ConfigurationService configService;
    
    private final ServerService serverService;
    
    private final ExecutionService executionService;
    
    public ShardingService(final CoordinatorRegistryCenter coordinatorRegistryCenter, final JobConfiguration jobConfiguration) {
        jobName = jobConfiguration.getJobName();
        jobNodeService = new LocalJobNodeService(coordinatorRegistryCenter);
        jobNodeStorage = new JobNodeStorage(coordinatorRegistryCenter, jobConfiguration);
        leaderElectionService = new LeaderElectionService(coordinatorRegistryCenter, jobConfiguration);
        configService = new ConfigurationService(coordinatorRegistryCenter, jobConfiguration);
        serverService = new ServerService(coordinatorRegistryCenter, jobConfiguration);
        executionService = new ExecutionService(coordinatorRegistryCenter, jobConfiguration);
    }
    
    /**
     * 设置需要重新分片的标记.
     */
    public void setReshardingFlag() {
        jobNodeStorage.createJobNodeIfNeeded(ShardingNode.NECESSARY);
    }
    
    /**判断是否需要重分片.
     * 
     * @return 是否需要重分片
     */
    public boolean isNeedSharding() {
        return jobNodeStorage.isJobNodeExisted(ShardingNode.NECESSARY);
    }
    
    /**
     * 如果需要分片且当前节点为主节点, 则作业分片.
     */
    public void shardingIfNecessary() {
        if (!isNeedSharding()) {
            return;
        }
        if (!leaderElectionService.isLeader()) {
            blockUntilShardingCompleted();
            return;
        }
        if (configService.isMonitorExecution()) {
            waitingOtherJobCompleted();
        }
        log.debug("Elastic job: sharding begin.");
        jobNodeStorage.fillEphemeralJobNode(ShardingNode.PROCESSING, "");
        clearShardingInfo();
        JobShardingStrategy jobShardingStrategy = JobShardingStrategyFactory.getStrategy(configService.getJobShardingStrategyClass());
        JobShardingStrategyOption option = new JobShardingStrategyOption(jobName, configService.getShardingTotalCount(), configService.getShardingItemParameters());
        persistShardingInfo(jobShardingStrategy.sharding(serverService.getAvailableServers(), option));
        jobNodeStorage.removeJobNodeIfExisted(ShardingNode.NECESSARY);
        jobNodeStorage.removeJobNodeIfExisted(ShardingNode.PROCESSING);
        log.debug("Elastic job: sharding completed.");
    }
    
    private void clearShardingInfo() {
        for (String each : serverService.getAllServers()) {
            jobNodeStorage.removeJobNodeIfExisted(ShardingNode.getShardingNode(each));
        }
    }
    
    private void persistShardingInfo(final Map<String, List<Integer>> shardingItems) {
        for (Entry<String, List<Integer>> entry : shardingItems.entrySet()) {
            jobNodeStorage.replaceJobNode(ShardingNode.getShardingNode(entry.getKey()), ItemUtils.toItemsString(entry.getValue()));
        }
    }
    
    private void blockUntilShardingCompleted() {
        while (jobNodeStorage.isJobNodeExisted(ShardingNode.NECESSARY) || jobNodeStorage.isJobNodeExisted(ShardingNode.PROCESSING)) {
            log.debug("Elastic job: sleep short time until sharding completed.");
            BlockUtils.waitingShortTime();
        }
    }
    
    private void waitingOtherJobCompleted() {
        while (executionService.hasRunningItems()) {
            log.debug("Elastic job: sleep short time until other job completed.");
            BlockUtils.waitingShortTime();
        }
    }
    
    /**
     * 获取运行在本作业服务器的分片序列号.
     * 
     * @return 运行在本作业服务器的分片序列号
     */
    public List<Integer> getLocalHostShardingItems() {
        String ip = jobNodeService.getNodeName();
        if (!jobNodeStorage.isJobNodeExisted(ShardingNode.getShardingNode(ip))) {
            return Collections.<Integer>emptyList();
        }
        return ItemUtils.toItemList(jobNodeStorage.getJobNodeDataDirectly(ShardingNode.getShardingNode(ip)));
    }
}
