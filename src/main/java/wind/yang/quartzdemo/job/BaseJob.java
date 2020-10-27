package wind.yang.quartzdemo.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.DefaultExecutor;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import wind.yang.quartzdemo.code.JobExecutionStatusCode;
import wind.yang.quartzdemo.dto.*;
import wind.yang.quartzdemo.service.ExecHistoryService;
import wind.yang.quartzdemo.service.ExecProgService;
import wind.yang.quartzdemo.service.QuartzService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j
public abstract class BaseJob implements Job, InterruptableJob {
    @Value("${quartzdemo.shell.timeout}")
    protected int timeoutMillsecs;

    @Value("${quartzdemo.shell.cmd}")
    protected String CMD;

    @Value("${quartzdemo.shell.script-path}")
    protected String SH_PATH;

    @Value("${quartzdemo.shell.log-path}")
    protected String LOG_PATH;

    @Autowired
    QuartzService quartzSvc;

    @Autowired
    ExecHistoryService ehSvc;

    @Autowired
    ExecProgService execProgService;

    protected TriggerKey triggerKey = null;
    protected List<ExecProg> execProgList = null;
    protected TBIBM710 tbibm710 = null;
    protected TBIBM713 tbibm713 = null;
    protected TBIBD760 masterTBIBD760 = null;
    protected ExecHistory masterExecHistory = null;
    protected DefaultExecutor executor = null;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        this.triggerKey = context.getTrigger().getKey();
        this.execProgList = (List<ExecProg>) context.getJobDetail().getJobDataMap().get("execProg");
        this.masterExecHistory = (ExecHistory) context.getJobDetail().getJobDataMap().get("execHistory");
        String execSeqStr = (String) context.getTrigger().getJobDataMap().get("execSeq");
        this.tbibm710 = (TBIBM710) context.getJobDetail().getJobDataMap().get("TBIBM710");
        this.tbibm713 = execProgService.findProgByProgId(tbibm710.getSettmWorkDvsCd(), tbibm710.getProgId());
        this.masterTBIBD760 = (TBIBD760) context.getJobDetail().getJobDataMap().get("TBIBD760");
        log.debug("TBIBD760 : {}", masterTBIBD760);

//        for(ExecProg execProg : execProgList) {
//            // 상세이력 '시작' 업데이트
//            TBIBD760 detail = beforeExecuteJob(tbibm710, tbibm713, masterTBIBD760);
            log.info("Trigger[{}]에 의해 Job(seq={})을 실행시작", triggerKey, tbibm710.getWorkSeq());
//
//            // 실행시퀀스(execSeq)가 세팅되어 있으면 해당 시퀀스만 실행한다.
//            if(!validExecSeq(execProg.getSeq(), execSeqStr)){
//                // 실행시퀀스가 세팅되었으나 해당되는 시퀀스가 아닌경우 '생략' 업데이트
//                afterExecuteJob(detail, JobExecutionStatusCode.SKIP, JobExecutionStatusCode.SKIP.getMsg());
//                continue;
//            }

            try {
                executeInternal(masterTBIBD760);
            } catch (JobExecutionException e) {
                // 상세이력 '에러' 업데이트
                afterExecuteJob(masterTBIBD760, JobExecutionStatusCode.ERROR, e.getMessage());
                throw e;
            }

            log.info("Trigger[{}]에 의해 Job(seq={})이 실행종료", triggerKey, tbibm710.getWorkSeq());

            // 상세이력 '성공' 업데이트
            afterExecuteJob(masterTBIBD760, JobExecutionStatusCode.SUCCESS, "정산작업결과상세 테이블(TBIBD760) 등록 완료전. 지역배치 데몬 작업 대기중...");
//        }
    }

    private boolean validExecSeq(int seq, String execSeqStr){
        log.debug("시퀀스를 검사합니다. execSeqStr = [{}], seq = {}", execSeqStr, seq);
        if(StringUtils.isEmpty(execSeqStr)){
            return true;
        }else{
            String[] execSeqArr = execSeqStr.split(",");
            return Arrays.stream(execSeqArr)
                         .anyMatch(seqStr -> Integer.parseInt(seqStr) == seq);
        }
    }

//    private ExecHistory beforeExecuteJob(ExecProg execProg) {
//        // 상세이력 '시작' 업데이트
//        String sttDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//        ExecHistory detail = ExecHistory.newDetail(execProg, masterExecHistory);
//        detail.setJobSttDtm(sttDtm);
//        detail.setJobExecStaCd(JobExecutionStatusCode.START);
//        detail.setJobExecRslt(JobExecutionStatusCode.START.getMsg());
//        ehSvc.updateExecHistory(detail);
//
//        return detail;
//    }

    private TBIBD760 beforeExecuteJob(TBIBM710 tbibm710, TBIBM713 tbibm713, TBIBD760 tbibd760) {
        // 상세이력 '시작' 업데이트
//        String sttDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        TBIBD760 detail = TBIBD760.newDetail(tbibm710, tbibm713, tbibd760);
//        detail.setWorkDesc("정산작업결과상세 테이블(TBIBD760) 등록 ");
        ehSvc.updateExecHistory(detail);

        return detail;
    }

//    private void afterExecuteJob(ExecHistory detail, JobExecutionStatusCode code, String rslt) {
//        // 상세이력 '성공'/'에러' 업데이트
//        String endDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//        detail.setJobEndDtm(endDtm);
//        detail.setJobExecStaCd(code);
//        detail.setJobExecRslt(rslt);
//        ehSvc.updateExecHistory(detail);
//    }

    private void afterExecuteJob(TBIBD760 detail, JobExecutionStatusCode code, String rslt) {
        // 상세이력 '성공'/'에러' 업데이트
//        detail.setWorkDesc(rslt);
        String updateDtime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        detail.setUpdateId("quartz scheduler");
        detail.setUpdateDtime(updateDtime);
        ehSvc.updateExecHistory(detail);
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        if(executor != null){
            executor.getWatchdog().destroyProcess(); // 자식 프로세스 종료
            log.info("Trigger[{}] 강제종료가 완료되었습니다.", triggerKey);
        }else{
            log.error("Trigger[{}] 강제종료를 할 수 없습니다.", triggerKey);
            throw new UnableToInterruptJobException("Trigger에 대한 강제종료를 할 수 없습니다. " + triggerKey.getName() + ", " +triggerKey.getGroup());
        }
    }

//    protected abstract void executeInternal(ExecProg execProg) throws JobExecutionException;
    protected abstract void executeInternal(TBIBD760 tbibd760) throws JobExecutionException;
}
