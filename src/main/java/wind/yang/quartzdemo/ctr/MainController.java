package wind.yang.quartzdemo.ctr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wind.yang.quartzdemo.dto.ExecProg;
import wind.yang.quartzdemo.dto.JobResponse;
import wind.yang.quartzdemo.dto.TriggerGroup;
import wind.yang.quartzdemo.service.ExecHistoryService;
import wind.yang.quartzdemo.service.ExecProgService;
import wind.yang.quartzdemo.service.QuartzService;
import wind.yang.quartzdemo.service.TriggerGroupService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class MainController {
    @Autowired
    private QuartzService quartzService;

    @Autowired
    private ExecHistoryService ehService;

    @Autowired
    private ExecProgService epServive;

    @Autowired
    private TriggerGroupService triggerGroupService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String main(){
        return "main";
    }

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String index(Model model){
        model.addAttribute("triggerGroup", triggerGroupService.findAllTriggerGroup());
        return "index";
    }

    @RequestMapping(path = "/monitoring", method = RequestMethod.GET)
    public String monitoring(Model model){
        List<JobResponse> jobResponseList = quartzService.readJobs();

        for(JobResponse jobResponse : jobResponseList) {
            String triggerName = jobResponse.getTriggerName();
            ExecProg execProgSample = new ExecProg();

            // 검색 조건을 넣어준다
            execProgSample.setTriggerName(triggerName); execProgSample.setSeq(0);

            String triggerExecStaCd = ehService.readLastExecHistory(execProgSample).getJobExecStaCd().toString();
            log.info("{} => triggerExecStaCd : {}", triggerName, triggerExecStaCd);
            jobResponse.setTriggerExecStaCd(triggerExecStaCd);
        }

        model.addAttribute("triggers", jobResponseList);
        return "monitoring :: monitoring-fragment";
    }

    @RequestMapping(path = "/dashboard/{active}", method = RequestMethod.GET)
    public String dashboard(Model model, @PathVariable String active) {
        List<JobResponse> jobResponseList = new ArrayList<>();
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

    @RequestMapping(path = "/jobs", method = RequestMethod.GET)
    public String jobs(Model model){
        model.addAttribute("jobs", quartzService.readJobs());
        return "fragment :: jobtable-fragment";
    }

    @RequestMapping(path = "/execHistory", method = RequestMethod.GET)
    public String execHistory(Model model){
        model.addAttribute("historyList", ehService.readExecHistory());
        return "history_fragment :: historytable-fragment";
    }
}
