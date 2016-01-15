
package com.myzh.dpc.console.job.repository.xml.impl;

import org.springframework.stereotype.Repository;

import com.myzh.dpc.console.job.domain.RegistryCenterConfigurations;
import com.myzh.dpc.console.job.repository.xml.RegistryCenterConfigurationsXmlRepository;

@Repository
public class RegistryCenterConfigurationsXmlRepositoryImpl extends AbstractXmlRepositoryImpl<RegistryCenterConfigurations> implements RegistryCenterConfigurationsXmlRepository {
    
    public RegistryCenterConfigurationsXmlRepositoryImpl() {
        super("RegistryCenterConfigurations.xml", RegistryCenterConfigurations.class);
    }
}
