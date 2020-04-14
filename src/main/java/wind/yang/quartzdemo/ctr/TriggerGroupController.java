package wind.yang.quartzdemo.ctr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wind.yang.quartzdemo.service.TriggerGroupService;

@Slf4j
@Controller
@RequestMapping("/triggerGroup")
public class TriggerGroupController {
    @Autowired
    private TriggerGroupService triggerGroupService;

    @RequestMapping(path = "/getTriggerData", method = RequestMethod.GET)
    public String getTriggerGroupData(Model model) {
        model.addAttribute("triggerGroup", triggerGroupService.findAllTriggerGroup());
        return "triggerGroup";
    }
}
