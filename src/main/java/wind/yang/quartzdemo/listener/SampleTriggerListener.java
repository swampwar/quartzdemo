package wind.yang.quartzdemo.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import wind.yang.quartzdemo.code.JobExecutionStatusCode;
import wind.yang.quartzdemo.dto.TBIBM710;
import wind.yang.quartzdemo.service.ExecProgService;
import wind.yang.quartzdemo.service.JobGropService;
import wind.yang.quartzdemo.service.QuartzService;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class SampleTriggerListener implements TriggerListener {
    @Autowired
    ExecProgService epSvc;

    @Autowired
    JobGropService jobGropService;

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
//            jobDataMap.put("execProg", epSvc.findOriginalByTrigger(trigger.getKey()));
            log.debug("parsing trigger name : {}",trigger.getKey().getName().substring(0, trigger.getKey().getName().indexOf(".")));
            String parseName = trigger.getKey().getName().substring(0, trigger.getKey().getName().indexOf("."));
            TriggerKey triggerKey = new TriggerKey(parseName, trigger.getKey().getGroup());
            jobDataMap.put("TBIBM710", jobGropService.findByTrigger(triggerKey));
        }else{ // 일반 실행이면
//            jobDataMap.put("execProg", epSvc.findByTrigger(trigger.getKey()));
            jobDataMap.put("TBIBM710", jobGropService.findByTrigger(trigger.getKey()));
        }
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        log.debug("vetoJobExecution 실행가능여부 검사");
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();


        // 잡이 실행가능 상태인지 검증한다.
        try {
            if(isTriggerDuplicateRunning(trigger.getKey())){ // 중복실행이면 종료
                jobDataMap.put("vetoJobRsltMsg", "작업그룹이 중복실행 되어 종료합니다.");
                jobDataMap.put("vetoJobRsltCode", "7"); //JobExecutionStatusCode.DUPLICATE
                return true;
            }
            if(context.getJobDetail().getJobDataMap().get("TBIBM710") == null){ // 실행프로그램이 없으면 종료
                jobDataMap.put("vetoJobRsltMsg", "정산작업결과 상세 테이블(TBIBD750)에 등록 실패");
                jobDataMap.put("vetoJobRsltCode", "3"); //JobExecutionStatusCode.ERROR
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
