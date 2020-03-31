package wind.yang.quartzdemo.dto;

import lombok.*;

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
    String jobExecStaCd;
    String jobExecRslt;

    public ExecProgAndHistory(String triggerGroup, String triggerName, int seq, String programName) {
        this.triggerGroup = triggerGroup; this.triggerName = triggerName; this.seq = seq;
        this.programName = programName;
    }
}
