package wind.yang.quartzdemo.runner;

import org.quartz.JobDetail;
import org.quartz.SchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;

@Component
public class TestRunner implements ApplicationRunner {
    @Autowired
    QuartzProperties quartzProperties;

    @Autowired
    JobDetail cronJobDetail;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("==========================================");
        System.out.println("SchedulerName : " + quartzProperties.getSchedulerName());
        System.out.println("JobStoreType : " + quartzProperties.getJobStoreType());
        System.out.println("InitializeSchema : " + quartzProperties.getJdbc().getInitializeSchema());
        quartzProperties.getProperties().forEach((k, v) -> System.out.println(k + " : " + v));
        System.out.println("==========================================");
    }
}
