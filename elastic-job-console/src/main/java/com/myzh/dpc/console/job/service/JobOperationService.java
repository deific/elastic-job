
package com.myzh.dpc.console.job.service;

public interface JobOperationService {
    
    void stopJob(String jobName, String serverIp);
    
    void resumeJob(String jobName, String serverIp);
    
    void stopAllJobsByJobName(String jobName);
    
    void resumeAllJobsByJobName(String jobName);
    
    void stopAllJobsByServer(String serverIp);
    
    void resumeAllJobsByServer(String serverIp);
}
