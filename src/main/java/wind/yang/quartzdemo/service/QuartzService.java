package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import wind.yang.quartzdemo.dto.*;
import wind.yang.quartzdemo.mapper.ExecProgMapper;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static wind.yang.quartzdemo.util.QuartzStringUtils.dateToString;

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

    @Autowired
    ExecHistoryService ehService;

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
//        if(execProgMapper.findOneByTrigger(new TriggerKey(jobRequest.getTriggerName(), jobRequest.getTriggerGroup()), 1) == null){
//            execProgMapper.insertExecProg(new ExecProg(jobRequest.getTriggerGroup(), jobRequest.getTriggerName(), 1, jobRequest.getShellScriptNm(),"", "", "a", "b", "c"));
//        }else{
//            // TODO 중복 데이터는 업데이트
//            log.info("중복 데이터는 업데이트");
//        }

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
    public String createForceJob(TriggerKey origTriggerKey, String execSeq){
        if(!"START".equals(getTriggerExecutionStatusCd(origTriggerKey))) {
            TriggerKey fTriggerKey = new TriggerKey(origTriggerKey.getName() + ".F."
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                    , origTriggerKey.getGroup());

            // 강제실행시 실행할 Job의 시퀀스를 지정한다.
            HashMap<String, String> jobData = new HashMap<>();
            jobData.put("execSeq", execSeq);

            SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
            factoryBean.setName(fTriggerKey.getName());
            factoryBean.setGroup(fTriggerKey.getGroup());
            factoryBean.setDescription("강제실행 트리거");
            factoryBean.setRepeatCount(0);
            factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT);
            factoryBean.setJobDetail(defaultJob);
            factoryBean.setJobDataAsMap(jobData);
            factoryBean.afterPropertiesSet();
            try {
                scheduler.scheduleJob(factoryBean.getObject());
            } catch (SchedulerException e) {
                log.error("Trigger[{}]를 강제실행을 위해 Scheduler에 등록 중 에러[{}]가 발생함", fTriggerKey, e.getMessage());
                e.printStackTrace();
                return "서버오류로 Trigger 강제실행 등록에 실패하였습니다.";
            }

            return "Trigger 강제실행 등록이 완료되었습니다.";
        }else {
            return "현재 실행중인 Trigger는 강제실행할 수 없습니다.";
        }
    }

    /**
     * Trigger 리스트 조회
     */
    private List<JobResponse> readTriggers(String triggerGroup){
        List<JobResponse> jobResponseList = new ArrayList<>();
        try {
            List<String> triggerGroupNames = readTriggerGroupNames(triggerGroup);
            for(String group : triggerGroupNames){
                Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.groupEquals(group));
                for(TriggerKey triggerKey : triggerKeys){
                    jobResponseList.add(JobResponse.of(scheduler.getTrigger(triggerKey),
                                                       scheduler.getTriggerState(triggerKey).name().toUpperCase()));
                }
            }
        } catch (SchedulerException e) {
            log.error("Job 전체조회 중 에러[{}]가 발생했습니다.", e.getMessage());
            e.printStackTrace();
        }

        return jobResponseList;
    }

    /**
     * Trigger 단건 조회
     */

    public JobResponse readTrigger(String triggerGroup, String triggerName) {
        JobResponse jobResponse;
        try {
            List<String> triggerGroupNames = readTriggerGroupNames(triggerGroup);
            for (String group : triggerGroupNames) {
                Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.groupEquals(group));
                for(TriggerKey triggerKey : triggerKeys) {
                    if (triggerKey.getName().equals(triggerName)) {
                        jobResponse = JobResponse.of(scheduler.getTrigger(triggerKey),
                                                     scheduler.getTriggerState(triggerKey).name().toUpperCase());

                        return jobResponse;
                    }
                }
            }
        }catch (SchedulerException e) {
            log.error("Job 전체조회 중 에러[{}]가 발생했습니다.", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Trigger 전체조회
     */
    public List<JobResponse> readTriggersAll(){
        return readTriggers(null);
    }

    /**
     * Trigger 부분조회 (trigger group 별 조회)
     * TriggerGroup이 "ALL"이면 전체조회
     */
    public List<JobResponse> readTriggersByTriggerGroup(String triggerGroup){
        if (triggerGroup.equals("ALL")) {
            return readTriggersAll();
        }
        return readTriggers(triggerGroup);
    }

    private List<String> readTriggerGroupNames(String triggerGroup) throws SchedulerException {
        return scheduler.getTriggerGroupNames()
                        .stream()
                        .filter(name -> groupChecker(triggerGroup, name))
                        .collect(toList());
    }

    private static boolean groupChecker(String expectedName, String name){
        if(StringUtils.isEmpty(expectedName)) return true;
        else return expectedName.equals(name);
    }

    // TODO pause 후에 다시 시작하면 다음실행시간이 최신화 되는지 확인필요
    public String pause(TriggerKey triggerkey) throws SchedulerException {
        if (!"PAUSED".equals(getTriggerState(triggerkey))) {
            scheduler.pauseTrigger(triggerkey);
            return "Trigger 정지가 완료되었습니다.";
        }else {
            return "이미 정지된 Trigger는 다시 정지 할 수 없습니다.";
        }
    }

    public String resume(TriggerKey triggerkey) throws SchedulerException {
        if ("PAUSED".equals(getTriggerState(triggerkey))) {
            scheduler.resumeTrigger(triggerkey);
            return "Trigger 재개가 완료되었습니다.";
        }else {
            return "정지된 Trigger만 재개 할 수 없습니다.";
        }
    }

    // quartz 에서 제공하는 triggerState 를 조회해서 PAUSE 혹은 RESUME 가능한지 validate 하기 위힘
    public String getTriggerState(TriggerKey triggerKey) throws SchedulerException {
        return scheduler.getTriggerState(triggerKey).name().toUpperCase();
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

//    public String kill(TriggerKey triggerKey) throws SchedulerException {
//        if("START".equals(getTriggerExecutionStatusCd(triggerKey))) {
//            List<JobExecutionContext> currentlyExecutingJobs = scheduler.getCurrentlyExecutingJobs();
//            for (JobExecutionContext ctx : currentlyExecutingJobs) {
//                if (ctx.getTrigger().getKey().equals(triggerKey)) {
//                    String fireInstanceId = ctx.getFireInstanceId();
//                    log.info("Trigger[{}]에 대한 강제종료를 실행합니다. FireInstanceId[{}]", triggerKey, fireInstanceId);
//                    scheduler.interrupt(fireInstanceId);
//                }
//            }
//            return "Trigger 강제종료가 완료되었습니다.";
//        }else {
//            return "실행중인 Trigger만 강제종료를 할 수 있습니다.\n현재 선택된 Trigger는 실행중이지 않습니다.";
//        }
//    }

    // execution history에서 최근 트리거 실행 상태 코드를 가져와 Kill 또는 CreateForce(재실행) 하기위한 validation으로 사용
    public String getTriggerExecutionStatusCd(TriggerKey triggerKey) {
        String status = ehService.readLastMasterExecHistory(triggerKey.getGroup(), triggerKey.getName()).getWorkResultCd();
        if ("00".equals(status) || "01".equals(status)) {
            status = "READY";
        }else if("1".equals(status) || "4".equals(status)) {
            status = "START";
        }else if("2".equals(status) || "5".equals(status)) {
            status = "SUCCESS";
        }else if("7".equals(status)) {
            status = "DUPLICATE";
        }else if("6".equals(status) || "3".equals(status)) {
            status = "ERROR";
        }
        return status;
    }
}
