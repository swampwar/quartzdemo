package wind.yang.quartzdemo.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequest {
    private String triggerName;
    private String triggerGroup;
    private String triggerDescription;
    private String cronExpression;
    private String jobName;
    private String jobGroup;
    private String shellScriptNm;
}
