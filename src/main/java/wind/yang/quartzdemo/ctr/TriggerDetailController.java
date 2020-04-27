package wind.yang.quartzdemo.ctr;

import lombok.extern.slf4j.Slf4j;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wind.yang.quartzdemo.dto.ExecProg;
import wind.yang.quartzdemo.dto.JobRequest;
import wind.yang.quartzdemo.dto.TriggerGroup;
import wind.yang.quartzdemo.dto.TriggerJobInfo;
import wind.yang.quartzdemo.service.DashboardService;
import wind.yang.quartzdemo.service.ExecProgService;
import wind.yang.quartzdemo.service.TriggerDetailService;
import wind.yang.quartzdemo.service.TriggerGroupService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/triggerDetail")
public class TriggerDetailController {
    @Autowired
    private TriggerDetailService triggerDetailService;
    @Autowired
    ExecProgService execProgService;
    @Autowired
    private TriggerGroupService triggerGroupService;
    @Autowired
    private DashboardService dashboardService;

    public TriggerDetailController(TriggerDetailService triggerDetailService) {
        this.triggerDetailService = triggerDetailService;
    }

    @RequestMapping(path = "/{triggerGroup}/{triggerName}", method = RequestMethod.GET)
    public String getTriggerDetail(@PathVariable(required = false) String triggerGroup,
                                            @PathVariable(required = false) String triggerName,
                                            Model model) {
        TriggerJobInfo triggerJobInfo = triggerDetailService.getTriggerJobInfo(triggerGroup, triggerName);
        log.debug(triggerJobInfo.toString());
        model.addAttribute("detail", triggerJobInfo);
        model.addAttribute("type", "select");
        return "triggerDetail";
    }

    @RequestMapping(path = "/update/{triggerGroup}/{triggerName}", method = RequestMethod.GET)
    public String getTriggerDetailForUpdate(@PathVariable(required = false) String triggerGroup,
                                   @PathVariable(required = false) String triggerName,
                                   Model model) {
        TriggerJobInfo triggerJobInfo = triggerDetailService.getTriggerJobInfo(triggerGroup, triggerName);
        log.debug(triggerJobInfo.toString());
        model.addAttribute("triggerGroup", triggerGroupService.findByTriggerGroup("ALL"));
        model.addAttribute("triggers", dashboardService.readTriggers("ALL"));
        model.addAttribute("detail", triggerJobInfo);
        model.addAttribute("type", "update");
        return "triggerDetail";
    }

    @PostMapping("/getExecProgList")
    @ResponseBody
    public List<ExecProg> getExecProgList(@RequestBody JobRequest jobRequest) {
        return execProgService.findByTrigger(new TriggerKey(jobRequest.getTriggerName(), jobRequest.getTriggerGroup()));
    }
}
