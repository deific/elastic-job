
package com.myzh.dpc.console.job.util;

import java.io.File;

public final class HomeFolder {
    
    private static final String USER_HOME = System.getProperty("user.home");
    
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    
    private static final String CONSOLE_ROOT_FOLDER = ".elastic-job-console";
    
    private HomeFolder() {
    }
    
    public static String getFilePathInHomeFolder(final String fileName) {
        return String.format("%s%s", getHomeFolder(), fileName);
    }
    
    public static void createHomeFolderIfNotExisted() {
        File file = new File(getHomeFolder());
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    
    private static String getHomeFolder() {
        return String.format("%s%s%s%s", USER_HOME, FILE_SEPARATOR, CONSOLE_ROOT_FOLDER, FILE_SEPARATOR);
    }
}
