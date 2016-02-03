package com.myzh.dpc.console.point.service;

import java.util.List;
import java.util.Map;

import com.myzh.dcp.model.point.entity.UserPoint;
import com.myzh.dcp.model.point.entity.UserPointDetail;
import com.myzh.dpc.console.core.mybatis.pagination.Page;
import com.myzh.dpc.console.core.mybatis.pagination.Pageable;

/**
 * 积分服务
 * @author chensg
 *
 */
public interface PointService {

	/**
	 * 根据用户id或诊所id获取用户积分
	 * @param userId
	 * @param clinicCode
	 * @return
	 */
	public UserPoint getUserPoint(String userId, String clinicCode);
	/**
	 * 查询用户积分列表
	 * @return
	 */
	public Page<UserPoint> queryUserPointPgae(String userId, String clinicCode, Pageable pageable);
	
	/**
	 * 保存积分
	 * @param userPoint
	 * @param userPointDetail
	 */
	public void savePoint(UserPoint userPoint, UserPointDetail userPointDetail);
	
	/**
	 * 保存积分
	 * @param userPoint
	 * @param userPointDetail
	 */
	public void savePoint(UserPointDetail userPointDetail);
	
	/**
	 * 创建积分
	 * @param userId
	 * @param clinicCode
	 * @param channel
	 * @param point
	 */
	public void createPoint(String userId, String clinicCode, String channel, Integer point);
	/**
	 * 消费积分
	 * @param userId
	 * @param clinicCode
	 * @param channel
	 * @param point
	 */
	public UserPoint consumePoint(String userId, String clinicCode, String channel, Integer point);
	
	/**
	 * 根据用户Id或诊所id获取用户积分明细
	 * @param userId
	 * @param clinicCode
	 * @return
	 */
	public List<UserPointDetail> getUserPointDetails(String userId, String clinicCode, String pointType, String startDate, String endDate);
	
	/**
	 * 根据用户Id或诊所id获取用户积分明细
	 * @param userId
	 * @param clinicCode
	 * @return
	 */
	public Page<UserPointDetail> getUserPointDetails(String userId, String clinicCode, String pointType, String startDate, String endDate, Pageable pageable);

	/**
	 * 获取积分统计数据
	 * @param days
	 * @return
	 */
	public Map<String, Object> getPointStatistics(Integer days);
}
