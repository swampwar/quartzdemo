package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.ExecProg;
import wind.yang.quartzdemo.dto.ProgDetailInfo;
import wind.yang.quartzdemo.dto.TBIBM713;
import wind.yang.quartzdemo.mapper.ExecProgMapper;
import wind.yang.quartzdemo.mapper.TBIBM713Mapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ExecProgService {
    @Autowired
    ExecProgMapper epMapper;

    @Autowired
    TBIBM713Mapper TBIBM713Mapper;

    public List<ExecProg> findByTrigger(TriggerKey key) {
        return epMapper.findByTrigger(key);
    }

    public List<ExecProg> findOriginalByTrigger(TriggerKey key) {
        String origName = key.getName().substring(0, key.getName().indexOf(".F."));
        log.debug("오리지날 트리거 조회를 위한 트리거 이름 : " + origName);

        return epMapper.findByTrigger(new TriggerKey(origName, key.getGroup()));
    }

    public List<ExecProg> findByTriggerGroup(String triggerGroup) { return epMapper.findByTriggerGroup(triggerGroup);}

    public void insertExecProg(ExecProg execProg) {epMapper.insertExecProg(execProg);}
    public void deleteExecProg(TriggerKey key) {epMapper.deleteExecProg(key);}

    public List<TBIBM713> findProgList(String workDcsCd) {
        List<TBIBM713> tbibm713List = TBIBM713Mapper.findProgList(workDcsCd);

        for (int i  = 0; i < tbibm713List.size(); i++) {
            TBIBM713 TBIBM713 = tbibm713List.get(i);
            log.debug(TBIBM713.toString());
        }
        return TBIBM713Mapper.findProgList(workDcsCd);
    }

    public List<TBIBM713> insertProgDetail(ProgDetailInfo progDetailInfo) {
        List<TBIBM713> progList = progDetailInfo.getExecProgInfoList();

        int successCnt = 0;
        List<TBIBM713> insertFailList = new ArrayList<>();
        for (int i = 0 ; i < progList.size(); i ++) {
            TBIBM713 TBIBM713 = progList.get(i);
            try {

                String progId = "P_" + TBIBM713.getWorkDvsCd() + "_";
                int progCnt = TBIBM713Mapper.countProgByWorkDvsCd(TBIBM713.getWorkDvsCd());
                if (progCnt < 9) {
                    progId += "00" + (progCnt + 1);
                } else if (progCnt < 99) {
                    progId += "0" + (progCnt + 1);
                } else {
                    progId += (progCnt + 1);
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                Date time = new Date();

                TBIBM713.setProgId(progId);
                TBIBM713.setRegistId("Quartz Scheduler");
                TBIBM713.setRegistDtime(format.format(time));
                TBIBM713.setUpdateId("Quartz Scheduler");
                TBIBM713.setUpdateDtime(format.format(time));

                TBIBM713Mapper.insertProgDetail(TBIBM713);
                successCnt++;
            }catch (Exception e) {
                log.error("{} TBFZD933 insert 실패", TBIBM713.getProgId());
                insertFailList.add(TBIBM713);
            }
        }
        return insertFailList;
    }
}
