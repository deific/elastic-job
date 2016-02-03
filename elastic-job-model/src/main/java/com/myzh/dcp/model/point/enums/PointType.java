package com.myzh.dcp.model.point.enums;

public enum PointType {
	produce("0","产生"),consume("1", "消费");
	
	private String code;
	private String desc;
	
	public String getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
	
	public static boolean isExist(String code) {
		PointType[] pointTypes = PointType.values();
		for (PointType pointType:pointTypes) {
			if (pointType.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}
	PointType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}
