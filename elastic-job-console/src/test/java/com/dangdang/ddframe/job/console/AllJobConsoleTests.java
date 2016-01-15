
package com.dangdang.ddframe.job.console;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.dangdang.ddframe.job.console.util.HomeFolderTest;
import com.dangdang.ddframe.job.console.util.JobNodePathTest;

@RunWith(Suite.class)
@SuiteClasses({
    HomeFolderTest.class, 
    JobNodePathTest.class
    })
public class AllJobConsoleTests {
}
