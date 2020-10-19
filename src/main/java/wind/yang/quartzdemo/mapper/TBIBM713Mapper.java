package wind.yang.quartzdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import wind.yang.quartzdemo.dto.TBIBM713;

import java.util.List;

@Mapper
public interface TBIBM713Mapper {
    List<TBIBM713> findProgList(@Param("workDvsCd") String workDvsCd);
    int countProgByWorkDvsCd(@Param("workDvsCd") String workDvsCd);

    void insertProgDetail(TBIBM713 TBIBM713);
}
