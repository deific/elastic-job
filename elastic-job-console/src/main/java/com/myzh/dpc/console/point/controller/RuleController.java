package com.myzh.dpc.console.point.controller;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myzh.dcp.model.point.entity.PointRule;
import com.myzh.dpc.console.core.controller.support.ResponseWrapper;
import com.myzh.dpc.console.core.mybatis.pagination.Page;
import com.myzh.dpc.console.core.mybatis.pagination.Pageable;
import com.myzh.dpc.console.point.model.form.BaseForm;
import com.myzh.dpc.console.point.model.form.point.ListPointRuleFrom;
import com.myzh.dpc.console.point.model.form.point.PointRuleForm;
import com.myzh.dpc.console.point.service.impl.RuleServiceImpl;

@Controller
@RequestMapping("/rule")
public class RuleController {

	@Resource
	private RuleServiceImpl ruleService;
	/**
	 * 查询所有用户的积分列表
	 * @param userId
	 * @param clinicCode
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public String ruleManager(BaseForm queryFrom, @PageableDefault Pageable pageable, final ModelMap model) {
		return "point/pointRuleManager";
	}
	
	/**
	 * 查询所有用户的积分列表
	 * @param userId
	 * @param clinicCode
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET, value="add")
	public String ruleAdd(BaseForm queryFrom, final ModelMap model) {
		return "point/pointRuleEdit";
	}
	
	/**
	 * 查询所有用户的积分列表
	 * @param userId
	 * @param clinicCode
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET, value="modify")
	public String ruleModify(BaseForm form, final ModelMap model) {
		PointRule pointRule = ruleService.getPointRule(Integer.valueOf(form.getId()));
		model.put("rule", pointRule);
		return "point/pointRuleEdit";
	}
	
	/**
	 * 保存规则
	 * @param userId
	 * @param clinicCode
	 * @return
	 */
	@RequestMapping(value="save")
	@ResponseBody
	public ResponseEntity<?> saveRule(PointRuleForm pointRuleForm) {
		ResponseWrapper rw = new ResponseWrapper();
		PointRule pointRule = new PointRule();
		BeanUtils.copyProperties(pointRuleForm, pointRule);
		ruleService.savePointRule(pointRule);
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
	public ResponseEntity<?> ruleList(ListPointRuleFrom listFrom, @PageableDefault Pageable pageable, final ModelMap model) {
		ResponseWrapper rw = new ResponseWrapper();
		Page<PointRule> pointRulePage = ruleService.getPointRules(listFrom.getAreaCode(), listFrom.getClinicCode(), listFrom.getAgentCode(), listFrom.getBehaviorCode(), pageable);
		rw.add("rows", pointRulePage);
		rw.add("total", pointRulePage.getTotal());
		return new ResponseEntity<ResponseWrapper>(rw, HttpStatus.OK);
	}
	

}
