package wind.yang.quartzdemo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.quartz.CronTrigger;
import org.quartz.Trigger;

import java.text.SimpleDateFormat;
import java.util.Date;

import static wind.yang.quartzdemo.util.QuartzStringUtils.dateToString;

@Getter
@Setter
@Builder
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
    private String triggerExecStaCd;
    private String description;

    public static JobResponse of(Trigger trigger, String triggerStatus) {
        JobResponse jobResponse = JobResponse.builder()
                                            .triggerGroup(trigger.getKey().getGroup())
                                            .triggerName(trigger.getKey().getName())
                                            .jobGroup(trigger.getJobKey().getGroup())
                                            .jobName(trigger.getJobKey().getName())
                                            .nextFireTime(dateToString(trigger.getNextFireTime()))
                                            .prevFireTime(dateToString(trigger.getPreviousFireTime()))
                                            .startTime(dateToString(trigger.getStartTime()))
                                            .triggerStatus(triggerStatus)
                                            .description(trigger.getDescription())
                                            .build();
        if(trigger instanceof CronTrigger){
            jobResponse.setCronExpression(((CronTrigger)trigger).getCronExpression());
        }

        return jobResponse;
    }


}
