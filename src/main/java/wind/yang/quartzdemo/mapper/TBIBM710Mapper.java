package wind.yang.quartzdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import wind.yang.quartzdemo.dto.TBIBM710;

import java.util.List;

@Mapper
public interface TBIBM710Mapper {
    List<TBIBM710> findJobGroupList(@Param("workDvsCd") String workDvsCd);
}
