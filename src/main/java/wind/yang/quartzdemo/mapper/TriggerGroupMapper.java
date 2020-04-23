package wind.yang.quartzdemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import wind.yang.quartzdemo.dto.TriggerGroup;

import java.util.List;

@Mapper
public interface TriggerGroupMapper {
    List<TriggerGroup> findAll();
    TriggerGroup findAll(TriggerGroup triggerGroup);
    List<TriggerGroup> findByTriggerGroup(String triggerGroup);

    void insertTriggerGroup(TriggerGroup triggerGroup);
    void updateTriggerGroup(TriggerGroup triggerGroup);
    void deleteTriggerGroup(String triggerGroup);
}
