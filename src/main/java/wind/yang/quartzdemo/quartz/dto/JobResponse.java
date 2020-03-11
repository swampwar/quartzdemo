package wind.yang.quartzdemo.quartz.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class JobResponse {
    private String schedulerName;
    private String triggerName;
    private String triggerGroup;
    private String cronExpression;
    private String jobName;
    private String jobGroup;
    private String triggerStatus;
    private String startTime;
    private String nextFireTime;
    private String prevFireTime;
}
