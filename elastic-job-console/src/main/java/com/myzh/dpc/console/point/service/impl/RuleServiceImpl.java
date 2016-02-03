package com.myzh.dpc.console.point.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myzh.dcp.model.point.entity.PointRule;
import com.myzh.dpc.console.core.exception.ExceptionFactory;
import com.myzh.dpc.console.core.mybatis.pagination.Page;
import com.myzh.dpc.console.core.mybatis.pagination.Pageable;
import com.myzh.dpc.console.point.dao.PointDao;

@Service
public class RuleServiceImpl {

	@Resource
	private PointDao pointDao;
	@Resource
	private ExceptionFactory exceptionFactory;
	
	public Page<PointRule> getPointRules(String areaCode, String clinicCode,String agentCode,  String behaviorCode, Pageable pageable) {
		return pointDao.getPointRules(areaCode, clinicCode, agentCode, behaviorCode, pageable);
	}
	
	public PointRule getPointRule(Integer id) {
		return pointDao.getPointRuleById(id);
	}
	
	public int savePointRule(PointRule rule) {
		int result = 0;
		if (rule.getId() == null) {
			rule.setCreateTime(new Date());
			rule.setCreateUser("");
			rule.setUpdateTime(rule.getCreateTime());
			rule.setUpdateUser("");
			result = pointDao.savePointRule(rule);
		} else {
			rule.setUpdateTime(new Date());
			rule.setUpdateUser("");
			result = pointDao.updatePointRule(rule);
		}
		return result;
	}
	
}
