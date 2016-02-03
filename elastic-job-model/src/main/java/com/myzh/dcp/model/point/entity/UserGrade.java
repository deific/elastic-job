package com.myzh.dcp.model.point.entity;

import java.util.Date;

import lombok.Data;

/**
 * 用户等级
 * @author chensg
 *
 */
@Data
public class UserGrade {
	private Integer id;
	private String userId;
	private String areaCode;
	private String agentCode;
	private String clinicCode;
	private String userType;
	private String grade;
	private Date createTime;
	private Date updateTime;
}
