package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.ExecProg;
import wind.yang.quartzdemo.dto.JobRequest;
import wind.yang.quartzdemo.dto.JobResponse;
import wind.yang.quartzdemo.dto.TriggerJobInfo;

import java.text.ParseException;
import java.util.List;

@Service
@Slf4j
public class TriggerDetailService {
    @Autowired
    ExecProgService execProgService;

    @Autowired
    QuartzService quartzService;

    public TriggerJobInfo getTriggerJobInfo(String triggerGroup, String triggerName) {
        TriggerJobInfo triggerJobInfo = new TriggerJobInfo();
        JobResponse jobResponse = quartzService.readTrigger(triggerGroup, triggerName);
        List<ExecProg> execProgList = execProgService.findByTrigger(new TriggerKey(triggerName, triggerGroup));

        triggerJobInfo = triggerJobInfo.builder()
                .triggerGroup(jobResponse.getTriggerGroup())
                .triggerName1(jobResponse.getTriggerName())
                .triggerDescription(jobResponse.getDescription())
                .cronExpression(jobResponse.getCronExpression())
                .execProgInfoList(execProgList)
                .build();

        return triggerJobInfo;
    }

    public boolean insertTriggerDetail(TriggerJobInfo triggerJobInfo) {
        String triggerGroup = triggerJobInfo.getTriggerGroup();
        String triggerName = "";
        if("".equals(triggerJobInfo.getTriggerName1())) {
            triggerName = triggerJobInfo.getTriggerName2();
        }else {
            triggerName = triggerJobInfo.getTriggerName1();
        }
        TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);

        boolean flag = true;

        try {
            // 기존에 없는 trigger인 경우 추가해준다.
            if (quartzService.readTrigger(triggerGroup, triggerName) == null) {
                JobRequest jobRequest = new JobRequest();
                jobRequest = jobRequest.builder()
                    .triggerGroup(triggerGroup)
                    .triggerName(triggerName)
                    .cronExpression(triggerJobInfo.getCronExpression())
                    .triggerDescription(triggerJobInfo.getTriggerDescription())
                    .build();

                log.debug("jobRequest : {}" , jobRequest.toString());

                quartzService.createJob(jobRequest);
            }
            // 기존에 있는 trigger인 경우 update해준다.
            else {
                quartzService.update(triggerKey, triggerJobInfo.getCronExpression());
            }

            // trigger에 등록된 기존 execProg를 전부 삭제한다.
            execProgService.deleteExecProg(triggerKey);

            // 이후 TriggerJobInfo의 execProgList의 내용들을 전부 insert한다.
            List<ExecProg> execProgList = triggerJobInfo.getExecProgInfoList();
            for (ExecProg execProg : execProgList) {
             // execProgList의 triggerGroup, triggerName은 전부 null로 넘어오니 세팅해준다.
                execProg.setTriggerGroup(triggerGroup);
                execProg.setTriggerName(triggerName);

                execProgService.insertExecProg(execProg);
            }

        }catch (Exception e) {
             e.printStackTrace();
             flag = false;
        }
        return flag;
    }

}
