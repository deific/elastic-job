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

import com.dangdang.ddframe.job.internal.env.JobNodeService;
import com.dangdang.ddframe.job.internal.env.LocalJobNodeService;
import com.dangdang.ddframe.job.internal.storage.JobNodePath;

/**
 * Elastic Job服务器节点名称的常量类.
 * 
 * @author zhangliang
 */
public final class ServerNode {
    
    /**
     * 作业服务器信息根节点.
     */
    public static final String ROOT = "servers";
    
    static final String HOST_NAME = ROOT + "/%s/hostName";
    
    static final String STATUS_APPENDIX = "status";
    
    static final String STATUS = ROOT + "/%s/" + STATUS_APPENDIX;
    
    static final String DISABLED_APPENDIX = "disabled";
    
    static final String DISABLED = ROOT + "/%s/" + DISABLED_APPENDIX;
    
    static final String PROCESS_SUCCESS_COUNT = ROOT + "/%s/processSuccessCount";
    
    static final String PROCESS_FAILURE_COUNT = ROOT + "/%s/processFailureCount";
    
    static final String STOPED = ROOT + "/%s/stoped";
    
    private final JobNodeService jobNodeService;
    
    private final JobNodePath jobNodePath;
    
    public ServerNode(JobNodeService jobNodeService, final String jobName) {
    	this.jobNodeService = jobNodeService;
        jobNodePath = new JobNodePath(jobName);
    }
    
    static String getHostNameNode(final String ip) {
        return String.format(HOST_NAME, ip);
    }
    
    static String getStatusNode(final String ip) {
        return String.format(STATUS, ip);
    }
    
    static String getDisabledNode(final String ip) {
        return String.format(DISABLED, ip);
    }
    
    static String getProcessSuccessCountNode(final String ip) {
        return String.format(PROCESS_SUCCESS_COUNT, ip);
    }
    
    static String getProcessFailureCountNode(final String ip) {
        return String.format(PROCESS_FAILURE_COUNT, ip);
    }
    
    static String getStopedNode(final String ip) {
        return String.format(STOPED, ip);
    }
    
    /**
     * 判断给定路径是否为作业服务器状态路径.
     * 
     * @param path 待判断的路径
     * @return 是否为作业服务器状态路径
     */
    public boolean isServerStatusPath(final String path) {
        return path.startsWith(jobNodePath.getFullPath(ServerNode.ROOT)) && path.endsWith(ServerNode.STATUS_APPENDIX);
    }
    
    /**
     * 判断给定路径是否为作业服务器禁用路径.
     * 
     * @param path 待判断的路径
     * @return 是否为作业服务器禁用路径
     */
    public boolean isServerDisabledPath(final String path) {
        return path.startsWith(jobNodePath.getFullPath(ServerNode.ROOT)) && path.endsWith(ServerNode.DISABLED_APPENDIX);
    }
    
    /**
     * 判断给定路径是否为作业停止状态路径.
     * 
     * @return 是否为作业停止状态路径.
     */
    public boolean isJobStopedPath(final String path) {
        return path.startsWith(jobNodePath.getFullPath(String.format(ServerNode.STOPED, jobNodeService.getNodeName())));
    }
}
