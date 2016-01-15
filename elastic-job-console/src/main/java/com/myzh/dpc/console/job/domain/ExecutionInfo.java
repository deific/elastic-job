
package com.myzh.dpc.console.job.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class ExecutionInfo implements Serializable, Comparable<ExecutionInfo> {
    
    private static final long serialVersionUID = 8587397581949456718L;
    
    private int item;
    
    private ExecutionStatus status;
    
    private String failoverIp;
    
    private Date lastBeginTime;
    
    private Date nextFireTime;
    
    private Date lastCompleteTime;
    
    @Override
    public int compareTo(final ExecutionInfo o) {
        return getItem() - o.getItem();
    }
    
    public enum ExecutionStatus {
        RUNNING, 
        COMPLETED, 
        PENDING;
        
        public static ExecutionStatus getExecutionStatus(final boolean running, final boolean completed) {
            if (running) {
                return ExecutionStatus.RUNNING;
            }
            if (completed) {
                return ExecutionStatus.COMPLETED;
            }
            return ExecutionStatus.PENDING;
        }
    }
}
