package wind.yang.quartzdemo.ctr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import wind.yang.quartzdemo.service.TriggerDetailService;

@Slf4j
@Controller
@RequestMapping("/triggerGroup")
public class TriggerDetailController {
    @Autowired
    private TriggerDetailService triggerDetailService;

    public TriggerDetailController(TriggerDetailService triggerDetailService) {
        this.triggerDetailService = triggerDetailService;
    }
}
