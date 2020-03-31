package wind.yang.quartzdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.*;
import wind.yang.quartzdemo.job.ShellExecJob;
import wind.yang.quartzdemo.quartz.AutoWiringSpringBeanJobFactory;
import wind.yang.quartzdemo.job.SampleCronJob;
import wind.yang.quartzdemo.job.SampleSimpleJob;
import wind.yang.quartzdemo.service.ExecProgService;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.stream.IntStream;

@Slf4j
@Configuration
public class QuartzConfig {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    QuartzProperties quartzProperties;

    @Autowired
    TriggerListener triggerListener;

    @Autowired
    JobListener jobListener;

    @Autowired
    ExecProgService epsvc;

    @Bean(name="cronJobDetail") // TODO FactoryBean의 장점? 이유?
    public JobDetailFactoryBean cronJobDetail() { // Spring 스타일의 JobDetail 빈등록
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(ShellExecJob.class); // 실행대상 Job의 Class 설정
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setApplicationContext(applicationContext);
        jobDetailFactory.setName("DEFUALT_JOB");
        jobDetailFactory.setGroup("DEFAULT_GROUP");
        jobDetailFactory.setDescription("디폴트 크론잡");

        return jobDetailFactory;
    }

    @Bean
    public CronTriggerFactoryBean cronTrigger1(@Qualifier("cronJobDetail") JobDetail cronJobDetail) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setName("MGT_TRIGGER1");
        factoryBean.setGroup("MGT");
        factoryBean.setDescription("관리 크론트리거1");
        factoryBean.setCronExpression("0 10 * * * ?"); // 매분 n초마다 실행
        factoryBean.setJobDetail(cronJobDetail);
        factoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
        return factoryBean;
    }

    @Bean
    public CronTriggerFactoryBean cronTrigger2(@Qualifier("cronJobDetail") JobDetail cronJobDetail) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setName("MGT_TRIGGER2");
        factoryBean.setGroup("MGT");
        factoryBean.setDescription("관리 크론트리거2");
        factoryBean.setCronExpression("0 10 * * * ?"); // 매분 n초마다 실행
        factoryBean.setJobDetail(cronJobDetail);
        factoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
        return factoryBean;
    }

    @Bean(name = "qScheduler")
    public SchedulerFactoryBean scheduler(DataSource dataSource, Trigger... triggers) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        IntStream.range(0, triggers.length).forEach(i -> log.info("===" + triggers[i].getKey().getName()));

        // App 구동 후에 생성되는 Job 인스턴스의 DI를 위해 JobFactory 설정
        schedulerFactory.setJobFactory(new SpringBeanJobFactory());
        // Scheduler 이름 설정
        schedulerFactory.setSchedulerName(quartzProperties.getSchedulerName());
        // Trigger 설정
        schedulerFactory.setTriggers(triggers);
        schedulerFactory.setApplicationContext(applicationContext);
        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setQuartzProperties(getQuartzProperties());
        schedulerFactory.setDataSource(dataSource);
        schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
        schedulerFactory.setGlobalTriggerListeners(triggerListener);
        schedulerFactory.setGlobalJobListeners(jobListener);
        return schedulerFactory;
    }

    private Properties getQuartzProperties() {
        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties()); // 쿼츠용 프로퍼티
        return properties;
    }

    //    @Bean
    public JobDetailFactoryBean simpleJobDetail() { // Spring 스타일의 JobDetail 빈등록
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(SampleSimpleJob.class);
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setApplicationContext(applicationContext);
        jobDetailFactory.setName("My_SimpleJob_Detail");
        jobDetailFactory.setDescription("Simple Job Detail 입니다.");
        return jobDetailFactory;
    }

//    @Bean
    public SimpleTriggerFactoryBean simpleTrigger(JobDetail simpleJobDetail) {
        SimpleTriggerFactoryBean simpleTrigger = new SimpleTriggerFactoryBean();
        simpleTrigger.setJobDetail(simpleJobDetail);
        simpleTrigger.setRepeatInterval(9000);
        simpleTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        return simpleTrigger;
    }



    //    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

}
