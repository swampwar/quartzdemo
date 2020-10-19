package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.JobSchedule;
import wind.yang.quartzdemo.mapper.TBFZD999Mapper;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class JobScheduleService {
    @Autowired
    TBFZD999Mapper tbfzd999Mapper;

    public int saveJobSchedule(JobSchedule jobSchedule) {
        // jobVerId, rgtDtm, udtDtm 등을 얻기 위함.
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String rgtDtm = format.format(new Date());
        String jobVerId = rgtDtm.substring(0, 10);
        jobVerId += countTodayJobSchedule(rgtDtm.substring(0,8));

        jobSchedule.setJobVerId(jobVerId);
        jobSchedule.setRgtDtm(rgtDtm);
        jobSchedule.setUdtDtm(rgtDtm);
        jobSchedule.setRgtId("qurtzScheduler");
        jobSchedule.setUdtId("qurtzScheduler");

        log.info("jobSchedule : {}", jobSchedule.toString());

        int result = 0;
        try {
            tbfzd999Mapper.insertJobSchedule(jobSchedule);
        }catch (Exception e){
            result = 1;
        }
        return result;
    }

    public String countTodayJobSchedule(String date) {
        String result = "";
        int cnt = tbfzd999Mapper.countJobSchedule(date);
        result = Integer.toString(cnt+1);
        if (cnt >= 100 && cnt < 1000) {
            result = "0" + result;
        }else if (cnt >= 10 && cnt < 100) {
            result = "00" + result;
        }else if (cnt < 10) {
            result = "000" + result;
        }

        return result;
    }

    public String dbtest() {
        String result = "";
        int cnt = tbfzd999Mapper.testDb();
        log.debug("popup cnt : " + cnt);
        System.out.println("popup cnt : " + cnt);

        return "popup cnt : " + cnt;
    }

}
