
package com.myzh.dpc.console.job.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myzh.dpc.console.job.repository.zookeeper.CuratorRepository;
import com.myzh.dpc.console.job.service.JobOperationService;
import com.myzh.dpc.console.job.util.JobNodePath;

@Service
public class JobOperationServiceImpl implements JobOperationService {
    
    @Resource
    private CuratorRepository curatorRepository;
    
    @Override
    public void stopJob(final String jobName, final String serverIp) {
        curatorRepository.create(JobNodePath.getServerNodePath(jobName, serverIp, "stoped"));
    }
    
    @Override
    public void resumeJob(final String jobName, final String serverIp) {
        curatorRepository.delete(JobNodePath.getServerNodePath(jobName, serverIp, "stoped"));
    }
    
    @Override
    public void stopAllJobsByJobName(final String jobName) {
        for (String each : curatorRepository.getChildren(JobNodePath.getServerNodePath(jobName))) {
            curatorRepository.create(JobNodePath.getServerNodePath(jobName, each, "stoped"));
        }
    }
    
    @Override
    public void resumeAllJobsByJobName(final String jobName) {
        for (String each : curatorRepository.getChildren(JobNodePath.getServerNodePath(jobName))) {
            curatorRepository.delete(JobNodePath.getServerNodePath(jobName, each, "stoped"));
        }
    }
    
    @Override
    public void stopAllJobsByServer(final String serverIp) {
        for (String jobName : curatorRepository.getChildren("/")) {
            String leaderIp = curatorRepository.getData(JobNodePath.getLeaderNodePath(jobName, "election/host"));
            if (serverIp.equals(leaderIp)) {
                for (String toBeStopedIp : curatorRepository.getChildren(JobNodePath.getServerNodePath(jobName))) {
                    curatorRepository.create(JobNodePath.getServerNodePath(jobName, toBeStopedIp, "stoped"));
                }
            } else {
                curatorRepository.create(JobNodePath.getServerNodePath(jobName, serverIp, "stoped"));
            }
        }
    }
    
    @Override
    public void resumeAllJobsByServer(final String serverIp) {
        for (String jobName : curatorRepository.getChildren("/")) {
            String leaderIp = curatorRepository.getData(JobNodePath.getLeaderNodePath(jobName, "election/host"));
            if (!serverIp.equals(leaderIp) && curatorRepository.checkExists(JobNodePath.getServerNodePath(jobName, leaderIp, "stoped"))) {
                continue;
            }
            curatorRepository.delete(JobNodePath.getServerNodePath(jobName, serverIp, "stoped"));
        }
    }
}
