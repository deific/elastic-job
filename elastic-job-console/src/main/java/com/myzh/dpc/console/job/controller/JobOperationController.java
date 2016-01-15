
package com.myzh.dpc.console.job.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.myzh.dpc.console.job.domain.JobServer;
import com.myzh.dpc.console.job.service.JobOperationService;

@RestController
@RequestMapping("job")
public class JobOperationController {
    
    @Resource
    private JobOperationService jobOperationService;
    
    @RequestMapping(value = "stop", method = RequestMethod.POST)
    public void stopJob(final JobServer jobServer) {
        jobOperationService.stopJob(jobServer.getJobName(), jobServer.getIp());
    }
    
    @RequestMapping(value = "resume", method = RequestMethod.POST)
    public void resumeJob(final JobServer jobServer) {
        jobOperationService.resumeJob(jobServer.getJobName(), jobServer.getIp());
    }
    
    @RequestMapping(value = "stopAll/name", method = RequestMethod.POST)
    public void stopAllJobsByJobName(final JobServer jobServer) {
        jobOperationService.stopAllJobsByJobName(jobServer.getJobName());
    }
    
    @RequestMapping(value = "resumeAll/name", method = RequestMethod.POST)
    public void resumeAllJobsByJobName(final JobServer jobServer) {
        jobOperationService.resumeAllJobsByJobName(jobServer.getJobName());
    }
    
    @RequestMapping(value = "stopAll/ip", method = RequestMethod.POST)
    public void stopAllJobs(final JobServer jobServer) {
        jobOperationService.stopAllJobsByServer(jobServer.getIp());
    }
    
    @RequestMapping(value = "resumeAll/ip", method = RequestMethod.POST)
    public void resumeAllJobs(final JobServer jobServer) {
        jobOperationService.resumeAllJobsByServer(jobServer.getIp());
    }
}
