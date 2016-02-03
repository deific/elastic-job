package com.myzh.dpc.console.point.model.form.point;

import com.myzh.dpc.console.point.model.form.BaseForm;

import lombok.Data;

@Data
public class GetUserPointDetailsForm extends BaseForm {
	private String pointType;
	private String startDate;
	private String endDate;
}
