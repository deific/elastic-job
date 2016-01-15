
package com.myzh.dpc.console.job.controller;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.myzh.dpc.console.job.domain.JobServer;
import com.myzh.dpc.console.job.domain.ServerBriefInfo;
import com.myzh.dpc.console.job.service.JobOperationService;
import com.myzh.dpc.console.job.service.ServerDimensionService;

@RestController
@RequestMapping("server")
public class ServerController {
    
    @Resource
    private ServerDimensionService serverDimensionService;
    
    @Resource
    private JobOperationService jobOperationService;
    
    @RequestMapping(value = "servers", method = RequestMethod.GET)
    public Collection<ServerBriefInfo> getAllServersBriefInfo() {
        return serverDimensionService.getAllServersBriefInfo();
    }
    
    @RequestMapping(value = "jobs", method = RequestMethod.GET)
    public Collection<JobServer> getJobs(final JobServer jobServer, final ModelMap model) {
        model.put("serverIp", jobServer.getIp());
        return serverDimensionService.getJobs(jobServer.getIp());
    }
}
