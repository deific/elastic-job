package com.myzh.dcp.model.point.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * 用户积分
 * @author chensg
 *
 */
@Data
public class UserPoint {
	@JsonIgnore
	private Integer id;
	@JsonIgnore
	private String areaCode;
	@JsonIgnore
	private String agentCode;
	private String clinicCode;
	private String userType;
	private String userId;
	private Integer allPoint = 0;
	private String year;
	private String grade;
	@JsonIgnore
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
}
