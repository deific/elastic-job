package com.myzh.dpc.console.point.model.form.point;

import com.myzh.dpc.console.point.model.form.BaseForm;

import lombok.Data;

@Data
public class SavePointForm extends BaseForm {
	private String pointType;
	private String pointDimension;
	private Integer point;
	private String desc;
}
