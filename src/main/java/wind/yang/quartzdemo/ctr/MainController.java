package wind.yang.quartzdemo.ctr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wind.yang.quartzdemo.service.TriggerGroupService;


/**
 * 화면이동을 위한 컨트롤러
 */
@Slf4j
@Controller
public class MainController {
    @Autowired
    private TriggerGroupService triggerGroupService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String main(Model model){
        model.addAttribute("triggerGroup", triggerGroupService.findAllTriggerGroup());
        return "index";
    }

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String index(Model model){
        model.addAttribute("triggerGroup", triggerGroupService.findAllTriggerGroup());
        return "index";
    }

    @RequestMapping(path = "/history", method = RequestMethod.GET)
    public String history(Model model){
        return "history";
    }

    @RequestMapping(path = "/triggerGroup", method = RequestMethod.GET)
    public String triggerGroup(Model model) {

        return "triggerGroup";
    }

    @RequestMapping(path = "/triggerDetail", method = RequestMethod.GET)
    public String triggerDetail(Model model) {

        return "triggerDetail";
    }
}
