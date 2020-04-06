package wind.yang.quartzdemo.ctr;

import lombok.extern.slf4j.Slf4j;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wind.yang.quartzdemo.code.JobExecutionStatusCode;
import wind.yang.quartzdemo.dto.*;
import wind.yang.quartzdemo.service.ExecHistoryService;
import wind.yang.quartzdemo.service.ExecProgService;
import wind.yang.quartzdemo.service.QuartzService;
import wind.yang.quartzdemo.service.TriggerGroupService;

import java.util.ArrayList;
import java.util.List;

/**
 * Dashboard Controller
 * 대시보드에서 화면조회용 컨트롤러
 */
@Slf4j
@Controller("/dashboard")
public class DashboardController {
    @Autowired
    private QuartzService quartzService;

    @Autowired
    private ExecHistoryService ehService;

    @Autowired
    private TriggerGroupService triggerGroupService;

    @Autowired
    ExecProgService execProgService;

    @Autowired
    ExecHistoryService execHistoryService;

    @RequestMapping(path = "/{active}", method = RequestMethod.GET)
    public String dashboard(Model model, @PathVariable String active) {
        List<JobResponse> jobResponseList = new ArrayList<>();
        // TODO 컨트롤러에서 비즈니스 로직 제외
        // TODO DashboardService 생성
        // TODO 중복코드 제거
        if("ALL".equals(active)) {
            model.addAttribute("activeGroup",triggerGroupService.findAllTriggerGroup());
            jobResponseList = quartzService.readJobs();
        }else {
            model.addAttribute("activeGroup",triggerGroupService.findByTriggerGroup(active));
            jobResponseList = quartzService.readJobsByTriggerGroup(active);
        }

        for(JobResponse jobResponse : jobResponseList) {
            ExecProg execProgSample = new ExecProg();

            // 검색 조건을 넣어준다
            execProgSample.setTriggerName(jobResponse.getTriggerName());
            execProgSample.setTriggerGroup(jobResponse.getTriggerGroup());
            execProgSample.setSeq(0);

            String triggerExecStaCd = ehService.readLastExecHistory(execProgSample).getJobExecStaCd().toString();
            log.info("{} => triggerExecStaCd : {}", jobResponse.getTriggerName(), triggerExecStaCd);
            jobResponse.setTriggerExecStaCd(triggerExecStaCd);
        }

        model.addAttribute("triggers", jobResponseList);
        return "dashboard :: dashboard-fragment";
    }

    @PostMapping("/getJob")
    @ResponseBody
    public List<ExecProgAndHistory> getJobDatas(@RequestBody JobRequest jobRequest) {
        // TODO 파라미터 필수검사
        log.info("getJobDatas 파라미터 : {}", jobRequest);
        List<ExecProgAndHistory> execProgAndHistoryList = new ArrayList<>();

        // Job list 호출
        List<ExecProg> execProgList = execProgService.findByTrigger(new TriggerKey(jobRequest.getTriggerName(), jobRequest.getTriggerGroup()));

        for (ExecProg execProg : execProgList) {
            ExecHistory execHistory = execHistoryService.readLastExecHistory(execProg);
            ExecProgAndHistory execProgAndHistory;
            if (execHistory != null) {
                execProgAndHistory = new ExecProgAndHistory(execProg.getTriggerGroup(), execProg.getTriggerName(), execProg.getSeq(),
                        execProg.getProgramName(), execHistory.getJobSttDtm(), execHistory.getJobEndDtm(), execHistory.getJobGroup(), execHistory.getJobName(),
                        execHistory.getJobExecStaCd(), execHistory.getJobExecRslt());

            } else {
                execProgAndHistory = new ExecProgAndHistory(execProg.getTriggerGroup(), execProg.getTriggerName(), execProg.getSeq(),
                        execProg.getProgramName(), JobExecutionStatusCode.READY, "DEFAULT_JOB");
            }
            execProgAndHistoryList.add(execProgAndHistory);
        }
        return execProgAndHistoryList;
    }

}
