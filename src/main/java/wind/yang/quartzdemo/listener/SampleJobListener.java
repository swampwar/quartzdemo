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

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        // TODO Job 연속실행시 수정되어야 함
        String jobSttDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        ExecHistory execHistory = new ExecHistory();
        execHistory.setJobSttDtm(jobSttDtm);
        execHistory.setTriggerGroup(context.getTrigger().getKey().getGroup());
        execHistory.setTriggerName(context.getTrigger().getKey().getName());
        execHistory.setJobGroup(context.getJobDetail().getKey().getGroup());
        execHistory.setJobName(context.getJobDetail().getKey().getName());
        execHistory.setExecProgName(((ExecProg)context.getJobDetail().getJobDataMap().get("execProg")).getProgramName());
        execHistory.setJobExecStaCd("P"); // TODO 나중에 코드로
        mapper.insertExecHistory(execHistory);

        // 실행 결과를 업데이트 하기위해 map에 넣는다.
        context.getJobDetail().getJobDataMap().put("execHistory", execHistory);

        JobKey jobKey = context.getJobDetail().getKey();
        log.info("jobToBeExecuted : jobKey : {}", jobKey);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        log.info("jobExecutionVetoed : jobKey : {}", jobKey);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        // 실행결과 DB Update
        // TODO Exception시 E 처리
        String jobEndDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        ExecHistory execHistory = (ExecHistory)context.getJobDetail().getJobDataMap().get("execHistory");
        execHistory.setJobEndDtm(jobEndDtm);
        execHistory.setJobExecStaCd("S");
        execHistory.setJobExecRslt("잘 끝남!");
        mapper.updateExecHistory(execHistory);

        JobKey jobKey = context.getJobDetail().getKey();
        log.info("jobWasExecuted : jobKey : {}", jobKey);
    }
}
