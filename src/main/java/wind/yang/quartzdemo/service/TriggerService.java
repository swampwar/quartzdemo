package wind.yang.quartzdemo.service;

import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.TriggerInfo;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class TriggerService {
    private Map<String, TriggerInfo> triggers = new HashMap<>();

    @PostConstruct
    public void init(){
        triggers.put("MGT.MGT_TRIGGER1",
                new TriggerInfo("MGT", "MGT_TRIGGER1", "NORMAL")
        );
    }

    public TriggerInfo getTrigger(TriggerKey triggerKey) {
        return triggers.get(triggerKey.getGroup() + "." + triggerKey.getName());
    }

    public void createTriggerInfo(TriggerInfo triggerInfo) {
        // TODO DB저장으로 변경필요
        triggers.put(triggerInfo.getTriggerGroup() + triggerInfo.getTriggerName(), triggerInfo);

    }

    /**
     * 강제실행을 위한 트리거를 등록한다.
     * 강제실행 트리거의 Group : "FORCE"
     * 강제실행 트리거의 NAME : triggerKey.group + '.' triggerKey.name + '.' + YYYYMMDDHH24MISS
     *
     * @param origTriggerKey : 강제실행할 오리지날 트리거의 키
     */
    public void createForceTriggerInfo(TriggerKey origTriggerKey){
        TriggerKey fTriggerKey = new TriggerKey(origTriggerKey.getGroup() + "."
                + origTriggerKey.getName() + "."
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        // TODO 트리거유형 코드화
        createTriggerInfo(new TriggerInfo(fTriggerKey.getGroup(), fTriggerKey.getName(), "FORCE"));
    }


}
