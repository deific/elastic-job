package com.myzh.dpc.console.point.model.form.point;

import org.hibernate.validator.constraints.NotBlank;

import com.myzh.dpc.console.point.model.form.BaseForm;

import lombok.Data;

@Data
public class PointForm extends BaseForm {
	@NotBlank(message="common.channel.is.null")
	private String channel;
	private Integer point;
}
