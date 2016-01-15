package com.myzh.dpc.console.job.repository.xml.impl;


import java.io.File;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.myzh.dpc.console.job.exception.JobConsoleException;
import com.myzh.dpc.console.job.repository.xml.XmlRepository;
import com.myzh.dpc.console.job.util.HomeFolder;

public abstract class AbstractXmlRepositoryImpl<E> implements XmlRepository<E> {
    
    private final File file;
    
    private final Class<E> clazz;
    
    private JAXBContext jaxbContext;
    
    protected AbstractXmlRepositoryImpl(final String fileName, final Class<E> clazz) {
        file = new File(HomeFolder.getFilePathInHomeFolder(fileName));
        this.clazz = clazz;
    }
    
    @PostConstruct
    private void init() {
        HomeFolder.createHomeFolderIfNotExisted();
        try {
            jaxbContext = JAXBContext.newInstance(clazz);
        } catch (final JAXBException ex) {
            throw new JobConsoleException(ex);
        }
    }
    
    @Override
    public synchronized E load() {
        if (!file.exists()) {
            try {
                return clazz.newInstance();
            } catch (final InstantiationException | IllegalAccessException ex) {
                throw new JobConsoleException(ex);
            }
        }
        try {
            @SuppressWarnings("unchecked")
            E result = (E) jaxbContext.createUnmarshaller().unmarshal(file);
            return result;
        } catch (final JAXBException ex) {
            throw new JobConsoleException(ex);
        }
    }
    
    @Override
    public synchronized void save(final E entity) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(entity, file);
        } catch (final JAXBException ex) {
            throw new JobConsoleException(ex);
        }
    }
}
