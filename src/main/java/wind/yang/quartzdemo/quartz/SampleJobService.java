package wind.yang.quartzdemo.quartz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
public class SampleJobService {
    public void executeSampleJob(){
        log.info("SampleJobService executed !!");
    }
}
