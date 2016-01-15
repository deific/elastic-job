
package com.myzh.dpc.console.job.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"zkAddressList", "namespace", "digest", "activated"})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
public class RegistryCenterConfiguration implements Serializable {
    
    private static final long serialVersionUID = -5996257770767863699L;
    
    @XmlAttribute(required = true)
    private String name;
    
    @XmlAttribute(required = true)
    private String zkAddressList;
    
    @XmlAttribute
    private String namespace;
    
    @XmlAttribute
    private String digest;
    
    @XmlAttribute
    private boolean activated;
    
    public RegistryCenterConfiguration(final String name) {
        this.name = name;
    }
}
