
package com.myzh.dpc.console.job.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Strings;

@Getter
@Setter
public final class JobServer implements Serializable {
    
    private static final long serialVersionUID = 4241736510750597679L;
    
    private String jobName;
    
    private String ip;
    
    private String hostName;
    
    private ServerStatus status;
    
    private int processSuccessCount;
    
    private int processFailureCount;
    
    private String sharding;
    
    private boolean leader;
    
    private boolean leaderStoped;
    
    public enum ServerStatus {
        READY, 
        RUNNING, 
        DISABLED, 
        STOPED, 
        CRASHED;
        
        public static ServerStatus getServerStatus(final String status, final boolean disabled, final boolean stoped) {
            if (Strings.isNullOrEmpty(status)) {
                return ServerStatus.CRASHED;
            }
            if (disabled) {
                return ServerStatus.DISABLED;
            }
            if (stoped) {
                return ServerStatus.STOPED;
            }
            return ServerStatus.valueOf(status);
        }
    }
}
