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
    String summary;
    String description;
    String execParam1;
    String execParam2;
    String execParam3;

    String triggerSttDtm;
    String jobSttDtm;
    String jobEndDtm;
    String jobGroup;
    String jobName;
    JobExecutionStatusCode jobExecStaCd;
    String jobExecRslt;

    public ExecProgAndHistory(String triggerGroup, String triggerName, int seq, String programName,
                              String summary, String description, String execParam1, String execParam2, String execParam3
                              ,JobExecutionStatusCode jobExecStaCd, String jobName) {
        this.triggerGroup = triggerGroup; this.triggerName = triggerName; this.seq = seq;
        this.programName = programName; this.jobExecStaCd = jobExecStaCd; this.jobName = jobName;
        this.summary = summary; this.description = description; this.execParam1 = execParam1; this.execParam2 = execParam2; this.execParam3 = execParam3;
    }
}
