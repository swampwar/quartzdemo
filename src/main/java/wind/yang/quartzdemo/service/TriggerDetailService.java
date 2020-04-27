package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.ExecProg;
import wind.yang.quartzdemo.dto.JobResponse;
import wind.yang.quartzdemo.dto.TriggerJobInfo;

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
                .triggerName(jobResponse.getTriggerName())
                .triggerDescription(jobResponse.getDescription())
                .cronExpression(jobResponse.getCronExpression())
                .execProgInfoList(execProgList)
                .build();

        return triggerJobInfo;
    }



}
