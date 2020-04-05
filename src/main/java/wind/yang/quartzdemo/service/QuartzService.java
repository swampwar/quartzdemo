package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.*;
import wind.yang.quartzdemo.mapper.ExecProgMapper;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class QuartzService {
    @Qualifier("qScheduler")
    @Autowired
    Scheduler scheduler;

    @Autowired
    ExecProgMapper execProgMapper;

    @Autowired
    TriggerService triggerService;

    JobDetail defaultJob;

    @PostConstruct
    public void init() throws SchedulerException {
        JobKey defaultJobKey = new JobKey("DEFUALT_JOB", "DEFAULT_GROUP");
        defaultJob = scheduler.getJobDetail(defaultJobKey);
    }

    /**
     * Job 등록
     * @param jobRequest
     * @return
     */
    public boolean createJob(JobRequest jobRequest) {
        // 잡이 실행할 스크립트정보 저장
        // TODO max+1 seq생성 후 저장
        if(execProgMapper.findOneByTrigger(new TriggerKey(jobRequest.getTriggerName(), jobRequest.getTriggerGroup()), 1) == null){
            execProgMapper.insertExecProg(new ExecProg(jobRequest.getTriggerGroup(), jobRequest.getTriggerName(), 1, jobRequest.getShellScriptNm(),"", "", "a", "b", "c"));
        }else{
            // TODO 중복 데이터는 업데이트
            log.info("중복 데이터는 업데이트");
        }

        // 리스케쥴이 될 트리거 생성
        CronTrigger trigger = null;
        try {
            CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
            factoryBean.setName(jobRequest.getTriggerName());
            factoryBean.setGroup(jobRequest.getTriggerGroup());
            factoryBean.setDescription(jobRequest.getTriggerDescription());
            factoryBean.setCronExpression(jobRequest.getCronExpression());
            factoryBean.setJobDetail(defaultJob);
            factoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
            factoryBean.afterPropertiesSet();
            trigger = factoryBean.getObject();

            scheduler.scheduleJob(trigger);
        } catch (SchedulerException | ParseException e) {
            log.error("Trigger[{}]를 Scheduler에 등록 중 에러[{}]가 발생함", trigger, e.getMessage());
            // TODO 삭제를 하지않고 트랜잭션 처리로 자동 롤백되도록 수정
            return false;
        }

        // TODO 쉘스크립트 파일이 업로드 됬는지 체크필요
        log.info("Trigger 등록완료 [{}], 실행 쉘스크립트정보 등록완료 [{}]", trigger, jobRequest.getShellScriptNm());
        return true;
    }

    /**
     * 강제실행을 위한 Job 등록
     * @param origTriggerKey : 강제실행 대상이 되는 트리거의 키
     * @return
     */
    public boolean createForceJob(TriggerKey origTriggerKey){
        TriggerKey fTriggerKey = new TriggerKey(origTriggerKey.getName() + ".F."
                                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                                , origTriggerKey.getGroup());

        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setName(fTriggerKey.getName());
        factoryBean.setGroup(fTriggerKey.getGroup());
        factoryBean.setDescription("강제실행 트리거");
        factoryBean.setRepeatCount(0);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT);
        factoryBean.setJobDetail(defaultJob);
        factoryBean.afterPropertiesSet();
        try {
            scheduler.scheduleJob(factoryBean.getObject());
        } catch (SchedulerException e) {
            log.error("Trigger[{}]를 강제실행을 위해 Scheduler에 등록 중 에러[{}]가 발생함", fTriggerKey, e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Job 전체조회
     * @return
     */
    public List<JobResponse> readJobs(){
        List<JobResponse> jobResponseList = new ArrayList<>();
        try {
            List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
            for(String group : triggerGroupNames){
                Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.groupEquals(group));
                for(TriggerKey triggerKey : triggerKeys){
                    Trigger trigger = scheduler.getTrigger(triggerKey);
                    JobResponse jobResponse = new JobResponse();
                    jobResponse.setSchedulerName(scheduler.getSchedulerName());
                    jobResponse.setTriggerGroup(triggerKey.getGroup());
                    jobResponse.setTriggerName(triggerKey.getName());
                    jobResponse.setJobGroup(trigger.getJobKey().getGroup());
                    jobResponse.setJobName(trigger.getJobKey().getName());
                    jobResponse.setNextFireTime(dateToString(trigger.getNextFireTime()));
                    jobResponse.setPrevFireTime(dateToString(trigger.getPreviousFireTime()));
                    jobResponse.setStartTime(dateToString(trigger.getStartTime()));
                    if(trigger instanceof CronTrigger){
                        jobResponse.setCronExpression(((CronTrigger)trigger).getCronExpression());
                    }
                    jobResponse.setTriggerStatus(scheduler.getTriggerState(triggerKey).name().toUpperCase());

                    jobResponseList.add(jobResponse);
                }
            }
        } catch (SchedulerException e) {
            log.error("Job 전체조회 중 에러[{}]가 발생했습니다.", e.getMessage());
            e.printStackTrace();
        }

        return jobResponseList;
    }

    /**
     * Job 부분조회 (trigger group 별 조회)
     * @return
     */
    public List<JobResponse> readJobsByTriggerGroup(String triggerGroup){
        List<JobResponse> jobResponseList = new ArrayList<>();
        try {
            List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
            for(String group : triggerGroupNames){
                Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.groupEquals(group));
                for(TriggerKey triggerKey : triggerKeys){
                    if(triggerKey.getGroup().equals(triggerGroup)) {
                        Trigger trigger = scheduler.getTrigger(triggerKey);
                        JobResponse jobResponse = new JobResponse();
                        jobResponse.setSchedulerName(scheduler.getSchedulerName());
                        jobResponse.setTriggerGroup(triggerKey.getGroup());
                        jobResponse.setTriggerName(triggerKey.getName());
                        jobResponse.setJobGroup(trigger.getJobKey().getGroup());
                        jobResponse.setJobName(trigger.getJobKey().getName());
                        jobResponse.setNextFireTime(dateToString(trigger.getNextFireTime()));
                        jobResponse.setPrevFireTime(dateToString(trigger.getPreviousFireTime()));
                        jobResponse.setStartTime(dateToString(trigger.getStartTime()));
                        if(trigger instanceof CronTrigger){
                            jobResponse.setCronExpression(((CronTrigger)trigger).getCronExpression());
                        }
                        jobResponse.setTriggerStatus(scheduler.getTriggerState(triggerKey).name().toUpperCase());

                        jobResponseList.add(jobResponse);
                    }
                }
            }
        } catch (SchedulerException e) {
            log.error("Job 전체조회 중 에러[{}]가 발생했습니다.", e.getMessage());
            e.printStackTrace();
        }

        return jobResponseList;
    }

    // TODO pause 후에 다시 시작하면 다음실행시간이 최신화 되는지 확인필요
    public void pause(TriggerKey triggerkey) throws SchedulerException {
        scheduler.pauseTrigger(triggerkey);
    }

    public void resume(TriggerKey triggerkey) throws SchedulerException {
        scheduler.resumeTrigger(triggerkey);
    }

    public String update(TriggerKey triggerkey, String cronExpression) throws SchedulerException, ParseException {
        // 기존 트리거 조회
        Trigger scheduledTrigger = scheduler.getTrigger(triggerkey);
        JobDetail jobDetail = scheduler.getJobDetail(scheduledTrigger.getJobKey());

        // 리스케쥴이 될 트리거 생성
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setName(triggerkey.getName());
        factoryBean.setGroup(triggerkey.getGroup());
        factoryBean.setDescription(scheduledTrigger.getDescription());
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
        factoryBean.afterPropertiesSet();

        // 리스케쥴
        Date dateOfFirstFire = scheduler.rescheduleJob(triggerkey, factoryBean.getObject());
        log.info("트리거({},{})에 대한 리스케쥴 완료. 첫 시작시간 : {}",
                triggerkey.getName(), triggerkey.getGroup(), dateToString(dateOfFirstFire));

        return dateToString(dateOfFirstFire);
    }

    public boolean delete(TriggerKey triggerKey) {
        try {
            return scheduler.unscheduleJob(triggerKey);
        } catch (SchedulerException e) {
            log.error("TriggerKey[{}]에 대한 삭제 중 에러[{}]발생", triggerKey, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // TODO guava에는 Date -> String 컨버터가 있는지?
    public String dateToString(Date src){
        String rslt = "";
        if(src == null){
            return rslt;
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            rslt = sdf.format(src);
        }
        return rslt;
    }

    public boolean isExist(TriggerKey triggerKey) {
        try {
            boolean checkRslt = scheduler.checkExists(triggerKey);
            log.info("TriggerKey[{}]에 대한 기존 등록여부 : [{}]", triggerKey, checkRslt);
            return checkRslt;
        } catch (SchedulerException e) {
            log.error("TriggerKey[{}]에 대한 기존 등록여부 검사 중 에러[{}]발생", triggerKey, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean isRunning(TriggerKey triggerKey) throws SchedulerException {
        List<JobExecutionContext> currentlyExecutingJobs = getCurrentlyExecutingJobs();
        for(JobExecutionContext ctx : currentlyExecutingJobs){
            TriggerKey runningTrigerKey = ctx.getTrigger().getKey();
            if(runningTrigerKey.equals(triggerKey)){
                log.info("Trigger[{}]는 현재 실행중입니다. 실행시간 : [{}]", ctx.getTrigger().getKey(), ctx.getFireTime());
                return true;
            }
        }
        return false;
    }

    public int getRunningTriggerCnt(TriggerKey triggerKey) throws SchedulerException {
        int cnt = 0;
        List<JobExecutionContext> currentlyExecutingJobs = getCurrentlyExecutingJobs();
        for(JobExecutionContext ctx : currentlyExecutingJobs){
            TriggerKey runningTrigerKey = ctx.getTrigger().getKey();
            if(runningTrigerKey.equals(triggerKey)){
                log.info("Trigger[{}]는 현재 실행중입니다. 실행시간 : [{}]", ctx.getTrigger().getKey(), ctx.getFireTime());
                cnt++;
            }
        }
        log.info("Trigger 실행 갯수 : [{}]", cnt);
        return cnt;
    }

    private List<JobExecutionContext> getCurrentlyExecutingJobs() throws SchedulerException {
        List<JobExecutionContext> currentlyExecutingJobs = null;
        try {
            currentlyExecutingJobs = scheduler.getCurrentlyExecutingJobs();
        } catch (SchedulerException e) {
            log.error("Trigger 실행중 여부체크를 위한 CurrentlyExecutingJobs 조회중 에러발생 : [{}]", e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return currentlyExecutingJobs;
    }

    public void kill(TriggerKey triggerKey) throws SchedulerException {
        List<JobExecutionContext> currentlyExecutingJobs = scheduler.getCurrentlyExecutingJobs();
        for(JobExecutionContext ctx : currentlyExecutingJobs){
            if(ctx.getTrigger().getKey().equals(triggerKey)){
                String fireInstanceId = ctx.getFireInstanceId();
                log.info("Trigger[{}]에 대한 강제종료를 실행합니다. FireInstanceId[{}]", triggerKey, fireInstanceId);
                scheduler.interrupt(fireInstanceId);
            }
        }
    }
}
