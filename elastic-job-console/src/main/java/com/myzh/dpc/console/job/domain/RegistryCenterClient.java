
package com.myzh.dpc.console.job.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.apache.curator.framework.CuratorFramework;

@Getter
@Setter
@NoArgsConstructor
public final class RegistryCenterClient implements Serializable {
    
    private static final long serialVersionUID = -946258964014121184L;
    
    private String name;
    
    private CuratorFramework curatorClient;
    
    private boolean connected;
    
    public RegistryCenterClient(final String name) {
        this.name = name;
    }
}
