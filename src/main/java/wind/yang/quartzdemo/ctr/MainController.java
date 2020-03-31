package wind.yang.quartzdemo.ctr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wind.yang.quartzdemo.dto.ExecProg;
import wind.yang.quartzdemo.dto.JobResponse;
import wind.yang.quartzdemo.service.ExecHistoryService;
import wind.yang.quartzdemo.service.ExecProgService;
import wind.yang.quartzdemo.service.QuartzService;

import java.time.LocalDateTime;
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

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String main(){
        return "main";
    }

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    @RequestMapping(path = "/monitoring", method = RequestMethod.GET)
    public String monitoring(Model model){
        List<JobResponse> jobResponseList = quartzService.readJobs();

//        for(JobResponse jobResponse : jobResponseList) {
//            String triggerName = jobResponse.getTriggerName();
//            ExecProg execProgSample = new ExecProg();
//            execProgSample.setProgramName(triggerName);
//            String triggerExecStaCd = execHistoryService.readLastExecHistory(execProgSample).getJobExecStaCd();
//            jobResponse.setTriggerExecStaCd(triggerExecStaCd);
//        }

        model.addAttribute("triggers", jobResponseList);
        return "monitoring :: monitoring-fragment";
    }

    @RequestMapping(path = "/dashboard", method = RequestMethod.GET)
    public String dashboard(Model model) {
        List<String> groups = null;
        return "";
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
