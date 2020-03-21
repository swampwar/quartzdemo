package wind.yang.quartzdemo.ctr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wind.yang.quartzdemo.service.ExecHistoryService;
import wind.yang.quartzdemo.service.QuartzService;

import java.time.LocalDateTime;

@Slf4j
@Controller
public class MainController {
    @Autowired
    private QuartzService quartzService;

    @Autowired
    private ExecHistoryService ehService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String main(){
        return "main";
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
