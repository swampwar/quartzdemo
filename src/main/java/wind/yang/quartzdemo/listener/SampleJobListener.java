package wind.yang.quartzdemo.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wind.yang.quartzdemo.dto.ExecHistory;
import wind.yang.quartzdemo.dto.ExecProg;
import wind.yang.quartzdemo.mapper.ExecHistoryMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class SampleJobListener implements JobListener {
    @Autowired
    ExecHistoryMapper mapper;

    @Override
    public String getName() {
        return "SampleJobListener";
    }

    /**
     * 잡이 실행 전 시작 실행이력을 저장한다.
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        // TODO Job 연속실행시 수정되어야 함
        // 신규 실행이력 저장
        ExecHistory execHistory = newExecHistory(context);
        mapper.insertExecHistory(execHistory);

        // 실행종료 후 이력 업데이트용으로 JobDataMap에 넣는다.
        context.getJobDetail().getJobDataMap().put("execHistory", execHistory);

        JobKey jobKey = context.getJobDetail().getKey();
        log.info("jobToBeExecuted : jobKey : {}", jobKey);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        log.info("jobExecutionVetoed : jobKey : {}", jobKey);
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
        if(jobException == null){ // 정상실행이면
            execHistory.setJobExecStaCd("S");
            execHistory.setJobExecRslt("잘 실행되었다~ :)");
        }else{ // 에러발생이면
            execHistory.setJobExecStaCd("E");
            execHistory.setJobExecRslt(jobException.getMessage());
        }
        execHistory.setJobEndDtm(jobEndDtm);
        mapper.updateExecHistory(execHistory);

        JobKey jobKey = context.getJobDetail().getKey();
        log.info("jobWasExecuted : jobKey : {}", jobKey);
    }

    /**
     * 신규 실행이력 생성
     *
     * @return 신규 실행이력 ExecHistory
     */
    private ExecHistory newExecHistory(JobExecutionContext ctx){
        String jobSttDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        ExecHistory execHistory = new ExecHistory();
        execHistory.setJobSttDtm(jobSttDtm);
        execHistory.setTriggerGroup(ctx.getTrigger().getKey().getGroup());
        execHistory.setTriggerName(ctx.getTrigger().getKey().getName());
        execHistory.setJobGroup(ctx.getJobDetail().getKey().getGroup());
        execHistory.setJobName(ctx.getJobDetail().getKey().getName());
        execHistory.setExecProgName(((ExecProg)ctx.getJobDetail().getJobDataMap().get("execProg")).getProgramName());
        execHistory.setJobExecStaCd("P"); // TODO 나중에 코드값으로
        return execHistory;
    }
}
