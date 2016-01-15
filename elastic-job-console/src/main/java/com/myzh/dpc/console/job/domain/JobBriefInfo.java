
package com.myzh.dpc.console.job.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class JobBriefInfo implements Serializable, Comparable<JobBriefInfo> {
    
    private static final long serialVersionUID = 8405751873086755148L;
    
    private String jobName;
    
    private JobStatus status;
    
    private String description;

    private String cron;
    
    @Override
    public int compareTo(final JobBriefInfo o) {
        return getJobName().compareTo(o.getJobName());
    }
    
    public enum JobStatus {
        OK, 
        PARTIAL_ALIVE, 
        MANUALLY_DISABLED, 
        ALL_CRASHED;
        
        public static JobStatus getJobStatus(final int okCount, final int crashedCount, final int manuallyDisabledCount, final int serverCount) {
            if (okCount == serverCount) {
                return JobStatus.OK;
            }
            if (crashedCount == serverCount) {
                return JobStatus.ALL_CRASHED;
            }
            if (crashedCount > 0) {
                return JobStatus.PARTIAL_ALIVE;
            }
            if (manuallyDisabledCount > 0) {
                return JobStatus.MANUALLY_DISABLED;
            }
            return JobStatus.OK;
        }
    }
}
