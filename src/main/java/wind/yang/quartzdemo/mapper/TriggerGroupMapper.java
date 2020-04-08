package wind.yang.quartzdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import wind.yang.quartzdemo.dto.TriggerGroup;

import java.util.List;

@Mapper
public interface TriggerGroupMapper {
    List<TriggerGroup> findAll();
    List<TriggerGroup> findByTriggerGroup(String triggerGroup);
}
