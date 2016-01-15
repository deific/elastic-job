package com.dangdang.tracker.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type;

import com.dangdang.ddframe.job.api.JobConfiguration;
import com.dangdang.ddframe.job.integrate.fixture.simple.SimpleElasticJob;
import com.dangdang.ddframe.job.internal.config.ConfigurationNode;
import com.dangdang.ddframe.job.internal.listener.AbstractJobListener;
import com.dangdang.ddframe.job.internal.listener.AbstractListenerManager;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import com.dangdang.tracker.JobTracker;

import lombok.extern.slf4j.Slf4j;

/**
 * 任务配置监听管理器
 * 通过对zookeeper的任务节点新增任务配置节点，动态的添加执行任务
 * @author chensg
 *
 */
@Slf4j
public class JobConfigurationListenerManager extends AbstractListenerManager {

	private ConfigurationNode configurationNode;
	private JobTracker jobTracker;
	public JobConfigurationListenerManager(CoordinatorRegistryCenter coordinatorRegistryCenter,
			JobConfiguration jobConfiguration) {
		super(coordinatorRegistryCenter, jobConfiguration);
	}
	
	public JobConfigurationListenerManager(CoordinatorRegistryCenter coordinatorRegistryCenter,
			JobConfiguration jobConfiguration, JobTracker jobTracker) {
		super(coordinatorRegistryCenter, jobConfiguration);
		this.jobTracker = jobTracker;
	}

	@Override
	public void start() {
		listenJobConfiguration();
	}
	
	/**
	 * 监听
	 */
	private void listenJobConfiguration() {
        addDataListener(new AbstractJobListener() {
            @Override
            protected void dataChanged(final CuratorFramework client, final TreeCacheEvent event, final String path) {
                if (Type.NODE_REMOVED == event.getType() && configurationNode.isNewConfiguration(path)) {
                    log.debug("Elastic job: new job configuration create, start initialization job configuration.");
                    
                    client.getData();
                    
                    JobConfiguration jobConfiguration = new JobConfiguration("simpleElasticDemoJob", SimpleElasticJob.class, 10, "0/5 * * * * ?");
                    jobTracker.newJob(jobConfiguration);
                    log.debug("Elastic job: initializate job configuration completed.");
                }
            }
        });
    }
}
