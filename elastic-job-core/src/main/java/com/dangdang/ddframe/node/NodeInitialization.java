package com.dangdang.ddframe.node;

import com.dangdang.ddframe.job.internal.server.ServerService;

public class NodeInitialization {
	private final ServerService serverService;
	
	NodeInitialization(ServerService serverService) {
		this.serverService = serverService;
	}
}
