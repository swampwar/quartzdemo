package wind.yang.quartzdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.quartz.TriggerKey;
import wind.yang.quartzdemo.dto.ExecProg;

import java.util.List;

@Mapper
public interface ExecProgMapper {
    List<ExecProg> findByTrigger(TriggerKey key);
    ExecProg findOneByTrigger(@Param("key") TriggerKey key, @Param("seq") int seq);
    void insertExecProg(ExecProg execProg);

    // TODO update, delete 추가
}
