
package com.myzh.dpc.console.job.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myzh.dpc.console.job.domain.ExecutionInfo;
import com.myzh.dpc.console.job.domain.ExecutionInfo.ExecutionStatus;
import com.myzh.dpc.console.job.domain.JobBriefInfo;
import com.myzh.dpc.console.job.domain.JobBriefInfo.JobStatus;
import com.myzh.dpc.console.job.domain.JobServer;
import com.myzh.dpc.console.job.domain.JobServer.ServerStatus;
import com.myzh.dpc.console.job.domain.JobSettings;
import com.myzh.dpc.console.job.repository.zookeeper.CuratorRepository;
import com.myzh.dpc.console.job.service.JobDimensionService;
import com.myzh.dpc.console.job.util.JobNodePath;

@Service
public class JobDimensionServiceImpl implements JobDimensionService {
    
    @Resource
    private CuratorRepository curatorRepository;
    
    @Override
    public Collection<JobBriefInfo> getAllJobsBriefInfo() {
        List<String> jobNames = curatorRepository.getChildren("/");
        List<JobBriefInfo> result = new ArrayList<>(jobNames.size());
        for (String each : jobNames) {
            JobBriefInfo jobBriefInfo = new JobBriefInfo();
            jobBriefInfo.setJobName(each);
            jobBriefInfo.setDescription(curatorRepository.getData(JobNodePath.getConfigNodePath(each, "description")));
            jobBriefInfo.setStatus(getJobStatus(each));
            jobBriefInfo.setCron(curatorRepository.getData(JobNodePath.getConfigNodePath(each, "cron")));
            result.add(jobBriefInfo);
        }
        Collections.sort(result);
        return result;
    }
    
    private JobStatus getJobStatus(final String jobName) {
        List<String> servers = curatorRepository.getChildren(JobNodePath.getServerNodePath(jobName));
        int okCount = 0;
        int crashedCount = 0;
        int manuallyDisabledCount = 0;
        for (String each : servers) {
            switch (getServerStatus(jobName, each)) {
                case READY:
                case RUNNING:
                    okCount++;
                    break;
                case DISABLED:
                case STOPED:
                    manuallyDisabledCount++;
                    break;
                case CRASHED:
                    crashedCount++;
                    break;
                default:
                    break;
            }
        }
        return JobStatus.getJobStatus(okCount, crashedCount, manuallyDisabledCount, servers.size());
    }
    
    @Override
    public JobSettings getJobSettings(final String jobName) {
        JobSettings result = new JobSettings();
        result.setJobName(jobName);
        result.setJobClass(curatorRepository.getData(JobNodePath.getConfigNodePath(jobName, "jobClass")));
        result.setShardingTotalCount(Integer.parseInt(curatorRepository.getData(JobNodePath.getConfigNodePath(jobName, "shardingTotalCount"))));
        result.setCron(curatorRepository.getData(JobNodePath.getConfigNodePath(jobName, "cron")));
        result.setShardingItemParameters(curatorRepository.getData(JobNodePath.getConfigNodePath(jobName, "shardingItemParameters")));
        result.setJobParameter(curatorRepository.getData(JobNodePath.getConfigNodePath(jobName, "jobParameter")));
        result.setMonitorExecution(Boolean.valueOf(curatorRepository.getData(JobNodePath.getConfigNodePath(jobName, "monitorExecution"))));
        result.setProcessCountIntervalSeconds(Integer.parseInt(curatorRepository.getData(JobNodePath.getConfigNodePath(jobName, "processCountIntervalSeconds"))));
        result.setConcurrentDataProcessThreadCount(Integer.parseInt(curatorRepository.getData(JobNodePath.getConfigNodePath(jobName, "concurrentDataProcessThreadCount"))));
        result.setFetchDataCount(Integer.parseInt(curatorRepository.getData(JobNodePath.getConfigNodePath(jobName, "fetchDataCount"))));
        result.setMaxTimeDiffSeconds(Integer.parseInt(curatorRepository.getData(JobNodePath.getConfigNodePath(jobName, "maxTimeDiffSeconds"))));
        result.setFailover(Boolean.valueOf(curatorRepository.getData(JobNodePath.getConfigNodePath(jobName, "failover"))));
        result.setMisfire(Boolean.valueOf(curatorRepository.getData(JobNodePath.getConfigNodePath(jobName, "misfire"))));
        result.setJobShardingStrategyClass(curatorRepository.getData(JobNodePath.getConfigNodePath(jobName, "jobShardingStrategyClass")));
        result.setDescription(curatorRepository.getData(JobNodePath.getConfigNodePath(jobName, "description")));
        return result;
    }
    
