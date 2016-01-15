
package com.myzh.dpc.console.job.controller;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.myzh.dpc.console.job.domain.ExecutionInfo;
import com.myzh.dpc.console.job.domain.JobBriefInfo;
import com.myzh.dpc.console.job.domain.JobServer;
import com.myzh.dpc.console.job.domain.JobSettings;
import com.myzh.dpc.console.job.service.JobDimensionService;
import com.myzh.dpc.console.job.service.JobOperationService;

@RestController
@RequestMapping("job")
public class JobController {
    
    @Resource
    private JobDimensionService jobDimensionService;
    
    @Resource
    private JobOperationService jobOperationService;
    
    @RequestMapping(value = "jobs", method = RequestMethod.GET)
    public Collection<JobBriefInfo> getAllJobsBriefInfo() {
        return jobDimensionService.getAllJobsBriefInfo();
    }
    
    @RequestMapping(value = "settings", method = RequestMethod.GET)
    public JobSettings getJobSettings(final JobSettings jobSettings, final ModelMap model) {
        model.put("jobName", jobSettings.getJobName());
        return jobDimensionService.getJobSettings(jobSettings.getJobName());
    }
    
    @RequestMapping(value = "settings", method = RequestMethod.POST)
    public void updateJobSettings(final JobSettings jobSettings) {
        jobDimensionService.updateJobSettings(jobSettings);
    }
    
    @RequestMapping(value = "servers", method = RequestMethod.GET)
    public Collection<JobServer> getServers(final JobServer jobServer) {
        return jobDimensionService.getServers(jobServer.getJobName());
    }
    
    @RequestMapping(value = "execution", method = RequestMethod.GET)
    public Collection<ExecutionInfo> getExecutionInfo(final JobSettings config) {
        return jobDimensionService.getExecutionInfo(config.getJobName());
    }
}
