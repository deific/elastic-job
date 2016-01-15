
package com.dangdang.ddframe.job.console.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.myzh.dpc.console.job.util.HomeFolder;

public final class HomeFolderTest {
    
    private static final String HOME_FOLDER = System.getProperty("user.home") + System.getProperty("file.separator") + ".elastic-job-console" + System.getProperty("file.separator");
    
    @Test
    public void assertGetFilePathInHomeFolder() {
        assertThat(HomeFolder.getFilePathInHomeFolder("test_file"), is(HOME_FOLDER + "test_file"));
    }
}
