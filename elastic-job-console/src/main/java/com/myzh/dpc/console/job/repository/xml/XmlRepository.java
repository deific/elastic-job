
package com.myzh.dpc.console.job.repository.xml;

public interface XmlRepository<E> {
    
    E load();
    
    void save(E entity);
}
