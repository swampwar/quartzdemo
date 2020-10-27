package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.TBIBM712;
import wind.yang.quartzdemo.mapper.TBIBM712Mapper;

@Service
@Slf4j
public class JobDetailService {
    @Autowired
    TBIBM712Mapper tbibm712Mapper;

    public boolean insertJob(TBIBM712 tbibm712) {
        boolean flag = true;

        log.debug("TBIBM712 insert data : {}", tbibm712.toString());

        try{
            tbibm712Mapper.insertJobDetail(tbibm712);
        }catch (Exception e) {
            flag =false;
        }

        return flag;
    }
}
