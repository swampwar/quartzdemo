package wind.yang.quartzdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import wind.yang.quartzdemo.dto.ExecHistory;
import wind.yang.quartzdemo.dto.ExecProg;

import java.util.List;

@Mapper
public interface ExecHistoryMapper {
    List<ExecHistory> findByExecHistory(ExecHistory execHistory);
    List<ExecHistory> findAll();
    void insertExecHistory(ExecHistory execHistory);
    void updateExecHistory(ExecHistory execHistory);
    void deleteExecHistory(ExecHistory execHistory);
    ExecHistory findLastExecHistory(ExecHistory execHistory);
}
