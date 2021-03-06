package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.TriggerGroup;
import wind.yang.quartzdemo.mapper.TriggerGroupMapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TriggerGroupService {
    @Autowired
    TriggerGroupMapper triggerGroupMapper;

    public List<TriggerGroup> findAllTriggerGroup() {return triggerGroupMapper.findAll();}

    public TriggerGroup findAllTriggerGroup(String triggerGroup) {
        log.debug("findAllTriggerGroup(String triggerGroup) 호출 / triggerGroup : {}", triggerGroup);
        TriggerGroup tg = new TriggerGroup();
        tg.setTriggerGroup(triggerGroup);
        return triggerGroupMapper.findAll(tg);
    }

    public List<TriggerGroup> findByTriggerGroup(String triggerGroup) {return triggerGroupMapper.findByTriggerGroup(triggerGroup);}

    public Map<String, Object> saveTriggerGroup(TriggerGroup triggerGroup) {
        Map<String, Object> map = new HashMap<>();

        // 기존에 등록된 트리거 그룹인지 확인
        TriggerGroup tg = findAllTriggerGroup(triggerGroup.getTriggerGroup());
        if(tg != null) {
            // 기존에 등록된 트리거 그룹이므로 업데이트한다.
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String udtDtm = format.format(new Date());
            tg.setDescription(triggerGroup.getDescription());
            tg.setUseYn(triggerGroup.getUseYn());
            tg.setUdtDtm(udtDtm);

            triggerGroupMapper.updateTriggerGroup(tg);

            map.put("msg", "트리거 그룹 수정을 완료했습니다.");
            map.put("triggerGroup", tg);

        }else {
            // 기존에 등록되지 않은 트리거 그룹이므로 인서트한다.
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String rgtDtm = format.format(new Date());
            triggerGroup.setRgtDtm(rgtDtm);
            triggerGroup.setUdtDtm(rgtDtm);

            int lastSeq = triggerGroupMapper.getLastSeq()+1;
            triggerGroup.setPrmtSeq(lastSeq);
            triggerGroup.setPrstId(makePrstId(lastSeq));
            triggerGroup.setPrmtId(makePrmtId(lastSeq));

            triggerGroupMapper.insertTriggerGroup(triggerGroup);

            map.put("msg", "트리거 그룹 신규 등록을 완료했습니다.");
            map.put("triggerGroup", triggerGroup);
        }

        return map;
    }

    public boolean deleteTriggerGroup(String triggerGroup) {
        boolean flag = true;

        try {
            triggerGroupMapper.deleteTriggerGroup(triggerGroup);
        }catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public String makePrstId(int lastSeq) {
        String PrstId = "PS_DVS_";

        if (lastSeq < 9) {
            PrstId += "000" + (lastSeq + 1);
        } else if (lastSeq < 99) {
            PrstId += "00" + (lastSeq + 1);
        } else if (lastSeq < 999) {
            PrstId += "0" + (lastSeq + 1);
        } else {
            PrstId += (lastSeq + 1);
        }

        log.debug("PrstId  : {}", PrstId);
        return PrstId;
    }

    private String makePrmtId(int lastSeq) {
        String PrmtId = "PI_DVS";

        if (lastSeq < 9) {
            PrmtId += "000" + (lastSeq + 1);
        } else if (lastSeq < 99) {
            PrmtId += "00" + (lastSeq + 1);
        } else if (lastSeq < 999) {
            PrmtId += "0" + (lastSeq + 1);
        } else {
            PrmtId += (lastSeq + 1);
        }

        log.debug("PrmtId  : {}", PrmtId);
        return PrmtId;
    }
}
