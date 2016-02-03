package com.myzh.dcp.model.common.enums;

public enum ChanelCode {
	PC("01","PC端"), 
	WEB("02","网页"),
	WX("03","微信"),
	APP("04","移动端");
	
	String code;
	String desc;
	ChanelCode(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public String getDesc() {
		return this.desc;
	}
}
