package wind.yang.quartzdemo.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import wind.yang.quartzdemo.quartz.SampleJobService;

@Slf4j
public class SampleSimpleJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("SampleSimpleJob : 저는 9초마다 실행됩니다.");
    }
}
