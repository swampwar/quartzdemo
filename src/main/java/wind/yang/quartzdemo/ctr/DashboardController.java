package wind.yang.quartzdemo.ctr;

import lombok.extern.slf4j.Slf4j;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wind.yang.quartzdemo.code.JobExecutionStatusCode;
import wind.yang.quartzdemo.dto.*;
import wind.yang.quartzdemo.service.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public String getTriggerGroupAndTriggerMaster(Model model, @PathVariable String active) {
        List<JobResponse> jobResponseList = new ArrayList<>();
        // TODO 중복코드 제거

        // 활성화된 버튼으로 Trigger Group 추출
        model.addAttribute("activeGroups", dashboardService.getTriggerGroup(active));

        //등록된 트리거를 활성화 버튼에 따라 가져와 마스터 정보 및 이력 정보 추출
        jobResponseList = quartzService.readJobsByTriggerGroup(active);
        model.addAttribute("triggers", dashboardService.getTriggerMaster(jobResponseList));
        return "dashboard :: dashboard-fragment";
    }

    @PostMapping("/getJob")
    @ResponseBody
    public List<ExecProgAndHistory> getJobDatas(@RequestBody JobRequest jobRequest) {
        return dashboardService.getExecJobAndHistory(jobRequest);
    }

    @RequestMapping(path = "/popup/{target}/{progName}", method = RequestMethod.GET)
    public String popup(Model model, @PathVariable String target, @PathVariable String progName) {
        model.addAttribute("contents", dashboardService.getPopupData(target, progName));
        model.addAttribute("progName", target.equals("execLog") ? progName + ".log" : progName);
        return "popup";
    }
}
