
package com.myzh.dpc.console.job.service;

import java.util.Collection;

import com.myzh.dpc.console.job.domain.JobServer;
import com.myzh.dpc.console.job.domain.ServerBriefInfo;

public interface ServerDimensionService {
    
    Collection<ServerBriefInfo> getAllServersBriefInfo();
    
    Collection<JobServer> getJobs(String serverIp);
}
