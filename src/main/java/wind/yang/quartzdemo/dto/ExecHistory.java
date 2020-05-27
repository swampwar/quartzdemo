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
@EqualsAndHashCode
@ToString
@Builder
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

    String execParam1;
    String execParam2;
    String execParam3;
    String execCmd;
    String summary;

    /**
     * 신규 마스터 실행이력 생성
     */
    public static ExecHistory newMaster(TriggerKey triggerKey, JobKey jobKey){
        String sttDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        String triggerName = triggerKey.getName();
        // FORCE인 경우 이력 등록시 원래 트리거 이름으로 마스터 저장
        if(triggerName.indexOf(".") != -1) {
            System.out.println(triggerName);
            triggerName = triggerName.substring(0, triggerName.indexOf("."));
            System.out.println(triggerName);
        }

        ExecHistory master = newExecHistory(new ExecProg(triggerKey.getGroup(), triggerName, 0), jobKey, sttDtm, sttDtm);
        master.setJobExecStaCd(JobExecutionStatusCode.START);
        return master;
    }

    /**
     * 신규 상세 실행이력 생성
     */
    public static ExecHistory newDetail(ExecProg execProg, ExecHistory master){
        JobKey jobkey = new JobKey(master.getJobName(), master.getJobGroup());
        return newExecHistory(execProg, jobkey, master.getTriggerSttDtm(),null);
    }

    /**
     * 신규 실행이력 생성
     */
    public static ExecHistory newExecHistory(ExecProg execProg, JobKey jobKey, String triggerSttDtm, String jobSttDtm){
        ExecHistory execHistory = ExecHistory.of(execProg);
        execHistory.setTriggerSttDtm(triggerSttDtm);
        if(!StringUtils.isEmpty(jobSttDtm)) execHistory.setJobSttDtm(jobSttDtm);
        execHistory.setJobGroup(jobKey.getGroup());
        execHistory.setJobName(jobKey.getName());
        return execHistory;
    }

    public static ExecHistory of(ExecProg execProg) {
        return ExecHistory.builder()
                .triggerGroup(execProg.getTriggerGroup())
                .triggerName(execProg.getTriggerName())
                .execProgSeq(execProg.getSeq())
                .execProgName(execProg.getProgramName())
                .execParam1(execProg.getExecParam1())
                .execParam2(execProg.getExecParam2())
                .execParam3(execProg.getExecParam3())
                .summary(execProg.getSummary())
                .jobExecStaCd(JobExecutionStatusCode.READY)
                .build();
    }

}
