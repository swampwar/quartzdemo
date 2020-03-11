package wind.yang.quartzdemo.quartz.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JobRequest {
    private String triggerName;
    private String triggerGroup;
    private String triggerDescription;
    private String cronExpression;
    private String jobName;
    private String jobGroup;
    private String shellScriptNm;
}
