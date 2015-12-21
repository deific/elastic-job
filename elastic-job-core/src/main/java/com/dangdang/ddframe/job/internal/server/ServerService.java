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

package com.dangdang.ddframe.job.internal.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dangdang.ddframe.job.api.JobConfiguration;
import com.dangdang.ddframe.job.internal.election.LeaderElectionService;
import com.dangdang.ddframe.job.internal.env.JobNodeService;
import com.dangdang.ddframe.job.internal.env.LocalJobNodeService;
import com.dangdang.ddframe.job.internal.storage.JobNodeStorage;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;

/**
 * 作业服务器节点服务.
 * 
 * @author zhangliang, caohao
 */
public final class ServerService {
    
    private final JobNodeStorage jobNodeStorage;
    
    private final JobNodeService jobNodeService;
    
    private final LeaderElectionService leaderElectionService;
    
    public ServerService(final CoordinatorRegistryCenter coordinatorRegistryCenter, final JobConfiguration jobConfiguration) {
    	jobNodeService = new LocalJobNodeService(coordinatorRegistryCenter);
        jobNodeStorage = new JobNodeStorage(coordinatorRegistryCenter, jobConfiguration);
        leaderElectionService = new LeaderElectionService(coordinatorRegistryCenter, jobConfiguration);
    }
    
    /**
     * 持久化作业服务器上线相关信息.
     */
    public void persistServerOnline() {
        if (!leaderElectionService.hasLeader()) {
            leaderElectionService.leaderElection();
        }
        persistHostName();
        persistDisabled();
        ephemeralPersistServerReady();
    }
    
    private void persistHostName() {
        jobNodeStorage.fillJobNodeIfNullOrOverwrite(ServerNode.getHostNameNode(jobNodeService.getNodeName()), jobNodeService.getHostName());
    }
    
    private void persistDisabled() {
        if (!jobNodeStorage.getJobConfiguration().isOverwrite()) {
            return;
        }
        if (jobNodeStorage.getJobConfiguration().isDisabled()) {
            jobNodeStorage.fillJobNodeIfNullOrOverwrite(ServerNode.getDisabledNode(jobNodeService.getNodeName()), "");
        } else {
            jobNodeStorage.removeJobNodeIfExisted(ServerNode.getDisabledNode(jobNodeService.getNodeName()));
        }
    }
    
    private void ephemeralPersistServerReady() {
        jobNodeStorage.fillEphemeralJobNode(ServerNode.getStatusNode(jobNodeService.getNodeName()), ServerStatus.READY);
    }
    
    /**
     * 清除停止作业的标记.
     */
    public void clearJobStopedStatus() {
        jobNodeStorage.removeJobNodeIfExisted(ServerNode.getStopedNode(jobNodeService.getNodeName()));
    }
    
    /**
     * 判断是否是手工停止的作业.
     * 
     * @return 是否是手工停止的作业
     */
    public boolean isJobStopedManually() {
        return jobNodeStorage.isJobNodeExisted(ServerNode.getStopedNode(jobNodeService.getNodeName()));
    }
    
    /**
     * 在开始或结束执行作业时更新服务器状态.
     * 
     * @param status 服务器状态
     */
    public void updateServerStatus(final ServerStatus status) {
        jobNodeStorage.updateJobNode(ServerNode.getStatusNode(jobNodeService.getNodeName()), status);
    }
    
    /**
     * 获取所有的作业服务器列表.
     * 
     * @return 所有的作业服务器列表
     */
    public List<String> getAllServers() {
        List<String> result = jobNodeStorage.getJobNodeChildrenKeys(ServerNode.ROOT);
        Collections.sort(result);
        return result;
    }
    
    /**
     * 获取可用的作业服务器列表.
     * 
     * @return 可用的作业服务器列表
     */
    public List<String> getAvailableServers() {
        List<String> servers = getAllServers();
        List<String> result = new ArrayList<>(servers.size());
        for (String each : servers) {
            if (isAvailableServer(each)) {
                result.add(each);
            }
        }
        Collections.sort(result);
        return result;
    }
    
    private Boolean isAvailableServer(final String ip) {
        return jobNodeStorage.isJobNodeExisted(ServerNode.getStatusNode(ip)) && !jobNodeStorage.isJobNodeExisted(ServerNode.getDisabledNode(ip));
    }
    
    /**
     * 判断当前服务器是否是等待执行的状态.
     * 
     * @return 当前服务器是否是等待执行的状态
     */
    public boolean isServerReady() {
        if (jobNodeStorage.isJobNodeExisted(ServerNode.getDisabledNode(jobNodeService.getNodeName()))) {
            return false;
        }
        if (jobNodeStorage.isJobNodeExisted(ServerNode.getStopedNode(jobNodeService.getNodeName()))) {
            return false;
        }
        String statusNode = ServerNode.getStatusNode(jobNodeService.getNodeName());
        if (jobNodeStorage.isJobNodeExisted(statusNode) && ServerStatus.READY.name().equals(jobNodeStorage.getJobNodeData(statusNode))) {
            return true;
        }
        return false;
    }
    
    /**
     * 持久化统计处理数据成功的数量的数据.
     */
    public void persistProcessSuccessCount(final int processSuccessCount) {
    	String node = ServerNode.getProcessSuccessCountNode(jobNodeService.getNodeName());
    	String count = jobNodeStorage.getJobNodeData(node);
    	count = (count == null || "".equals(count))?"0":count;
        jobNodeStorage.replaceJobNode(node, processSuccessCount + Integer.valueOf(count));
    }
    
    /**
     * 持久化统计处理数据失败的数量的数据.
     */
    public void persistProcessFailureCount(final int processFailureCount) {
    	String node = ServerNode.getProcessFailureCountNode(jobNodeService.getNodeName());
    	String count = jobNodeStorage.getJobNodeData(node);
    	count = (count == null || "".equals(count))?"0":count;
        jobNodeStorage.replaceJobNode(node, processFailureCount + Integer.valueOf(count));
    }
}
