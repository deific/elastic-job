
package com.myzh.dpc.console.job.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class ServerBriefInfo implements Serializable, Comparable<ServerBriefInfo> {
    
    private static final long serialVersionUID = 1133149706443681483L;
    
    private String serverIp;
    
    private String serverHostName;
    
    private ServerBriefStatus status;
    
    @Override
    public int compareTo(final ServerBriefInfo o) {
        return getServerIp().compareTo(o.getServerIp());
    }
    
    public enum ServerBriefStatus {
        OK, 
        PARTIAL_ALIVE, 
        ALL_CRASHED
    }
}
