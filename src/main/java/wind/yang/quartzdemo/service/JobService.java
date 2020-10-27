package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.TBIBM711;
import wind.yang.quartzdemo.mapper.TBIBM711Mapper;

import java.util.List;

@Service
@Slf4j
public class JobService {
    @Autowired
    TBIBM711Mapper tbibm711Mapper;

    public List<TBIBM711> getJobList(String workDvsCd, String jobGroupId) {
        //
        List<TBIBM711> jobList = tbibm711Mapper.findJobList(workDvsCd, jobGroupId);

        for (int i  = 0; i < jobList.size(); i++) {
            TBIBM711 tbibm711 = jobList.get(i);
            log.debug(tbibm711.toString());
        }

        return jobList;
    }

    public String makeJobId(String workDvsCd) {
        String jobGroupId = "J_" + workDvsCd + "_";
        int jobGroupCnt = tbibm711Mapper.countJobByWorkDvsCd(workDvsCd);
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

    public boolean insertJob(TBIBM711 tbibm711) {
        boolean flag = true;

        log.debug("TBIBM711 insert data : {}", tbibm711.toString());

        try{
            tbibm711Mapper.insertJob(tbibm711);
        }catch (Exception e) {
            flag =false;
        }

        return flag;
    }
}
