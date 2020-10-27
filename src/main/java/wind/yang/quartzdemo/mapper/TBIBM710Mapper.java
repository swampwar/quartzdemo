package wind.yang.quartzdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.quartz.TriggerKey;
import wind.yang.quartzdemo.dto.TBIBM710;

import java.util.List;

@Mapper
public interface TBIBM710Mapper {
    List<TBIBM710> findJobGroupList(@Param("workDvsCd") String workDvsCd);
    int countJobGroupByWorkDvsCd(@Param("workDvsCd") String workDvsCd);
    void insertJobGroup(TBIBM710 tbibm710);

    TBIBM710 findByTrigger(TriggerKey key);
}
