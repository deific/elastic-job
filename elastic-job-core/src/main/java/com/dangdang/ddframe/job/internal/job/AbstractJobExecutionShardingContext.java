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

import lombok.Getter;
import lombok.Setter;

/**
 * 作业运行时分片上下文抽象类.
 * 
 * @author zhangliang
 */

public abstract class AbstractJobExecutionShardingContext {
    
    /**
     * 作业名称.
     */
	@Getter
	@Setter
    private String jobName;
    
    /**
     * 分片总数.
     */
	@Getter
	@Setter
    private int shardingTotalCount;
    
    /**
     * 作业自定义参数.
     * 可以配置多个相同的作业, 但是用不同的参数作为不同的调度实例.
     */
	@Getter
	@Setter
    private String jobParameter;
	
	/**
     * 作业自定义参数.
     * 可以配置多个相同的作业, 但是用不同的参数作为不同的调度实例.
     */
	@Getter
	@Setter
    private String fetchDataOffset;
    
    /**
     * 监控作业执行时状态.
     */
	@Getter
	@Setter
    private boolean monitorExecution;
    
    /**
     * 每次抓取的数据量.
     */
	@Getter
	@Setter
    private int fetchDataCount;
}
