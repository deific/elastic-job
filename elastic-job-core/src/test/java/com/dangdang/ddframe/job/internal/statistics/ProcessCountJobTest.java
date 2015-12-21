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

package com.dangdang.ddframe.job.internal.statistics;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionException;

import com.dangdang.ddframe.job.internal.AbstractBaseJobTest;

public final class ProcessCountJobTest extends AbstractBaseJobTest {
    
    private final ProcessCountJob processCountJob = new ProcessCountJob(getRegistryCenter(), getJobConfig());
    
    @Before
    public void setUp() {
        ProcessCountStatistics.incrementProcessSuccessCount("testJob");
        ProcessCountStatistics.incrementProcessSuccessCount("otherTestJob");
        ProcessCountStatistics.incrementProcessFailureCount("testJob");
        ProcessCountStatistics.incrementProcessFailureCount("otherTestJob");
    }
    
    @Test
    public void assertRun() throws JobExecutionException {
        processCountJob.run();
        assertThat(getRegistryCenter().get("/testJob/servers/" + jobNodeService.getNodeName() + "/processSuccessCount"), is("1"));
        assertFalse(getRegistryCenter().isExisted("/otherTestJob/servers/" + jobNodeService.getNodeName() + "/processSuccessCount"));
        assertThat(getRegistryCenter().get("/testJob/servers/" + jobNodeService.getNodeName() + "/processFailureCount"), is("1"));
        assertFalse(getRegistryCenter().isExisted("/otherTestJob/servers/" + jobNodeService.getNodeName() + "/processFailureCount"));
        processCountJob.run();
        assertThat(getRegistryCenter().get("/testJob/servers/" + jobNodeService.getNodeName() + "/processSuccessCount"), not("0"));
        assertFalse(getRegistryCenter().isExisted("/otherTestJob/servers/" + jobNodeService.getNodeName() + "/processSuccessCount"));
        assertThat(getRegistryCenter().get("/testJob/servers/" + jobNodeService.getNodeName() + "/processFailureCount"), not("0"));
        assertFalse(getRegistryCenter().isExisted("/otherTestJob/servers/" + jobNodeService.getNodeName() + "/processFailureCount"));
    }
}
