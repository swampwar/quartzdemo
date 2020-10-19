package wind.yang.quartzdemo.ctr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wind.yang.quartzdemo.dto.ProgDetailInfo;
import wind.yang.quartzdemo.dto.TBIBM713;
import wind.yang.quartzdemo.service.ExecProgService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/program")
public class ExecProgController {
    @Autowired
    ExecProgService execProgService;

    @RequestMapping("/search")
    @ResponseBody
    public Map<String,List<TBIBM713>> getProgList(@RequestParam("workDvsCd") String workDvsCd) {
        HashMap<String, List<TBIBM713>> rslt = new HashMap<>();
        rslt.put("data", execProgService.findProgList(workDvsCd));
        return rslt;
    }


    @PostMapping("/insert")
    public String insertProgramDetail(@ModelAttribute ProgDetailInfo progDetailInfo) {
        log.debug(progDetailInfo.toString());
        log.debug(progDetailInfo.getExecProgInfoList().toString());

        List<TBIBM713> insertFailList = execProgService.insertProgDetail(progDetailInfo);

        return "redirect:/programSearch";
    }
}
