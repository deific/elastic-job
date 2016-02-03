package com.myzh.dcp.model.common.enums;

/**
 * 行为类型
 * @author chensg
 *
 */
public enum BehaviorType {

	operation("01", "操作"), status("02", "状态"), amount("03", "量级"), daycount("04","天/次");
	
	String code, desc;
	
	BehaviorType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getDesc() {
		return desc;
	}
	
	
	public static BehaviorType getBehaviorType(String code) {
		BehaviorType[] types = values();
		for (BehaviorType type: types) {
			if (type.getCode().equals(code)) {
				return type;
			}
		}
		return null;
	}
}
