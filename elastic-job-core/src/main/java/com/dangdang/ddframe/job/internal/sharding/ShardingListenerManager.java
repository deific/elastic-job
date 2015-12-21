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

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type;

import com.dangdang.ddframe.job.api.JobConfiguration;
import com.dangdang.ddframe.job.internal.config.ConfigurationNode;
import com.dangdang.ddframe.job.internal.env.JobNodeService;
import com.dangdang.ddframe.job.internal.env.LocalJobNodeService;
import com.dangdang.ddframe.job.internal.execution.ExecutionService;
import com.dangdang.ddframe.job.internal.listener.AbstractJobListener;
import com.dangdang.ddframe.job.internal.listener.AbstractListenerManager;
import com.dangdang.ddframe.job.internal.server.ServerNode;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;

/**
 * 分片监听管理器.
 * 
 * @author zhangliang
 */
public final class ShardingListenerManager extends AbstractListenerManager {
    
    private final ShardingService shardingService;
    
    private final ExecutionService executionService;
    
    private final ConfigurationNode configurationNode;
    
    private final ServerNode serverNode;
    
    private final JobNodeService jobNodeService;
    
    public ShardingListenerManager(final CoordinatorRegistryCenter coordinatorRegistryCenter, final JobConfiguration jobConfiguration) {
        super(coordinatorRegistryCenter, jobConfiguration);
        jobNodeService = new LocalJobNodeService(coordinatorRegistryCenter);
        shardingService = new ShardingService(coordinatorRegistryCenter, jobConfiguration);
        executionService = new ExecutionService(coordinatorRegistryCenter, jobConfiguration);
        configurationNode = new ConfigurationNode(jobConfiguration.getJobName());
        serverNode = new ServerNode(jobNodeService, jobConfiguration.getJobName());
    }
    
    @Override
    public void start() {
        listenShardingTotalCountChanged();
        listenServersChanged();
    }
    
    void listenShardingTotalCountChanged() {
        addDataListener(new AbstractJobListener() {
            
            @Override
            protected void dataChanged(final CuratorFramework client, final TreeCacheEvent event, final String path) {
                if (configurationNode.isShardingTotalCountPath(path)) {
                    shardingService.setReshardingFlag();
                    executionService.setNeedFixExecutionInfoFlag();
                }
            }
        });
    }
    
    void listenServersChanged() {
        addDataListener(new AbstractJobListener() {
            
            @Override
            protected void dataChanged(final CuratorFramework client, final TreeCacheEvent event, final String path) {
                if (isServersCrashed(event, path) || serverNode.isServerDisabledPath(path)) {
                    shardingService.setReshardingFlag();
                }
            }
            
            private boolean isServersCrashed(final TreeCacheEvent event, final String path) {
                return Type.NODE_UPDATED != event.getType() && serverNode.isServerStatusPath(path);
            }
        });
    }
}
