package wind.yang.quartzdemo.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wind.yang.quartzdemo.mapper.ExecProgMapper;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class SampleTriggerListener implements TriggerListener {
    @Autowired
    ExecProgMapper execProgMapper;

    @Override
    public String getName() {
        return "SampleTriggerListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        log.debug("triggerFired at {} : JobKey : {}", trigger.getStartTime(), trigger.getJobKey());
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        // TODO 현재는 단일파일만 실행되도록 되어있다. 리스트 반환결과를 순차실행 하는 기능은 개발이 필요함
        jobDataMap.put("execProg", execProgMapper.findByTrigger(trigger.getKey().getGroup(), trigger.getKey().getName()).get(0));
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        log.debug("SampleTriggerListener vetoJobExecution 이벤트 감지 : false 리턴");
        // TODO 반환 프로그램이 없으면 에러가 나도록 수정
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
