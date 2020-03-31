package wind.yang.quartzdemo.dto;

import lombok.*;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.springframework.util.StringUtils;
import wind.yang.quartzdemo.code.JobExecutionStatusCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExecHistory {
    String triggerSttDtm;
    String jobSttDtm;
    String jobEndDtm;
    String triggerGroup;
    String triggerName;
    String jobGroup;
    String jobName;
    JobExecutionStatusCode jobExecStaCd;
    String execProgName;
    int execProgSeq;
    String jobExecRslt;

    /**
     * 신규 마스터 실행이력 생성
     */
    public static ExecHistory newMaster(TriggerKey triggerKey, JobKey jobKey){
        String sttDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return newExecHistory(triggerKey, jobKey, sttDtm, sttDtm, "", 0);
    }

    /**
     * 신규 상세 실행이력 생성
     */
    public static ExecHistory newDetail(ExecHistory master, String execProgName, int execProgSeq){
        TriggerKey triggerKey = new TriggerKey(master.getTriggerName(), master.getTriggerGroup());
        JobKey jobkey = new JobKey(master.getJobName(), master.getJobGroup());
        return newExecHistory(triggerKey, jobkey, master.getTriggerSttDtm(), null, execProgName, execProgSeq);
    }

    /**
     * 신규 실행이력 생성
     */
    public static ExecHistory newExecHistory(TriggerKey triggerKey, JobKey jobKey, String triggerSttDtm, String jobSttDtm, String execProgName, int execProgSeq){
        ExecHistory execHistory = new ExecHistory();
        execHistory.setTriggerSttDtm(triggerSttDtm);
        execHistory.setTriggerGroup(triggerKey.getGroup());
        execHistory.setTriggerName(triggerKey.getName());
        if(!StringUtils.isEmpty(jobSttDtm)) execHistory.setJobSttDtm(jobSttDtm);
        execHistory.setJobGroup(jobKey.getGroup());
        execHistory.setJobName(jobKey.getName());
        execHistory.setExecProgName(execProgName);
        execHistory.setExecProgSeq(execProgSeq);
        execHistory.setJobExecStaCd(JobExecutionStatusCode.READY);
        return execHistory;
    }
}
