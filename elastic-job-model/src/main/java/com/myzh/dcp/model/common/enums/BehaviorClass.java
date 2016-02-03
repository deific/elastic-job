package com.myzh.dcp.model.common.enums;

/**
 * 行为分类
 * 0 产生
 * 1 消费
 * 3 其他
 * @author chensg
 *
 */
public enum BehaviorClass {
	product("0"),customer("1");
	
	String code;
	BehaviorClass(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
}
