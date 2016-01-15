
package com.dangdang.ddframe.job.console.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.myzh.dpc.console.job.util.JobNodePath;

public final class JobNodePathTest {
    
    @Test
    public void assertGetConfigNodePath() {
        assertThat(JobNodePath.getConfigNodePath("test_job", "test"), is("/test_job/config/test"));
    }
    
    @Test
    public void assertGetServerNodePath() {
        assertThat(JobNodePath.getServerNodePath("test_job"), is("/test_job/servers"));
        assertThat(JobNodePath.getServerNodePath("test_job", "localhost", "test"), is("/test_job/servers/localhost/test"));
    }
    
    @Test
    public void assertGetExecutionNodePath() {
        assertThat(JobNodePath.getExecutionNodePath("test_job"), is("/test_job/execution"));
        assertThat(JobNodePath.getExecutionNodePath("test_job", "0", "test"), is("/test_job/execution/0/test"));
    }
    
    @Test
    public void assertGetLeaderNodePath() {
        assertThat(JobNodePath.getLeaderNodePath("test_job", "test/test"), is("/test_job/leader/test/test"));
    }
}
