package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.TBIBD760;
import wind.yang.quartzdemo.mapper.TBFZD999Mapper;
import wind.yang.quartzdemo.mapper.TBIBD760Mapper;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class JobScheduleService {
    @Autowired
    TBFZD999Mapper tbfzd999Mapper;

    @Autowired
    TBIBD760Mapper tbibd760Mapper;

    public int saveJobSchedule(TBIBD760 tbibd760) {
        // jobVerId, rgtDtm, udtDtm 등을 얻기 위함.
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String rgtDtm = format.format(new Date());

        tbibd760.setUpdateDtime(rgtDtm);
        tbibd760.setRegistDtime(rgtDtm);
        tbibd760.setUpdateId("qurtzScheduler");
        tbibd760.setRegistId("qurtzScheduler");

        log.info("jobSchedule : {}", tbibd760.toString());

        int result = 0;
        try {
            tbfzd999Mapper.insertJobSchedule(tbibd760);
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

    public int checkJobHistroy(TBIBD760 tbibd760) {
        int rslt = tbibd760Mapper.checkJobHistory(tbibd760);
        if (rslt == 1) {
            return 0;
        }else {
            return -1;
        }
    }

    public String dbtest() {
        String result = "";
        int cnt = tbfzd999Mapper.testDb();
        log.debug("popup cnt : " + cnt);
        System.out.println("popup cnt : " + cnt);

        return "popup cnt : " + cnt;
    }

}
