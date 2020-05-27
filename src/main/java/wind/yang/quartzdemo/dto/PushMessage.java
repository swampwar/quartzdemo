package wind.yang.quartzdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wind.yang.quartzdemo.code.JobExecutionStatusCode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PushMessage {
    // 웹푸쉬 전송 토큰
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
    String channel;

//    private String fcmToken;
//    private String title;
//    private String msg;

    public PushMessage(ExecHistory execHistory){
        this.jobGroup = execHistory.getJobGroup();
        this.jobName = execHistory.getJobName();
        this.execProgName = execHistory.getExecProgName();
        this.triggerGroup = execHistory.getTriggerGroup();
        this.triggerName = execHistory.getTriggerName();
        this.jobEndDtm = execHistory.getJobEndDtm();
        this.jobExecStaCd = execHistory.getJobExecStaCd();
        this.jobExecRslt = execHistory.getJobExecRslt();
    }

    public String toSimpleSlackTitle(){
        StringBuilder builder = new StringBuilder();
        builder.append("*[SLACK]").append(" 모니터링 결과*").append("\n").append("\n");
        return builder.toString();
    }

    public String toSimpleSlackMessage(){
        StringBuilder builder = new StringBuilder();
        builder.append("JobGroup     : ").append(getJobGroup()).append("\n")
                .append("JobName      : ").append(getJobName()).append("\n")
                .append("Trigger Group: ").append(getTriggerGroup()).append("\n")
                .append("Trigger Name : ").append(getTriggerName()).append("\n")
                .append("JobEndDtm    : ").append(getJobEndDtm()).append("\n")
                .append("JobResult    : ").append(getJobExecStaCd());
        return builder.toString();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class MessageEntity{
        private String text;
    }

    public MessageEntity getMessageEntity(){
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setText(this.toSimpleSlackTitle() + this.toSimpleSlackMessage());
        return messageEntity;
    }

}
