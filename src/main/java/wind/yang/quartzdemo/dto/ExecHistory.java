package wind.yang.quartzdemo.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExecHistory {
    String jobSttDtm;
    String jobEndDtm;
    String triggerGroup;
    String triggerName;
    String jobGroup;
    String jobName;
    String jobExecStaCd;
    String execProgName;
    String jobExecRslt;
}
