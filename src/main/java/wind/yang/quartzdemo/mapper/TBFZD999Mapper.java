package wind.yang.quartzdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import wind.yang.quartzdemo.dto.JobSchedule;

@Mapper
public interface TBFZD999Mapper {
    void insertJobSchedule(JobSchedule jobSchedule);
    int countJobSchedule(@Param("date") String date);

    int testDb();
}
