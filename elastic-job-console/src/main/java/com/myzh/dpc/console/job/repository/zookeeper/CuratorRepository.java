
package com.myzh.dpc.console.job.repository.zookeeper;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;

public interface CuratorRepository {
    
    CuratorFramework connect(String connectString, String namespace, String digest);
    
    boolean checkExists(String znode);
    
    String getData(String znode);
    
    List<String> getChildren(String znode);
    
    void create(String znode);
    
    void update(String znode, Object value);
    
    void delete(String znode);
}
