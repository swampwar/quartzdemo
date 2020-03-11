package wind.yang.quartzdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wind.yang.quartzdemo.quartz.service.QuartzService;

@Slf4j
@Controller
public class MainController {
    @Autowired
    private QuartzService service;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String main(){
        return "main";
    }

    @RequestMapping(path = "/jobs", method = RequestMethod.GET)
    public String jobs(Model model){
        model.addAttribute("jobs", service.readJobs());
        return "fragment :: jobtable-fragment";
    }
}
