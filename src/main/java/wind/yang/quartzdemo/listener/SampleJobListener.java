package wind.yang.quartzdemo.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wind.yang.quartzdemo.code.JobExecutionStatusCode;
import wind.yang.quartzdemo.dto.ExecHistory;
import wind.yang.quartzdemo.dto.ExecProg;
import wind.yang.quartzdemo.dto.PushMessage;
import wind.yang.quartzdemo.service.ExecHistoryService;
import wind.yang.quartzdemo.service.SendMsgService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class SampleJobListener implements JobListener {
    @Autowired
    ExecHistoryService ehSvc;

    @Autowired
    SendMsgService smSvc;

    @Override
    public String getName() {
        return "SampleJobListener";
    }

    /**
     * 잡이 실행전 시작 실행이력을 저장한다.
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        // 신규 실행이력 저장
        List<ExecProg> execProgList = (List<ExecProg>) context.getJobDetail().getJobDataMap().get("execProg");
        ExecHistory masterExecHistory = ehSvc.insertStartExecHistory(context.getTrigger().getKey(), context.getJobDetail().getKey(), execProgList);

        // 실행종료 후 이력 업데이트용으로 JobDataMap에 넣는다.
        context.getJobDetail().getJobDataMap().put("execHistory", masterExecHistory);

        JobKey jobKey = context.getJobDetail().getKey();
        log.info("jobToBeExecuted : jobKey : {}", jobKey);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        Trigger trigger = context.getTrigger();
        log.error("잡 실행이 거부되었습니다. : TRIGGER[{}], JOB[{}]", trigger.getKey(), jobKey);

        // 신규 실행이력 저장
        List<ExecProg> execProgList = (List<ExecProg>) context.getJobDetail().getJobDataMap().get("execProg");
        ExecHistory masterExecHistory = ehSvc.insertStartExecHistory(context.getTrigger().getKey(), context.getJobDetail().getKey(), execProgList);

        // 거부된 실행이력을 업데이트 한다.
        masterExecHistory.setJobExecStaCd((JobExecutionStatusCode) context.getJobDetail().getJobDataMap().get("vetoJobRsltCode"));
        masterExecHistory.setJobExecRslt((String) context.getJobDetail().getJobDataMap().get("vetoJobRsltMsg"));
        String jobEndDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        masterExecHistory.setJobEndDtm(jobEndDtm);
        ehSvc.updateExecHistory(masterExecHistory);
    }

    /**
     * 잡이 실행 후 결과 실행이력을 저장한다.
     * 정상실행시 : JobExecutionException = null
     */
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        // 실행결과 DB Update
        ExecHistory execHistory = (ExecHistory)context.getJobDetail().getJobDataMap().get("execHistory");
        String jobEndDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        execHistory.setJobEndDtm(jobEndDtm);
        System.out.println("완료후 이력 업데이트 : " + execHistory.getTriggerName() + " / 시퀀스 : " + execHistory.getExecProgSeq());

        if(jobException == null){ // 정상실행이면
            execHistory.setJobExecStaCd(JobExecutionStatusCode.SUCCESS);
            execHistory.setJobExecRslt(JobExecutionStatusCode.SUCCESS.getMsg());

        }else{ // 에러발생이면
            execHistory.setJobExecStaCd(JobExecutionStatusCode.ERROR);
            execHistory.setJobExecRslt(jobException.getMessage());
        }

        ehSvc.updateExecHistory(execHistory);

        // 에러 발생시 Push 전송
        if (jobException != null ) smSvc.notifySlack(new PushMessage(execHistory));

        JobKey jobKey = context.getJobDetail().getKey();
        log.info("jobWasExecuted : jobKey : {}", jobKey);
    }
}
