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

package com.dangdang.ddframe.job.internal.env;

/**
 * 本机网络服务接口.
 * 
 * @author zhangliang
 */
public interface JobNodeService {
    
    /**
     * 获取本机IP地址.
     * 
     * <p>
     * 有限获取外网IP地址.
     * 也有可能是链接着路由器的最终IP地址.
     * </p>
     * 
     * @return 本机IP地址
     */
    String getNodeName();
    
    /**
     * 获取本机Host名称.
     * 
     * @return 本机Host名称
     */
    String getHostName();
    
}
