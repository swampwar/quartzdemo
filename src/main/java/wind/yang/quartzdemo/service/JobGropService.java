package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.TBIBM710;
import wind.yang.quartzdemo.mapper.TBIBM710Mapper;

import javax.xml.ws.Action;
import java.util.List;

@Service
@Slf4j
public class JobGropService {
    @Autowired
    TBIBM710Mapper tbibm710Mapper;

    public List<TBIBM710> getJobGroupList(String workDvsCd) {
        // 작업그룹 리스트를 조회
        List<TBIBM710> jobGroupList = tbibm710Mapper.findJobGroupList(workDvsCd);

        return jobGroupList;
    }
}
