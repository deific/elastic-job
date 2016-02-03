package com.myzh.dpc.console.point.api;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myzh.dcp.model.common.enums.Dimension;
import com.myzh.dcp.model.point.entity.UserPoint;
import com.myzh.dcp.model.point.entity.UserPointDetail;
import com.myzh.dcp.model.point.enums.PointType;
import com.myzh.dpc.console.core.controller.support.ResponseWrapper;
import com.myzh.dpc.console.core.exception.ExceptionCode;
import com.myzh.dpc.console.core.exception.ExceptionFactory;
import com.myzh.dpc.console.core.mybatis.pagination.Page;
import com.myzh.dpc.console.core.mybatis.pagination.Pageable;
import com.myzh.dpc.console.point.model.form.BaseForm;
import com.myzh.dpc.console.point.model.form.point.GetUserPointDetailsForm;
import com.myzh.dpc.console.point.model.form.point.PointForm;
import com.myzh.dpc.console.point.service.PointService;

@Controller
@RequestMapping("/api/v1/point")
public class PointApiController {

	@Resource
	private PointService pointService;
	@Resource
	private ExceptionFactory exceptionFactory;
	/**
	 * 根据用户id或诊所编码查询积分
	 * @param userId
	 * @param clinicCode
	 * @return
	 */
	@RequestMapping(value = "/getUserPoint", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
	public ResponseEntity<?> getUserPoint(@Valid BaseForm form) {
		ResponseWrapper rw = new ResponseWrapper();
		UserPoint userPoint = pointService.getUserPoint(form.getUserId(), form.getClinicCode());
		if (userPoint == null) {
			throw exceptionFactory.create(ExceptionCode.User_Point_Not_Exist);
		}
		
		rw.setData(userPoint);
		return new ResponseEntity<ResponseWrapper>(rw, HttpStatus.OK);
	}
	
	/**
	 * 消费积分
	 * @param userId
	 * @param clinicCode
	 * @param point
	 * @return
	 */
	@RequestMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
	public ResponseEntity<?> createPoint(@Valid PointForm form) {
		ResponseWrapper rw = new ResponseWrapper();
		pointService.createPoint(form.getUserId(), form.getClinicCode(), form.getChannel(), form.getPoint());
		return new ResponseEntity<ResponseWrapper>(rw, HttpStatus.OK);
	}
	
	/**
	 * 消费积分
	 * @param userId
	 * @param clinicCode
	 * @param point
	 * @return
	 */
	@RequestMapping(value = "/consume", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
	public ResponseEntity<?> consumePoint(@Valid PointForm form) {
		ResponseWrapper rw = new ResponseWrapper();
		// 校验
		if(StringUtils.isBlank(form.getUserId()) && StringUtils.isBlank(form.getClinicCode())) {
			throw exceptionFactory.create(ExceptionCode.UserId_Or_ClinicCode_Required);
		}
		// 错误的渠道
		if (!Dimension.isExist(form.getChannel())) {
			throw exceptionFactory.create(ExceptionCode.Common_Channel_Not_Exist);
		}
		// 扣除积分
		UserPoint userPoint = pointService.consumePoint(form.getUserId(), form.getClinicCode(), form.getChannel(), form.getPoint());
		rw.setData(userPoint);
		return new ResponseEntity<ResponseWrapper>(rw, HttpStatus.OK);
	}

	/**
	 * 根据用户id或诊所编码查询积分明细
	 * @param userId
	 * @param clinicCode
	 * @return
	 */
	@RequestMapping(value = "/getAllUserPointDetails", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
	public ResponseEntity<?> getUserPointDetails(@Valid GetUserPointDetailsForm form) {
		ResponseWrapper rw = new ResponseWrapper();
		// 错误的类型
		if (StringUtils.isNotBlank(form.getPointType()) && !PointType.isExist(form.getPointType())) {
			throw exceptionFactory.create(ExceptionCode.User_Point_Type_Not_Exist);
		}
		List<UserPointDetail> userDetails = pointService.getUserPointDetails(form.getUserId(), form.getClinicCode(), form.getPointType(), form.getStartDate(), form.getEndDate());
		rw.setData(userDetails);
		return new ResponseEntity<ResponseWrapper>(rw, HttpStatus.OK);
	}
	
	/**
	 * 根据用户id或诊所编码查询积分明细
	 * @param userId
	 * @param clinicCode
	 * @return
	 */
	@RequestMapping(value = "/getUserPointDetailsPage", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
	public ResponseEntity<?> getUserPointDetails(@Valid GetUserPointDetailsForm form, @PageableDefault Pageable pageable) {
		ResponseWrapper rw = new ResponseWrapper();
		// 错误的类型
		if (StringUtils.isNotBlank(form.getPointType()) && !PointType.isExist(form.getPointType())) {
			throw exceptionFactory.create(ExceptionCode.User_Point_Type_Not_Exist);
		}
		Page<UserPointDetail> userDetails = pointService.getUserPointDetails(form.getUserId(), form.getClinicCode(), form.getPointType(), form.getStartDate(), form.getEndDate(), pageable);
		rw.setData(userDetails);
		return new ResponseEntity<ResponseWrapper>(rw, HttpStatus.OK);
	}
	
}
