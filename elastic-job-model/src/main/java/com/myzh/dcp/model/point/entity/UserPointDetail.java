package com.myzh.dcp.model.point.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * 用户积分明细
 * @author chensg
 *
 */
@Data
public class UserPointDetail {
	private Integer id;
	@JsonIgnore
	private String behaviorId;
	@JsonIgnore
	private String behaviorCode;
	@JsonIgnore
	private String ruleId;
	private String userId;
	private String clinicCode;
	private String pointType;
	private String pointDimension;
	@JsonIgnore
	private String triggerValue;
	private Integer point;
	private String desc;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date pointTime;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
}
