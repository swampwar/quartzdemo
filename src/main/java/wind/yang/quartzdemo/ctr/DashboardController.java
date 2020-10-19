package wind.yang.quartzdemo.ctr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wind.yang.quartzdemo.dto.*;
import wind.yang.quartzdemo.service.*;

import java.util.List;

/**
 * Dashboard Controller
 * 대시보드에서 화면조회용 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/dashboard")
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

    @Autowired
    DashboardService dashboardService;

    @RequestMapping(path = "/{active}", method = RequestMethod.GET)
    public String getTriggers(Model model, @PathVariable String active) {
        // Dashboard에 출력할 트리거 정보를 조회
        model.addAttribute("activeGroups", dashboardService.getTriggerGroup(active));
        model.addAttribute("triggers", dashboardService.readTriggers(active));
        return "dashboard :: dashboard-fragment";
    }


    @PostMapping("/getJob")
    @ResponseBody
    public List<ExecHistory> getJobDatas(@RequestBody JobRequest jobRequest) {
        return dashboardService.getExecHistoryList(jobRequest);
    }

    @RequestMapping(path = "/popup/{target}/{progName}", method = RequestMethod.GET)
    public String popup(Model model, @PathVariable String target, @PathVariable String progName) {
        model.addAttribute("contents", dashboardService.getPopupData(target, progName));
        model.addAttribute("progName", target.equals("execLog") ? progName + ".log" : progName);
        return "popup";
    }

}
