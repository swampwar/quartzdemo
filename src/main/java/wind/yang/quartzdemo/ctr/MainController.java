package wind.yang.quartzdemo.ctr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wind.yang.quartzdemo.service.DashboardService;
import wind.yang.quartzdemo.service.ExecProgService;
import wind.yang.quartzdemo.service.JobGropService;
import wind.yang.quartzdemo.service.TriggerGroupService;


/**
 * 화면이동을 위한 컨트롤러
 */
@Slf4j
@Controller
public class MainController {
    @Autowired
    private TriggerGroupService triggerGroupService;
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private JobGropService jobGropService;
    @Autowired
    private ExecProgService execProgService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String main(Model model){
        model.addAttribute("triggerGroup", triggerGroupService.findByTriggerGroup("ALL"));
        return "index";
    }

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String index(Model model){
        model.addAttribute("triggerGroup", triggerGroupService.findByTriggerGroup("ALL"));
        return "index";
    }

    @RequestMapping(path = "/history", method = RequestMethod.GET)
    public String history(Model model){
        model.addAttribute("triggerGroup", triggerGroupService.findByTriggerGroup("ALL"));
        model.addAttribute("triggers", dashboardService.readTriggers("ALL"));
        return "history";
    }

    @RequestMapping(path = "/triggerGroup", method = RequestMethod.GET)
    public String triggerGroup(Model model) {
        return "triggerGroup";
    }

    @RequestMapping(path = "/triggerDetail", method = RequestMethod.GET)
    public String triggerDetail(Model model) {
        model.addAttribute("workDvsCd", triggerGroupService.findByTriggerGroup("ALL"));
        model.addAttribute("jobGroup", jobGropService.getJobGroupList("ALL"));
        model.addAttribute("programList", execProgService.findProgList("ALL"));
        model.addAttribute("type", "insert");
        return "triggerDetail";
    }

    @RequestMapping(path = "/programSearch", method = RequestMethod.GET)
    public String programDetail(Model model) {
        model.addAttribute("triggerGroup", triggerGroupService.findByTriggerGroup("ALL"));
        return "programSearch";
    }

    @RequestMapping(path = "/programRegist", method = RequestMethod.GET)
    public String programRegist(Model model) {
        model.addAttribute("triggerGroup", triggerGroupService.findByTriggerGroup("ALL"));
        model.addAttribute("type", "insert");
        return "programDetail";
    }

    @RequestMapping(path = "/dbtest", method = RequestMethod.GET)
    public String dbtest(Model model) {
        String result = dashboardService.dbtest();

        return result;
    }
}
