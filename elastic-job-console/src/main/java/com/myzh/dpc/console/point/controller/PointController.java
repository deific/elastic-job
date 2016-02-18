package com.myzh.dpc.console.point.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import com.myzh.dpc.console.point.model.form.point.SavePointForm;
import com.myzh.dpc.console.point.model.form.point.StatisticsForm;
import com.myzh.dpc.console.point.service.PointService;

@Controller
@RequestMapping("/point")
public class PointController {

	@Resource
	private PointService pointService;
	@Resource
	private ExceptionFactory exceptionFactory;

	/**
	 * 查询所有用户的积分列表
	 * @param userId
	 * @param clinicCode
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public String pointManager(BaseForm queryFrom, @PageableDefault Pageable pageable, final ModelMap model) {
		return "point/pointManager";
	}
	
	/**
	 * 新建积分
	 * @return
	 */
	@RequestMapping(value="add")
	public String addPoint(final ModelMap model) {
		return "point/pointEdit";
	}
	
	/**
	 * 新建积分
	 * @return
	 */
	@RequestMapping(value="statistics")
	public String statistics(final ModelMap model) {
		return "point/pointStatistics";
	}
	
	/**
	 * 新建积分
	 * @return
	 */
	@RequestMapping(value="doStatistics")
	@ResponseBody
	public ResponseEntity<?> doStatistics(StatisticsForm statisticsForm, final ModelMap model) {
		ResponseWrapper rw = new ResponseWrapper();
		Map<String, Object> statistics = pointService.getPointStatistics(statisticsForm.getAreaCode(), statisticsForm.getClinicCode(), statisticsForm.getDays());
		rw.setData(statistics);
		return new ResponseEntity<ResponseWrapper>(rw, HttpStatus.OK);
	}
	
	/**
	 * 查询所有用户的积分列表
	 * @param userId
	 * @param clinicCode
	 * @return
	 */
	@RequestMapping(value="list")
	@ResponseBody
	public ResponseEntity<?> pointList(BaseForm queryFrom, @PageableDefault Pageable pageable, final ModelMap model) {
		ResponseWrapper rw = new ResponseWrapper();
		Page<UserPoint> userPointPage = pointService.queryUserPointPgae(queryFrom.getUserId(), queryFrom.getClinicCode(), pageable);
		rw.add("rows", userPointPage);
		rw.add("total", userPointPage.getTotal());
		return new ResponseEntity<ResponseWrapper>(rw, HttpStatus.OK);
	}
	
	
	/**
	 * 消费积分
	 * @param userId
	 * @param clinicCode
	 * @param point
	 * @return
	 */
	@RequestMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
	public ResponseEntity<?> savePoint(@Valid SavePointForm form) {
		ResponseWrapper rw = new ResponseWrapper();
		
		UserPointDetail userPointDetail = new UserPointDetail();
		BeanUtils.copyProperties(form, userPointDetail);
		pointService.savePoint(userPointDetail);
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
	@RequestMapping(value = "/detail/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
	public ResponseEntity<?> getUserPointDetails(@Valid GetUserPointDetailsForm form, @PageableDefault Pageable pageable) {
		ResponseWrapper rw = new ResponseWrapper();
		// 错误的类型
		if (StringUtils.isNotBlank(form.getPointType()) && !PointType.isExist(form.getPointType())) {
			throw exceptionFactory.create(ExceptionCode.User_Point_Type_Not_Exist);
		}
		Page<UserPointDetail> userDetails = pointService.getUserPointDetails(form.getUserId(), form.getClinicCode(), form.getPointType(), form.getStartDate(), form.getEndDate(), pageable);
		rw.add("rows", userDetails);
		rw.add("total", userDetails.getTotal());
		return new ResponseEntity<ResponseWrapper>(rw, HttpStatus.OK);
	}
}
