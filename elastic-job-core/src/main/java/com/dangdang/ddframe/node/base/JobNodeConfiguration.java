package com.dangdang.ddframe.node.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class JobNodeConfiguration {
	/**
     * 本地属性文件路径.
     */
    private String nodePropertiesPath;
    
}
