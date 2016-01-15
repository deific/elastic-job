
package com.myzh.dpc.console.job.service;

import java.util.Collection;

import com.myzh.dpc.console.job.domain.RegistryCenterClient;
import com.myzh.dpc.console.job.domain.RegistryCenterConfiguration;

public interface RegistryCenterService {
    
    Collection<RegistryCenterConfiguration> loadAll();
    
    boolean add(RegistryCenterConfiguration config);
    
    void delete(String name);
    
    RegistryCenterClient connect(String name);
    
    RegistryCenterClient connectActivated();
}
