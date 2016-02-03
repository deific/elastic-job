package com.myzh.dpc.console.point.model.form.point;

import com.myzh.dpc.console.point.model.form.BaseForm;

import lombok.Data;

@Data
public class ListPointRuleFrom extends BaseForm {
	private String name;
	private String desc;
	private String behaviorCode;
	private String areaCode;
	private String agentCode;
	private String clinicCode;
	private String userClass;
	private String userType;
}
