
package com.myzh.dpc.console.job.service.impl;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.stereotype.Service;

import com.myzh.dpc.console.job.domain.RegistryCenterClient;
import com.myzh.dpc.console.job.domain.RegistryCenterConfiguration;
import com.myzh.dpc.console.job.domain.RegistryCenterConfigurations;
import com.myzh.dpc.console.job.repository.xml.RegistryCenterConfigurationsXmlRepository;
import com.myzh.dpc.console.job.repository.zookeeper.CuratorRepository;
import com.myzh.dpc.console.job.service.RegistryCenterService;

@Service
public class RegistryCenterServiceImpl implements RegistryCenterService {
    
    @Resource
    private CuratorRepository curatorRepository;
    
    @Resource
    private RegistryCenterConfigurationsXmlRepository registryCenterConfigurationsXmlRepository;
    
    private ConcurrentHashMap<String, RegistryCenterClient> clientMap = new ConcurrentHashMap<>();
    
    @Override
    public Collection<RegistryCenterConfiguration> loadAll() {
        return registryCenterConfigurationsXmlRepository.load().getRegistryCenterConfiguration();
    }
    
    @Override
    public boolean add(final RegistryCenterConfiguration config) {
        RegistryCenterConfigurations configs = registryCenterConfigurationsXmlRepository.load();
        boolean result = configs.getRegistryCenterConfiguration().add(config);
        if (result) {
            registryCenterConfigurationsXmlRepository.save(configs);
        }
        return result;
    }
    
    @Override
    public void delete(final String name) {
        RegistryCenterConfigurations configs = registryCenterConfigurationsXmlRepository.load();
        if (configs.getRegistryCenterConfiguration().remove(new RegistryCenterConfiguration(name))) {
            registryCenterConfigurationsXmlRepository.save(configs);
        }
    }
    
    @Override
    public RegistryCenterClient connect(final String name) {
        RegistryCenterClient result = new RegistryCenterClient(name);
        RegistryCenterConfigurations configs = registryCenterConfigurationsXmlRepository.load();
        RegistryCenterConfiguration toBeConnectedConfig = findRegistryCenterConfiguration(name, configs);
        if (null == toBeConnectedConfig) {
            return result;
        }
        RegistryCenterClient clientInCache = findInCache(name);
        if (null != clientInCache) {
            setActivated(configs, toBeConnectedConfig);
            return clientInCache;
        }
        CuratorFramework client = curatorRepository.connect(toBeConnectedConfig.getZkAddressList(), toBeConnectedConfig.getNamespace(), toBeConnectedConfig.getDigest());
        if (null == client) {
            return result;
        }
        setRegistryCenterClient(result, name, client);
        setActivated(configs, toBeConnectedConfig);
        return result;
    }
    
    private RegistryCenterClient findInCache(final String name) {
        if (clientMap.containsKey(name)) {
            if (clientMap.get(name).isConnected()) {
                return clientMap.get(name);
            }
            clientMap.remove(name);
        }
        return null;
    }
    
    private RegistryCenterConfiguration findRegistryCenterConfiguration(final String name, final RegistryCenterConfigurations configs) {
        for (RegistryCenterConfiguration each : configs.getRegistryCenterConfiguration()) {
            if (name.equals(each.getName())) {
                return each;
            }
        }
        return null;
    }
    
    private RegistryCenterConfiguration findActivatedRegistryCenterConfiguration(final RegistryCenterConfigurations configs) {
        for (RegistryCenterConfiguration each : configs.getRegistryCenterConfiguration()) {
            if (each.isActivated()) {
                return each;
            }
        }
        return null;
    }
    
    private void setRegistryCenterClient(final RegistryCenterClient registryCenterClient, final String name, final CuratorFramework client) {
        registryCenterClient.setConnected(true);
        registryCenterClient.setCuratorClient(client);
        clientMap.putIfAbsent(name, registryCenterClient);
    }
    
    private void setActivated(final RegistryCenterConfigurations configs, final RegistryCenterConfiguration toBeConnectedConfig) {
        RegistryCenterConfiguration activatedConfig = findActivatedRegistryCenterConfiguration(configs);
        if (!toBeConnectedConfig.equals(activatedConfig)) {
            if (null != activatedConfig) {
                activatedConfig.setActivated(false);
            }
            toBeConnectedConfig.setActivated(true);
            registryCenterConfigurationsXmlRepository.save(configs);
        }
    }
    
    @Override
    public RegistryCenterClient connectActivated() {
        RegistryCenterClient result = new RegistryCenterClient();
        RegistryCenterConfiguration activatedConfig = findActivatedRegistryCenterConfiguration(registryCenterConfigurationsXmlRepository.load());
        if (null == activatedConfig) {
            return result;
        }
        result = connect(activatedConfig.getName());
        return result;
    }
}
