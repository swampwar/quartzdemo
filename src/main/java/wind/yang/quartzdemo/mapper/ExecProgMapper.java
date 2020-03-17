package wind.yang.quartzdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import wind.yang.quartzdemo.dto.ExecProg;

import java.util.List;

@Mapper
public interface ExecProgMapper {
    List<ExecProg> findByTrigger(String triggerGroup, String triggerName);
    void insertExecProg(ExecProg execProg);

    // TODO update, delete 추가
}
