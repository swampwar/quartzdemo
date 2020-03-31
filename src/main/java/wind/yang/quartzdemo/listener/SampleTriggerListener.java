package wind.yang.quartzdemo.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import wind.yang.quartzdemo.code.JobExecutionStatusCode;
import wind.yang.quartzdemo.service.ExecProgService;
import wind.yang.quartzdemo.service.QuartzService;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class SampleTriggerListener implements TriggerListener {
    @Autowired
    ExecProgService epSvc;

    @Lazy
    @Autowired
    QuartzService quartzSvc;

    @Override
    public String getName() {
        return "SampleTriggerListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        log.debug("triggerFired at {} : JobKey : {}", trigger.getStartTime(), trigger.getJobKey());
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        // 실행 프로그램 조회
        if(trigger.getKey().getName().contains(".F.")){ // 강제 실행이면
            jobDataMap.put("execProg", epSvc.findOriginalByTrigger(trigger.getKey()));
        }else{ // 일반 실행이면
            jobDataMap.put("execProg", epSvc.findByTrigger(trigger.getKey()));
        }
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        log.debug("vetoJobExecution 실행가능여부 검사");
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        // 잡이 실행가능 상태인지 검증한다.
        try {
            if(isTriggerDuplicateRunning(trigger.getKey())){ // 중복실행이면 종료
                jobDataMap.put("vetoJobRsltMsg", "Trigger가 중복실행 되어 종료합니다.");
                jobDataMap.put("vetoJobRsltCode", JobExecutionStatusCode.DUPLICATE);
                return true;
            }
            if(context.getJobDetail().getJobDataMap().get("execProg") == null){ // 실행프로그램이 없으면 종료
                jobDataMap.put("vetoJobRsltMsg", "Trigger에 실행가능한 프로그램이 없습니다.");
                jobDataMap.put("vetoJobRsltCode", JobExecutionStatusCode.ERROR);
                return true;
            }
        } catch (SchedulerException e) {
            return true; // 실행종료
        }
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

    private boolean isTriggerDuplicateRunning(TriggerKey triggerKey) throws SchedulerException {
        try {
            int runningTriggerCnt = quartzSvc.getRunningTriggerCnt(triggerKey);
            if(runningTriggerCnt > 1){ // 본인을 제외하고 실행중인 Job이 있으면
                log.error("Trigger[{}]가 이미 실행중이므로 Job을 종료합니다.(실행건수 2건 이상)", triggerKey);
                return true; // 중복실행
            }else{
                return false; // 단독실행
            }
        } catch (SchedulerException e) {
            log.error("Trigger[{}] 실행여부 조회 중 에러발생으로 Job을 종료합니다.", triggerKey);
            throw e;
        }
    }
}
