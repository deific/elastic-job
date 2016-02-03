package com.myzh.dcp.model.point.enums;

public enum PointRuleState {
	enable("1"),disable("0");
	private String code;
	PointRuleState(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
