package wind.yang.quartzdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import wind.yang.quartzdemo.dto.ExecHistory;
import wind.yang.quartzdemo.dto.ExecProg;

import java.util.List;

@Mapper
public interface ExecHistoryMapper {
    List<ExecHistory> findByExecHistory(ExecHistory execHistory);
    List<ExecHistory> findAll();
    List<ExecHistory> findDetailAll(@Param("startDate") String startDate,
                                    @Param("endDate") String endDate,
                                    @Param("triggerGroup") String triggerGroup,
                                    @Param("triggerName") String triggerName);
    void insertExecHistory(ExecHistory execHistory);
    void updateExecHistory(ExecHistory execHistory);
    void deleteExecHistory(ExecHistory execHistory);
    ExecHistory findLastExecHistory(ExecHistory execHistory);
}
