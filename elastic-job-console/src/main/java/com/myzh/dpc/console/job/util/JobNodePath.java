
package com.myzh.dpc.console.job.util;

public final class JobNodePath {
    
    private JobNodePath() {
    }
    
    public static String getConfigNodePath(final String jobName, final String nodeName) {
        return String.format("/%s/config/%s", jobName, nodeName);
    }
    
    public static String getServerNodePath(final String jobName) {
        return String.format("/%s/servers", jobName);
    }
    
    public static String getServerNodePath(final String jobName, final String serverIp, final String nodeName) {
        return String.format("%s/%s/%s", getServerNodePath(jobName), serverIp, nodeName);
    }
    
    public static String getExecutionNodePath(final String jobName) {
        return String.format("/%s/execution", jobName);
    }
    
    public static String getExecutionNodePath(final String jobName, final String item, final String nodeName) {
        return String.format("%s/%s/%s", getExecutionNodePath(jobName), item, nodeName);
    }
    
    public static String getLeaderNodePath(final String jobName, final String nodeName) {
        return String.format("/%s/leader/%s", jobName, nodeName);
    }
}
