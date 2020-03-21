package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.ExecProg;
import wind.yang.quartzdemo.mapper.ExecProgMapper;

import java.util.List;

@Service
@Slf4j
public class ExecProgService {
    @Autowired
    ExecProgMapper epMapper;

    public List<ExecProg> findByTrigger(TriggerKey key) {
        return epMapper.findByTrigger(key);
    }

    public List<ExecProg> findOriginalByTrigger(TriggerKey key) {
        String origName = key.getName().substring(0, key.getName().indexOf(".F."));
        log.debug("오리지날 트리거 조회를 위한 트리거 이름 : " + origName);

        return epMapper.findByTrigger(new TriggerKey(origName, key.getGroup()));
    }
}
