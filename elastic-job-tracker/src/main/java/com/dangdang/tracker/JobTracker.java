package com.dangdang.tracker;

import com.dangdang.ddframe.job.api.JobConfiguration;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperRegistryCenter;
import com.dangdang.tracker.listener.JobConfigurationListenerManager;

/**
 * 任务管理器
 * 接收任务运行请求，管理和执行任务调度器的启动停止
 * @author chensg
 *
 */
public class JobTracker {
	
	private final ZookeeperConfiguration zkConfig = new ZookeeperConfiguration("localhost:2181", "default-jobs", 1000, 3000, 3);
	private final CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(zkConfig);
	private JobConfigurationListenerManager jobConfigurationListenerManager = null;
	
	
	public void newJob(JobConfiguration jobConfiguration) {
		
	}
	
	
	/**
	 * 任务配置监听
	 */
	private void startJobConfigurationListener() {
		jobConfigurationListenerManager = new JobConfigurationListenerManager(regCenter, null);
		jobConfigurationListenerManager.start();
	}
}
