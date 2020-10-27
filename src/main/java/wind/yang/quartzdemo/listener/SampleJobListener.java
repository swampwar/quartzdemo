package wind.yang.quartzdemo.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wind.yang.quartzdemo.code.JobExecutionStatusCode;
import wind.yang.quartzdemo.dto.*;
import wind.yang.quartzdemo.service.ExecHistoryService;
import wind.yang.quartzdemo.service.ExecProgService;
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
    ExecProgService execProgService;

//    @Autowired
//    SendMsgService smSvc;

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
//        List<ExecProg> execProgList = (List<ExecProg>) context.getJobDetail().getJobDataMap().get("execProg");
        TBIBM710 tbibm710 = (TBIBM710) context.getJobDetail().getJobDataMap().get("TBIBM710");
        TBIBM713 tbibm713 = execProgService.findProgByProgId(tbibm710.getSettmWorkDvsCd(), tbibm710.getProgId());
//        ExecHistory masterExecHistory = ehSvc.insertStartExecHistory(context.getTrigger().getKey(), context.getJobDetail().getKey(), execProgList);
        TBIBD760 masterExecHistory = ehSvc.insertStartExecHistory(tbibm710, tbibm713);
        log.debug("TBIBD760 : {}", masterExecHistory);

        // 실행종료 후 이력 업데이트용으로 JobDataMap에 넣는다.
//        context.getJobDetail().getJobDataMap().put("execHistory", masterExecHistory);
        context.getJobDetail().getJobDataMap().put("TBIBD760", masterExecHistory);


        JobKey jobKey = context.getJobDetail().getKey();
        log.info("jobToBeExecuted : jobKey : {}", jobKey);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        Trigger trigger = context.getTrigger();
        log.error("잡 실행이 거부되었습니다. : TRIGGER[{}], trigger group[{}], trigger name[{}] / JOB[{}]", trigger.getKey(),trigger.getKey().getGroup(), trigger.getKey().getName(), jobKey);

        // 신규 실행이력 저장
//        List<ExecProg> execProgList = (List<ExecProg>) context.getJobDetail().getJobDataMap().get("execProg");
        TBIBM710 tbibm710 = (TBIBM710) context.getJobDetail().getJobDataMap().get("TBIBM710");
        TBIBM713 tbibm713 = execProgService.findProgByProgId(tbibm710.getSettmWorkDvsCd(), tbibm710.getProgId());
//        ExecHistory masterExecHistory = ehSvc.insertStartExecHistory(context.getTrigger().getKey(), context.getJobDetail().getKey(), execProgList);
        TBIBD760 masterExecHistory = ehSvc.insertStartExecHistory(tbibm710, tbibm713);

        // 거부된 실행이력을 업데이트 한다.
//        masterExecHistory.setWorkResultCd("정산작업결과 상세 테이블(TBIBD750)에 등록 실패");
        masterExecHistory.setWorkDesc((String) context.getJobDetail().getJobDataMap().get("vetoJobRsltMsg"));
        masterExecHistory.setWorkResultCd((String) context.getJobDetail().getJobDataMap().get("vetoJobRsltMsg"));
        String jobEndDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        masterExecHistory.setWorkSttDtime(jobEndDtm);
        masterExecHistory.setUpdateId("quartz scheduler");
        masterExecHistory.setUpdateDtime(jobEndDtm);
        ehSvc.updateExecHistory(masterExecHistory);
    }

    /**
     * 잡이 실행 후 결과 실행이력을 저장한다.
     * 정상실행시 : JobExecutionException = null
     */
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        // 실행결과 DB Update
//        ExecHistory execHistory = (ExecHistory)context.getJobDetail().getJobDataMap().get("execHistory");
        TBIBD760 execHistory = (TBIBD760)context.getJobDetail().getJobDataMap().get("TBIBD760");
        String jobEndDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        System.out.println("완료후 이력 업데이트 : " + execHistory.getSettmJobGroupId() + "(" + execHistory.getProgId() +")");

        if(jobException == null){ // 정상실행이면


        }else{ // 에러발생이면
            execHistory.setWorkEndDtime(jobEndDtm);
            execHistory.setUpdateId("quartz scheduler");
            execHistory.setUpdateDtime(jobEndDtm);
            execHistory.setWorkResultCd("99");
            execHistory.setWorkDesc("정산작업결과 상세 테이블(TBIBD750)에 등록 실패");
        }

        ehSvc.updateExecHistory(execHistory);

        // 에러 발생시 Push 전송
//        if (jobException != null ) smSvc.notifySlack(new PushMessage(execHistory));

        JobKey jobKey = context.getJobDetail().getKey();
        log.info("jobWasExecuted : jobKey : {}", jobKey);
    }
}
