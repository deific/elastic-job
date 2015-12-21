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

package com.dangdang.ddframe.job.integrate.fixture.dataflow.throughput;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.AbstractThroughputDataFlowElasticJob;

public final class OneOffThroughputDataFlowElasticJob extends AbstractThroughputDataFlowElasticJob<String> {
    
    private static Set<String> processedData = new CopyOnWriteArraySet<>();
    
    private static List<String> result = Arrays.asList("data0", "data1", "data2", "data3", "data4", "data5", "data6", "data7", "data8", "data9");
    
    @Override
    public List<String> fetchData(final JobExecutionMultipleShardingContext context) {
        return result;
    }
    
    @Override
    public boolean processData(final JobExecutionMultipleShardingContext context, final String data) {
        processedData.add(data);
        return true;
    }
    
    @Override
    public boolean isStreamingProcess() {
        return false;
    }
    
    public static boolean isCompleted() {
        return result.size() == processedData.size();
    }
    
    public static void reset() {
        processedData.clear();
    }


}
