package com.myzh.dcp.model.sys.entity;

import lombok.Data;

@Data
public class Permission {
	private Integer id;
	private String permission;
	private Integer roleId;
}
