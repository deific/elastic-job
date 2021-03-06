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

package com.dangdang.ddframe.reg.base;

import java.util.List;

/**
 * 用于协调分布式服务的注册中心.
 * 
 * @author zhangliang
 */
public interface CoordinatorRegistryCenter extends RegistryCenter {
    
    /**
     * 直接从注册中心而非本地缓存获取数据.
     * 
     * @param key 键
     * @return 值
     */
    String getDirectly(String key);
    
    /**
     * 获取子节点名称集合.
     * 
     * @param key 键
     * @return 子节点名称集合
     */
    List<String> getChildrenKeys(String key);
    
    /**
     * 持久化临时注册数据.
     * 
     * @param key 键
     * @param value 值
     */
    void persistEphemeral(String key, String value);
    
    /**
     * 持久化临时顺序注册数据.
     * 
     * @param key
     */
    void persistEphemeralSequential(String key);
    
    /**
     * 获取注册中心数据缓存对象.
     * 
     * @return 注册中心数据缓存对象
     */
    Object getRawCache();
    
    /**
     * 获取在主持中心注册的任务节点名称
     */
    String getJobNodeName();
}