    @Override
    public void updateJobSettings(final JobSettings jobSettings) {
        updateIfchanged(jobSettings.getJobName(), "shardingTotalCount", jobSettings.getShardingTotalCount());
        updateIfchanged(jobSettings.getJobName(), "cron", jobSettings.getCron());
        updateIfchanged(jobSettings.getJobName(), "shardingItemParameters", jobSettings.getShardingItemParameters());
        updateIfchanged(jobSettings.getJobName(), "jobParameter", jobSettings.getJobParameter());
        updateIfchanged(jobSettings.getJobName(), "monitorExecution", jobSettings.isMonitorExecution());
        updateIfchanged(jobSettings.getJobName(), "processCountIntervalSeconds", jobSettings.getProcessCountIntervalSeconds());
        updateIfchanged(jobSettings.getJobName(), "concurrentDataProcessThreadCount", jobSettings.getConcurrentDataProcessThreadCount());
        updateIfchanged(jobSettings.getJobName(), "fetchDataCount", jobSettings.getFetchDataCount());
        updateIfchanged(jobSettings.getJobName(), "maxTimeDiffSeconds", jobSettings.getMaxTimeDiffSeconds());
        updateIfchanged(jobSettings.getJobName(), "failover", jobSettings.isFailover());
        updateIfchanged(jobSettings.getJobName(), "misfire", jobSettings.isMisfire());
        updateIfchanged(jobSettings.getJobName(), "jobShardingStrategyClass", jobSettings.getJobShardingStrategyClass());
        updateIfchanged(jobSettings.getJobName(), "description", jobSettings.getDescription());
    }
    
    private void updateIfchanged(final String jobName, final String nodeName, final Object value) {
        String configNodePath = JobNodePath.getConfigNodePath(jobName, nodeName);
        String originalValue = curatorRepository.getData(configNodePath);
        if (null != originalValue && !originalValue.equals(value.toString())) {
            curatorRepository.update(configNodePath, value);
        }
    }
    
    @Override
    public Collection<JobServer> getServers(final String jobName) {
        List<String> serverIps = curatorRepository.getChildren(JobNodePath.getServerNodePath(jobName));
        String leaderIp = curatorRepository.getData(JobNodePath.getLeaderNodePath(jobName, "election/host"));
        Collection<JobServer> result = new ArrayList<>(serverIps.size());
        for (String each : serverIps) {
            result.add(getJobServer(jobName, leaderIp, each));
        }
        return result;
    }
    
    private JobServer getJobServer(final String jobName, final String leaderIp, final String serverIp) {
        JobServer result = new JobServer();
        result.setIp(serverIp);
        result.setHostName(curatorRepository.getData(JobNodePath.getServerNodePath(jobName, serverIp, "hostName")));
        String processSuccessCount = curatorRepository.getData(JobNodePath.getServerNodePath(jobName, serverIp, "processSuccessCount"));
        result.setProcessSuccessCount(null == processSuccessCount ? 0 : Integer.parseInt(processSuccessCount));
        String processFailureCount = curatorRepository.getData(JobNodePath.getServerNodePath(jobName, serverIp, "processFailureCount"));
        result.setProcessFailureCount(null == processFailureCount ? 0 : Integer.parseInt(processFailureCount));
        result.setSharding(curatorRepository.getData(JobNodePath.getServerNodePath(jobName, serverIp, "sharding")));
        result.setStatus(getServerStatus(jobName, serverIp));
        result.setLeader(serverIp.equals(leaderIp));
        return result;
    }
    
    private ServerStatus getServerStatus(final String jobName, final String serverIp) {
        String status = curatorRepository.getData(JobNodePath.getServerNodePath(jobName, serverIp, "status"));
        boolean disabled = curatorRepository.checkExists(JobNodePath.getServerNodePath(jobName, serverIp, "disabled"));
        boolean stoped = curatorRepository.checkExists(JobNodePath.getServerNodePath(jobName, serverIp, "stoped"));
        return ServerStatus.getServerStatus(status, disabled, stoped);
    }
    
    @Override
    public Collection<ExecutionInfo> getExecutionInfo(final String jobName) {
        String executionRootpath = JobNodePath.getExecutionNodePath(jobName);
        if (!curatorRepository.checkExists(executionRootpath)) {
            return Collections.emptyList();
        }
        List<String> items = curatorRepository.getChildren(executionRootpath);
        List<ExecutionInfo> result = new ArrayList<>(items.size());
        for (String each : items) {
            result.add(getExecutionInfo(jobName, each));
        }
        Collections.sort(result);
        return result;
    }
    
    private ExecutionInfo getExecutionInfo(final String jobName, final String item) {
        ExecutionInfo result = new ExecutionInfo();
        result.setItem(Integer.parseInt(item));
        boolean running = curatorRepository.checkExists(JobNodePath.getExecutionNodePath(jobName, item, "running"));
        boolean completed = curatorRepository.checkExists(JobNodePath.getExecutionNodePath(jobName, item, "completed"));
        result.setStatus(ExecutionStatus.getExecutionStatus(running, completed));
        if (curatorRepository.checkExists(JobNodePath.getExecutionNodePath(jobName, item, "failover"))) {
            result.setFailoverIp(curatorRepository.getData(JobNodePath.getExecutionNodePath(jobName, item, "failover")));
        }
        String lastBeginTime = curatorRepository.getData(JobNodePath.getExecutionNodePath(jobName, item, "lastBeginTime"));
        result.setLastBeginTime(null == lastBeginTime ? null : new Date(Long.parseLong(lastBeginTime)));
        String nextFireTime = curatorRepository.getData(JobNodePath.getExecutionNodePath(jobName, item, "nextFireTime"));
        result.setNextFireTime(null == nextFireTime ? null : new Date(Long.parseLong(nextFireTime)));
        String lastCompleteTime = curatorRepository.getData(JobNodePath.getExecutionNodePath(jobName, item, "lastCompleteTime"));
        result.setLastCompleteTime(null == lastCompleteTime ? null : new Date(Long.parseLong(lastCompleteTime)));
        return result;
    }
}
