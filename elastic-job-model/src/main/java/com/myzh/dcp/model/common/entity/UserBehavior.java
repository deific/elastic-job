package com.myzh.dcp.model.common.entity;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class UserBehavior {
	private String id;
	private String source;
	private String areaCode;
	private String agentCode;
	private String clinicCode;
	private String chanelCode;
	private String userClass;
	private String userType;
	private String userId;
	private String behaviorType;
	private String behaviorClass;
	private String behaviorDimension;
	private String behaviorCode;
	private String behaviorValue;
	private Map<String, Object> behaviorData = new HashMap<String, Object>();
	private String behaviorTime;
	private String collectTime;
	
	
	public void addBehaviorData(String key, Object value) {
		behaviorData.put(key, value);
	}
}
