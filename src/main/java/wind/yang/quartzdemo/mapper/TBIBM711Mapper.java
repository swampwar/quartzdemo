package wind.yang.quartzdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import wind.yang.quartzdemo.dto.TBIBM711;

import java.util.List;

@Mapper
public interface TBIBM711Mapper {
    List<TBIBM711> findJobList(@Param("workDvsCd") String workDvsCd, @Param("jobGroupId") String jobGroupId);

    int countJobByWorkDvsCd(String workDvsCd);

    void insertJob(TBIBM711 tbibm711);
}
