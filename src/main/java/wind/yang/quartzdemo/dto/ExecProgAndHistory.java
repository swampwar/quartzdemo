package wind.yang.quartzdemo.dto;

import lombok.*;
import wind.yang.quartzdemo.code.JobExecutionStatusCode;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExecProgAndHistory {
    String triggerGroup;
    String triggerName;
    int seq;
    String programName;

    String jobSttDtm;
    String jobEndDtm;
    String jobGroup;
    String jobName;
    JobExecutionStatusCode jobExecStaCd;
    String jobExecRslt;

    public ExecProgAndHistory(String triggerGroup, String triggerName, int seq, String programName, JobExecutionStatusCode jobExecStaCd, String jobName) {
        this.triggerGroup = triggerGroup; this.triggerName = triggerName; this.seq = seq;
        this.programName = programName; this.jobExecStaCd = jobExecStaCd; this.jobName = jobName;
    }
}
