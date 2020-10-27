package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.TBIBM710;
import wind.yang.quartzdemo.mapper.TBIBM710Mapper;
import java.util.List;

@Service
@Slf4j
public class JobGropService {
    @Autowired
    TBIBM710Mapper tbibm710Mapper;

    public List<TBIBM710> getJobGroupList(String workDvsCd) {
        // 작업그룹 리스트를 조회
        List<TBIBM710> jobGroupList = tbibm710Mapper.findJobGroupList(workDvsCd);

        for (int i  = 0; i < jobGroupList.size(); i++) {
            TBIBM710 tbibm710 = jobGroupList.get(i);
            log.debug(tbibm710.toString());
        }

        return jobGroupList;
    }

    public String makeJobGroupId(String workDvsCd) {
        String jobGroupId = "S_" + workDvsCd + "_";
        int jobGroupCnt = tbibm710Mapper.countJobGroupByWorkDvsCd(workDvsCd);
        if (jobGroupCnt < 9) {
            jobGroupId += "000" + (jobGroupCnt + 1);
        } else if (jobGroupCnt < 99) {
            jobGroupId += "00" + (jobGroupCnt + 1);
        } else if (jobGroupCnt < 999) {
            jobGroupId += "0" + (jobGroupCnt + 1);
        } else {
            jobGroupId += (jobGroupCnt + 1);
        }

        log.debug("jobGroupId  : {}", jobGroupId);
        return jobGroupId;
    }

    public boolean insertJobGroup(TBIBM710 tbibm710) {
        boolean flag = true;

        log.debug("TBIBM710 insert data : {}", tbibm710.toString());

        try{
            tbibm710Mapper.insertJobGroup(tbibm710);
        }catch (Exception e) {
            flag =false;
        }

        return flag;
    }

    public TBIBM710 findByTrigger(TriggerKey key) {
        return tbibm710Mapper.findByTrigger(key);
    }
}
