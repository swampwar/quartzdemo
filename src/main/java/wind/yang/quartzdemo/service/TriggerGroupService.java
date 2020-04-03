package wind.yang.quartzdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.TriggerGroup;
import wind.yang.quartzdemo.mapper.TriggerGroupMapper;

import java.util.List;

@Service
public class TriggerGroupService {
    @Autowired
    TriggerGroupMapper triggerGroupMapper;

    public List<TriggerGroup> findAllTriggerGroup() {return triggerGroupMapper.findAll();}

    public  TriggerGroup findByTriggerGroup(String triggerGroup) {return triggerGroupMapper.findByTriggerGroup(triggerGroup);}
}
