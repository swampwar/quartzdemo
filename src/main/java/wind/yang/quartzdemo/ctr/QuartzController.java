package wind.yang.quartzdemo.ctr;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wind.yang.quartzdemo.dto.*;
import wind.yang.quartzdemo.service.ExecHistoryService;
import wind.yang.quartzdemo.service.ExecProgService;
import wind.yang.quartzdemo.service.QuartzService;

import java.text.ParseException;
import java.util.List;

/**
 * Quartz Scheduler Controller
 * 쿼츠 스케쥴러에 대한 조회/업데이트
 */
@Slf4j
@Controller
@RequestMapping(path = "/scheduler")
public class QuartzController {
    @Autowired
    QuartzService quartzService;
    @Autowired
    ExecProgService execProgService;
    @Autowired
    ExecHistoryService execHistoryService;

    @GetMapping("/jobs")
    @ResponseBody
    public List<JobResponse> readJobs() {
        log.info("Job(Trigger) 조회 시작");
        return quartzService.readTriggersAll();
    }

    @PostMapping("/job")
    @ResponseBody
    public ApiResponse createJob(@RequestBody JobRequest jobRequest){
        log.info("Job(Trigger) 생성 시작 [{}]", jobRequest);
        TriggerKey triggerKey = new TriggerKey(jobRequest.getTriggerName(), jobRequest.getTriggerGroup());

        // 등록여부 존재 확인
        if(quartzService.isExist(triggerKey)){
            return new ApiResponse(false, "해당 Trigger가 이미 존재합니다.");
        }

        // Job(Trigger) 생성
        if(quartzService.createJob(jobRequest)){
            return new ApiResponse(true, "Trigger 등록이 완료되었습니다.");
        }else{
            return new ApiResponse(false, "Trigger 등록을 실패했습니다.");
        }
    }

    @DeleteMapping("/job")
    @ResponseBody
    public ApiResponse deleteJob(@RequestBody JobRequest jobRequest) throws SchedulerException {
        log.info("Job(Trigger) 삭제 시작 [{}]", jobRequest);
        TriggerKey triggerKey = new TriggerKey(jobRequest.getTriggerName(), jobRequest.getTriggerGroup());

        if(!quartzService.isExist(triggerKey)) // 등록여부 확인
            return new ApiResponse(false, "Trigger가 존재하지 않습니다.");

        if(!quartzService.isRunning(triggerKey)){ // 실행중 확인
            if(quartzService.delete(triggerKey)){ // 삭제
                return new ApiResponse(true, "Trigger가 삭제되었습니다.");
            }else{
                return new ApiResponse(false, "Trigger가 삭제에 실패하였습니다.");
            }
        }else{
            // TODO 강제종료 할 수는 없나?
            return new ApiResponse(false, "Trigger가 실행중 상태입니다.");
        }
    }

    @PutMapping("/job/pause")
    @ResponseBody
    public ApiResponse pauseJob(@RequestBody JobRequest jobRequest) {
        // TODO 파라미터 필수검사
        log.info("pauseJob 파라미터 : {}", jobRequest);
        String msg = "서버오류로 Trigger 정지에 실패했습니다.";
        try{
            msg = quartzService.pause(new TriggerKey(jobRequest.getTriggerName(), jobRequest.getTriggerGroup()));
        }catch(SchedulerException e){
            return new ApiResponse(false, msg);
        }
        return new ApiResponse(true, msg);
    }

    @PutMapping("/job/resume")
    @ResponseBody
    public ApiResponse resumeJob(@RequestBody JobRequest jobRequest) {
        log.info("resumeJob 파라미터 : {}", jobRequest);
        String msg = "서버오류로 Trigger 재개에 실패했습니다.";
        try{
            msg = quartzService.resume(new TriggerKey(jobRequest.getTriggerName(), jobRequest.getTriggerGroup()));
        }catch(SchedulerException e){
            return new ApiResponse(false, msg);
        }

        return new ApiResponse(true, msg);
    }

    @PutMapping("/job/update")
    @ResponseBody
    public ApiResponse updateJob(@RequestBody JobRequest jobRequest) throws SchedulerException, ParseException {
        log.info("updateJob 파라미터 : {}", jobRequest);
        quartzService.update(new TriggerKey(jobRequest.getTriggerName(), jobRequest.getTriggerGroup()), jobRequest.getCronExpression());

        return new ApiResponse(true, "Trigger 수정이 완료되었습니다.");
    }

    @PostMapping("/job/kill")
    @ResponseBody
    public ApiResponse killJob(@RequestBody JobRequest jobRequest) {
        // TODO 파라미터 필수검사
        log.info("killJob 파라미터 : {}", jobRequest);
        String msg = "서버오류로 Trigger 강제종료에 실패했습니다.";
        try{
            msg = quartzService.kill(new TriggerKey(jobRequest.getTriggerName(), jobRequest.getTriggerGroup()));
        }catch(SchedulerException e){
            return new ApiResponse(false, msg);
        }

        return new ApiResponse(true, msg);
    }

    @PostMapping("/job/runNow")
    @ResponseBody
    public ApiResponse runNowJob(@RequestBody JobRequest jobRequest) {
        // TODO 파라미터 필수검사
        log.info("runNowJob 파라미터 : {}", jobRequest);
        String msg = "";
        // Job(Trigger) 생성
        msg = quartzService.createForceJob(new TriggerKey(jobRequest.getTriggerName(), jobRequest.getTriggerGroup()));
        return new ApiResponse(true, msg);
    }
}
