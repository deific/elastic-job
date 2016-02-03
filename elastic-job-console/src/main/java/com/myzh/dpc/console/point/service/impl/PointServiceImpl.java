package com.myzh.dpc.console.point.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.myzh.dcp.model.point.entity.UserPoint;
import com.myzh.dcp.model.point.entity.UserPointDetail;
import com.myzh.dcp.model.point.enums.PointType;
import com.myzh.dpc.console.core.exception.ExceptionCode;
import com.myzh.dpc.console.core.exception.ExceptionFactory;
import com.myzh.dpc.console.core.mybatis.pagination.Page;
import com.myzh.dpc.console.core.mybatis.pagination.Pageable;
import com.myzh.dpc.console.point.dao.PointDao;
import com.myzh.dpc.console.point.service.PointService;

@Service
public class PointServiceImpl implements PointService {

	@Resource
	private PointDao pointDao;
	@Resource
	private ExceptionFactory exceptionFactory;
	
	@Override
	public UserPoint getUserPoint(String userId, String clinicCode) {
		return pointDao.getUserPoint(userId, clinicCode);
	}

	@Override
	public Page<UserPoint> queryUserPointPgae(String userId, String clinicCode, Pageable pageable) {
		return pointDao.queryUserPointList(userId, clinicCode, pageable);
	}
	
	@Override
	public List<UserPointDetail> getUserPointDetails(String userId, String clinicCode, String pointType, String startDate, String endDate) {
		return pointDao.getUserPointDetails(userId, clinicCode, pointType, startDate, endDate);
	}

	@Override
	public Page<UserPointDetail> getUserPointDetails(String userId, String clinicCode, String pointType, String startDate, String endDate,
			Pageable pageable) {
		Page<UserPointDetail> page = pointDao.getUserPointDetails(userId, clinicCode, pointType, startDate, endDate, pageable);
		return page;
	}

	@Transactional
	@Override
	public void createPoint(String userId, String clinicCode, String channel, Integer point) {
		UserPointDetail userPointDetail = newUserPointDetail(channel, PointType.produce, userId, clinicCode, point);
		savePoint(userPointDetail);
	}
	
	@Override
	public UserPoint consumePoint(String userId, String clinicCode, String channel, Integer point) {
		UserPoint userPoint = getUserPoint(userId, clinicCode);
		Integer allPoint = userPoint.getAllPoint();
		// 余额不足
		if (allPoint == null || point > allPoint) {
			throw exceptionFactory.create(ExceptionCode.User_Point_Is_Not_Enough);
		}
		
		UserPointDetail userPointDetail = newUserPointDetail(channel, PointType.consume, userId, clinicCode, point);
		savePoint(userPoint, userPointDetail);
		
		return userPoint;
	}

	@Override
	public void savePoint(UserPointDetail userPointDetail) {
		savePoint(null, userPointDetail);
	}
	
	@Override
	@Transactional
	public void savePoint(UserPoint userPoint, UserPointDetail userPointDetail) {
		// 更新总分
		if (userPoint == null) {
			userPoint = pointDao.getUserPoint(userPointDetail.getUserId(), userPointDetail.getClinicCode());
		}
		if (userPoint != null) {
			userPoint.setAllPoint(userPoint.getAllPoint() + userPointDetail.getPoint());
			pointDao.updateUserPoint(userPoint);
		} else {
			userPoint = newUserPoint(userPointDetail.getUserId(), userPointDetail.getClinicCode(), userPointDetail.getPoint());
			pointDao.saveUserPoint(userPoint);
		}
		// 保存明细
		pointDao.saveUserPointDetail(userPointDetail);
	}
	
	
	private UserPoint newUserPoint(String userId, String clinicCode, Integer point) {
		UserPoint userPoint = new UserPoint();
		userPoint.setUserId(userId);
		userPoint.setClinicCode(clinicCode);
		Date now = new Date();
		userPoint.setYear(FastDateFormat.getInstance("yyyy").format(now));
		userPoint.setCreateTime(now);
		userPoint.setUpdateTime(now);
		userPoint.setAllPoint(point);
		return userPoint;
	}
	
	private UserPointDetail newUserPointDetail(String pointDimension, PointType pointType, String userId, String clinicCode, Integer point) {
		UserPointDetail userPointDetail = new UserPointDetail();
		userPointDetail.setUserId(userId);
		userPointDetail.setPointType(pointType.getCode());
		userPointDetail.setPointTime(new Date());
		if (pointType.equals(PointType.consume)) {
			userPointDetail.setPoint(-point);
		} else {
			userPointDetail.setPoint(point);
		}
		userPointDetail.setDesc("您" + pointType.getDesc() + "了" + point + "积分");
		userPointDetail.setClinicCode(clinicCode);
		userPointDetail.setPointDimension(pointDimension);
		return userPointDetail;
	}

	@Override
	public Map<String, Object> getPointStatistics(Integer days) {
		if (days == null) {
			days = 7;
		}
		List<Map<String, Object>> statisticMaps = pointDao.getPointStatistics(days);
		Map<String, Object> statisticData = new HashMap<String, Object>();
		if (statisticMaps != null && !statisticMaps.isEmpty()) {
			List<String> categories = Lists.newArrayList();
			List<Map<String, Object>> series = Lists.newArrayList();
			
			String date = null;
			String province = null;
			for (Map<String, Object> statisticMap: statisticMaps) {
				date = (String)statisticMap.get("date");
				if (!categories.contains(date)) {
					categories.add(date);
				}
				province = (String)statisticMap.get("province");
				province = StringUtils.trimToEmpty(province);
				
				Map<String, Object> seriesMap = getSeriesMapByProvince(series, province);
				if (seriesMap == null) {
					seriesMap = new HashMap<String, Object>();
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("name", province);
					itemMap.put("y", ((BigDecimal)statisticMap.get("point")).intValue());
					itemMap.put("drilldown", true);
					List<Map<String, Object>> data = Lists.newArrayList();
					data.add(itemMap);
					seriesMap.put("name", province);
					seriesMap.put("data", data);
					series.add(seriesMap);
				} else {
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("name", province);
					itemMap.put("y", ((BigDecimal)statisticMap.get("point")).intValue());
					itemMap.put("drilldown", true);
					((List<Map<String, Object>>)seriesMap.get("data")).add(itemMap);
				}
			}
			statisticData.put("categories", categories);
			statisticData.put("series", series);
		}
		
		return statisticData;
	}
	
	
	private Map<String, Object> getSeriesMapByProvince(List<Map<String, Object>> series, String province) {
		for (Map<String, Object> seriesMap : series) {
			if (province.equals((String)seriesMap.get("name"))) {
				return seriesMap;
			}
		}
		return null;
	}
}
