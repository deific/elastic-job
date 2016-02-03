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

package com.dangdang.ddframe.job.plugin.job.type;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.api.JobExecutionSingleShardingContext;
import com.dangdang.ddframe.job.internal.job.AbstractDataFlowElasticJob;

/**
 * 保证同一分片顺序性的处理数据流程的作业.
 * 
 * @author zhangliang
 * 
 * @param <T> 数据流作业处理的数据实体类型
 */
@Slf4j
public abstract class AbstractSequenceDataFlowElasticJob<T> extends AbstractDataFlowElasticJob<T, JobExecutionSingleShardingContext> {
    
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    
    /**
     * 作业执行入口
     */
    @Override
    protected final void executeJob(final JobExecutionMultipleShardingContext shardingContext) {
    	long startTime = System.currentTimeMillis();
    	log.info("starting execute job:{}", shardingContext.getJobName());
        if (isStreamingProcess()) {
            executeStreamingJob(shardingContext);
        } else {
            executeOneOffJob(shardingContext);
        }
        long endTime = System.currentTimeMillis();
        log.info("completed job:{}, times：{} ms", shardingContext.getJobName(), (endTime - startTime));
    }
    /**
     * 流式作业
     * 循环调用读取数据处理数据作业
     * @param shardingContext
     */
    private void executeStreamingJob(final JobExecutionMultipleShardingContext shardingContext) {
        Map<Integer, List<T>> data = concurrentFetchData(shardingContext);
        while (!data.isEmpty() && !isStoped() && !getShardingService().isNeedSharding()) {
            concurrentProcessData(shardingContext, data);
            data = concurrentFetchData(shardingContext);
        }
    }
    /**
     * 单次作业
     * @param shardingContext
     */
    private void executeOneOffJob(final JobExecutionMultipleShardingContext shardingContext) {
        // 读取所有分片作业数据
    	Map<Integer, List<T>> data = concurrentFetchData(shardingContext);
        if (!data.isEmpty()) {
        	// 处理作业数据
            concurrentProcessData(shardingContext, data);
        }
    }
    
    /**
     * 读取数据
     * @param shardingContext
     * @return
     */
    private Map<Integer, List<T>> concurrentFetchData(final JobExecutionMultipleShardingContext shardingContext) {
        // 获取分片信息
    	List<Integer> items = shardingContext.getShardingItems();
        final Map<Integer, List<T>> result = new ConcurrentHashMap<>(items.size());
        final CountDownLatch latch = new CountDownLatch(items.size());
        
        // 执行每个分片任务，当所有分片任务结束后执行数据处理
        for (final int each : items) {
        	executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<T> data = fetchDataWithStatistics(shardingContext.createJobExecutionSingleShardingContext(each));
                        if (null != data && !data.isEmpty()) {
                            result.put(each, data);
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }
        latchAwait(latch);
        log.debug("Elastic job: fetch data size: {}.", result != null ? result.size() : 0);
        return result;
    }
    
    /**
     * 处理数据
     * @param shardingContext
     * @param data
     */
    private void concurrentProcessData(final JobExecutionMultipleShardingContext shardingContext, final Map<Integer, List<T>> data) {
        final CountDownLatch latch = new CountDownLatch(data.size());
        // 执行每个分片任务的数据处理任务
        for (final Entry<Integer, List<T>> each : data.entrySet()) {
            executorService.submit(new Runnable() {
                
                @Override
                public void run() {
                    try {
                        processDataWithStatistics(shardingContext.createJobExecutionSingleShardingContext(each.getKey()), each.getValue());
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }
        latchAwait(latch);
    }
    
	public void updateSharingItemDataOffset(JobExecutionSingleShardingContext shardingContext, T data) {
	}
}
