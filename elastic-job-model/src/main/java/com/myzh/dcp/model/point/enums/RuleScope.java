package com.myzh.dcp.model.point.enums;

/**
 * 规则运行有效范围
 * @author chensg
 *
 */
public enum RuleScope {
	hour, // 按小时
	day,  // 按天
	week, // 按周
	month,// 按月
	year, // 按年
	onlyOnce,// 单次
	everyTime,  // 每一次
	loop, // 循环
	period // 阶段，范围
}
