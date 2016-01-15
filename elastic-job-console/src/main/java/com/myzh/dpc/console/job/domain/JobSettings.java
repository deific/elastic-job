
package com.myzh.dpc.console.job.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class JobSettings implements Serializable {
    
    private static final long serialVersionUID = -6532210090618686688L;
    
    private String jobName;
    
    private String jobClass;
    
    private int shardingTotalCount;
    
    private String cron;
    
    private String shardingItemParameters;
    
    private String jobParameter;
    
    private boolean monitorExecution;
    
    private int processCountIntervalSeconds;
    
    private int concurrentDataProcessThreadCount;
    
    private int fetchDataCount;
    
    private int maxTimeDiffSeconds;
    
    private boolean failover;
    
    private boolean misfire;
    
    private String jobShardingStrategyClass;
    
    private String description;
}
