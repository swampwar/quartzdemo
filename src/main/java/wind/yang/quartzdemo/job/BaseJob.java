package wind.yang.quartzdemo.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.DefaultExecutor;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import wind.yang.quartzdemo.code.JobExecutionStatusCode;
import wind.yang.quartzdemo.dto.ExecHistory;
import wind.yang.quartzdemo.dto.ExecProg;
import wind.yang.quartzdemo.service.ExecHistoryService;
import wind.yang.quartzdemo.service.QuartzService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    protected TriggerKey triggerKey = null;
    protected List<ExecProg> execProgList = null;
    protected ExecHistory masterExecHistory = null;
    protected DefaultExecutor executor = null;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        this.triggerKey = context.getTrigger().getKey();
        this.execProgList = (List<ExecProg>) context.getJobDetail().getJobDataMap().get("execProg");
        this.masterExecHistory = (ExecHistory) context.getJobDetail().getJobDataMap().get("execHistory");

        for(ExecProg execProg : execProgList) {
            // 상세이력 '시작' 업데이트
            ExecHistory detail = beforeExecuteJob(execProg);

            log.info("Trigger[{}]에 의해 Job(seq={})을 실행시작", triggerKey, execProg.getSeq());

            try {
                executeInternal(execProg);
            } catch (JobExecutionException e) {
                // 상세이력 '에러' 업데이트
                afterExecuteJob(detail, JobExecutionStatusCode.ERROR, e.getMessage());
                throw e;
            }

            log.info("Trigger[{}]에 의해 Job(seq={})이 실행종료", triggerKey, execProg.getSeq());

            // 상세이력 '성공' 업데이트
            afterExecuteJob(detail, JobExecutionStatusCode.SUCCESS, JobExecutionStatusCode.SUCCESS.getMsg());
        }
    }

    private ExecHistory beforeExecuteJob(ExecProg execProg) {
        // 상세이력 '시작' 업데이트
        String sttDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        ExecHistory detail = ExecHistory.newDetail(masterExecHistory, execProg.getProgramName(), execProg.getSeq());
        detail.setJobSttDtm(sttDtm);
        detail.setJobExecStaCd(JobExecutionStatusCode.START);
        detail.setJobExecRslt(JobExecutionStatusCode.START.getMsg());
        ehSvc.updateExecHistory(detail);

        return detail;
    }

    private void afterExecuteJob(ExecHistory detail, JobExecutionStatusCode code, String rslt) {
        // 상세이력 '성공'/'에러' 업데이트
        String endDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        detail.setJobEndDtm(endDtm);
        detail.setJobExecStaCd(code);
        detail.setJobExecRslt(rslt);
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

    protected abstract void executeInternal(ExecProg execProg) throws JobExecutionException;
}
