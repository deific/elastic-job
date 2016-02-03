package com.myzh.dpc.console.point.model.form.point;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class PointRuleForm {
	private Integer id;
	private String name;
	private String desc;
	private String behaviorCode;
	private String areaCode;
	private String agentCode;
	private String clinicCode;
	/**
	 * 用户类型
	 * vip 普通
	 */
	private String userClass;
	/**
	 * 用户类别
	 * 超级管理员、主账户、子账户
	 */
	private String userType;
	
	// 规则优先级
	private Integer priority;
	/**
	 *  规则排他性 0 不排他 1 排他
	 */
	private Boolean exclusive;
	/**
	 * 规则计算周期
	 * hour 小时
	 * day 天
	 * month 月
	 */
	private String scope;
	// 规则类型
	private String ruleMode;
	// 规则表信息
	private String ruleMessage;
	// 规则表达式
	private String ruleExpression;
	// 规则值
	private String value;
	// 规则值
	private Double maxValue;
	// 激活时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date effectiveTime;
	// 过期时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date expireTime;
	// 状态
	private String state;
	private Date createTime;
	private String createUser;
	private Date updateTime;
	private String updateUser;
	

}
