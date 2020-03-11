package wind.yang.quartzdemo.quartz.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class SampleTriggerListener implements TriggerListener {
    private Map<String, String> triggerCmdMap = new HashMap<>();

    @PostConstruct
    public void init(){ // TODO cmd를 DB로 관리
        log.info("triggerCmdMap 초기화!!");
        triggerCmdMap.put("MGT_TRIGGER1", "trigger1.sh");
        triggerCmdMap.put("MGT_TRIGGER2", "trigger2.sh");
    }

    public void addTriggerCmd(String triggerName, String scriptName){
        triggerCmdMap.put(triggerName, scriptName);
    }

    public void removeTriggerCmd(String triggerName){
        triggerCmdMap.remove(triggerName);
    }

    @Override
    public String getName() {
        return "SampleTriggerListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        log.debug("triggerFired at {} : JobKey : {}", trigger.getStartTime(), trigger.getJobKey());
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        jobDataMap.put("shFileName", triggerCmdMap.get(trigger.getKey().getName()));
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        log.debug("SampleTriggerListener vetoJobExecution 이벤트 감지 : false 리턴");
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        log.debug("triggerMisfired at {} : JobKey : {}", trigger.getStartTime(), trigger.getJobKey());
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        log.debug("triggerComplete at {} : JobKey : {}", trigger.getStartTime(), trigger.getJobKey());
    }
}
