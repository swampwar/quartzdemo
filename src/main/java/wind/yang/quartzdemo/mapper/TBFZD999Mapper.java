package wind.yang.quartzdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import wind.yang.quartzdemo.dto.TBIBD760;

@Mapper
public interface TBFZD999Mapper {
    void insertJobSchedule(TBIBD760 tbibd760);
    int countJobSchedule(@Param("date") String date);

    int testDb();
}
