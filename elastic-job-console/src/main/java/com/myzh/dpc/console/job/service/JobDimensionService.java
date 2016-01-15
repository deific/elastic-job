
package com.myzh.dpc.console.job.service;

import java.util.Collection;

import com.myzh.dpc.console.job.domain.ExecutionInfo;
import com.myzh.dpc.console.job.domain.JobBriefInfo;
import com.myzh.dpc.console.job.domain.JobServer;
import com.myzh.dpc.console.job.domain.JobSettings;

public interface JobDimensionService {
    
    Collection<JobBriefInfo> getAllJobsBriefInfo();
    
    JobSettings getJobSettings(String jobName);
    
    void updateJobSettings(JobSettings jobSettings);
    
    Collection<JobServer> getServers(String jobName);
    
    Collection<ExecutionInfo> getExecutionInfo(String jobName);
}
