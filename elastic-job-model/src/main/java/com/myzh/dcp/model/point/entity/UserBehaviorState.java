package com.myzh.dcp.model.point.entity;

import java.util.Date;

import lombok.Data;

/**
 * 用户行为状态数据
 * 某个时间段内的行为
 * 操作统计状态数据
 * 
 * @author chensg
 *
 */
@Data
public class UserBehaviorState {
	private Integer id;
	private String source;
	private String areaCode;
	private String agentCode;
	private String clinicCode;
	private String chanelCode;
	private String userType;
	private String userId;
	private String behaviorType;
	private String behaviorClass;
	private String behaviorDimension;
	private String behaviorCode;
	private Double stateValue = 0d;
	private String date;
	private Double count = 0d;
	private Date createTime;
	private Date updateTime;
}
