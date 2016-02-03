package com.myzh.dcp.model.point.entity;

import java.io.Serializable;
import java.util.Date;

import com.myzh.dcp.model.point.enums.RuleScope;
import com.myzh.dcp.model.utils.DateUtil;

import lombok.Data;

/**
 * 规则状态
 * @author chensg
 *
 */
@Data
public class RuleState implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer ruleId;
	private String userId;
	private Date scope;
	private String state = "";
	private Date updateTime;
	private int fireCount = 0;
	private double totalPoint;
	private double totalValue = 0d;
	
	public void nextScope(Date scopeDate) {
		scope = scopeDate;
		totalPoint = 0d;
		totalValue = 0d;
		updateTime = new Date();
	}
	public void addFireCount() {
		fireCount ++;
	}
	/**
	 * 周期内的积分值
	 * @param value
	 */
	public void addPoint(double value) {
		totalPoint = totalPoint + value;
	}
	/**
	 * 周期内的累计行为值
	 * @param value
	 */
	public void addValue(double value) {
		totalValue = totalValue + value;
	}
	
	/**
	 * 是否在范围内
	 * @param ruleScope
	 * @return
	 */
	public boolean isInScope(PointRule rule, Date scopeDate) {
		boolean isInScope = false;
		RuleScope ruleScope = null;
		if (rule.getScope() != null && !"".equals(rule.getScope())) {
			ruleScope = RuleScope.valueOf(rule.getScope());
		} else {
			ruleScope = RuleScope.onlyOnce;
		}
		
		switch (ruleScope) {
		case hour:
			isInScope = scope == null || DateUtil.isSameHour(scopeDate, scope);
			break;
		case day:
			isInScope = scope == null ||  DateUtil.isSameDay(scopeDate, scope);
			break;
		case week:
			isInScope = scope == null ||  DateUtil.isSameWeek(scopeDate, scope);
			break;
		case month:
			isInScope = scope == null ||  DateUtil.isSameMonth(scopeDate, scope);
			break;
		case year:
			isInScope = scope == null ||  DateUtil.isSameYear(scopeDate, scope);
			break;
		case onlyOnce:
			isInScope = scope != null;
			break;
		case everyTime:
			isInScope = false;
			break;
		case loop:
			isInScope = true;
			break;
		case period:
			isInScope = true;
			break;
		default:
			isInScope = false;	
		}
		scope = scopeDate;
		return isInScope;
	}
}
