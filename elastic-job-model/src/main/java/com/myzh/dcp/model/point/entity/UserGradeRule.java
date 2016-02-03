package com.myzh.dcp.model.point.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class UserGradeRule implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String desc;
	private String grade;
	private Integer min;
	private Integer max;
	private Integer state;
	private Date createTime;
}
