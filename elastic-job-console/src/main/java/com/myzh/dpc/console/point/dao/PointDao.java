package com.myzh.dpc.console.point.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.myzh.dcp.model.point.entity.PointRule;
import com.myzh.dcp.model.point.entity.UserPoint;
import com.myzh.dcp.model.point.entity.UserPointDetail;
import com.myzh.dpc.console.core.mybatis.pagination.Page;
import com.myzh.dpc.console.core.mybatis.pagination.Pageable;

/**
 * 积分查询DAO
 * @author chensg
 *
 */
public interface PointDao {

	/**
	 * 根据用户id或诊所id获取用户积分
	 * @param userId
	 * @param clinicCode
	 * @return
	 */
	public UserPoint getUserPoint(@Param(value = "userId")String userId, @Param(value = "clinicCode")String clinicCode);
	
	/**
	 * 根据用户Id或诊所id获取用户积分明细
	 * @param userId
	 * @param clinicCode
	 * @return
	 */
	public Page<UserPoint> queryUserPointList(
			@Param(value = "userId")String userId, 
			@Param(value = "clinicCode")String clinicCode,
			Pageable pageable
			);
	
	/**
	 * 保存用户积分
	 * @param userPoint
	 * @return
	 */
	public int saveUserPoint(UserPoint userPoint);
	/**
	 * 更新用户积分
	 * @param userPoint
	 * @return
	 */
	public int updateUserPoint(UserPoint userPoint);
	
	
	/**
	 * 根据用户Id或诊所id获取用户积分明细
	 * @param userId
	 * @param clinicCode
	 * @return
	 */
	public List<UserPointDetail> getUserPointDetails(
			@Param(value = "userId")String userId, 
			@Param(value = "clinicCode")String clinicCode,
			@Param(value = "pointType") String pointType, 
			@Param(value = "startDate")String startDate,
			@Param(value = "endDate")String endDate
			);
	
	/**
	 * 保存用户积分明细
	 * @param userPoint
	 * @return
	 */
	public int saveUserPointDetail(UserPointDetail userPointDetail);
	
	
	/**
	 * 根据用户Id或诊所id获取用户积分明细
	 * @param userId
	 * @param clinicCode
	 * @return
	 */
	public Page<UserPointDetail> getUserPointDetails(
			@Param(value = "userId")String userId, 
			@Param(value = "clinicCode")String clinicCode,
			@Param(value = "pointType") String pointType, 
			@Param(value = "startDate")String startDate,
			@Param(value = "endDate")String endDate,
			Pageable pageable
			);
	
	
	/**
	 * 获取规则
	 * @param areaCode
	 * @param clinicCode
	 * @param agentCode
	 * @param behaviorCode
	 * @param pageable
	 * @return
	 */
	public Page<PointRule> getPointRules(
			@Param(value = "areaCode")String areaCode, 
			@Param(value = "clinicCode")String clinicCode,
			@Param(value = "agentCode") String agentCode, 
			@Param(value = "behaviorCode")String behaviorCode,
			Pageable pageable
			);
	
	/**
	 * 获取规则
	 * @param id
	 * @return
	 */
	public PointRule getPointRuleById(
			@Param(value = "id")Integer id
			);
	
	/**
	 * 保存
	 * @param rule
	 * @return
	 */
	public int savePointRule(PointRule rule);
	
	/**
	 * 更新
	 * @param rule
	 * @return
	 */
	public int updatePointRule(PointRule rule);
	
	/**
	 * 积分统计
	 * @param days
	 * @return
	 */
	public List<Map<String, Object>> getPointStatistics(
			@Param(value = "areaCode") String areaCode, 
			@Param(value = "clinicCode")  String clinicCode,
			@Param(value = "days") Integer days
			);
}
