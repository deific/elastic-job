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
 * 为测试使用的获取本地网络的服务.
 * 
 * @author zhangliang
 */
public final class FakeLocalHostService implements JobNodeService {
    
    private final String localHostIp;
    
    public FakeLocalHostService(final String localHostIp) {
        this.localHostIp = localHostIp;
    }
    
    @Override
    public String getNodeName() {
        return localHostIp;
    }
    
    @Override
    public String getHostName() {
        return localHostIp + "_hostMame";
    }
}
