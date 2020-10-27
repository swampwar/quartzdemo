package wind.yang.quartzdemo.ctr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wind.yang.quartzdemo.dto.ExecHistory;
import wind.yang.quartzdemo.dto.TBIBD760;
import wind.yang.quartzdemo.service.ExecHistoryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/history")
public class ExecHistoryController {
    @Autowired
    ExecHistoryService ehService;

//    @RequestMapping("/{group}/{name}")
//    @ResponseBody
//    public Map<String,List<ExecHistory>> history(@PathVariable(required = false) String group,
//                                                 @PathVariable(required = false) String name){
//        HashMap<String, List<ExecHistory>> rslt = new HashMap<>();
//        rslt.put("data", ehService.readLastDetailExecHistory(group, name));
//        return rslt;
//    }

//    @RequestMapping("/search")
//    @ResponseBody
//    public Map<String,List<ExecHistory>> getHistory(@RequestParam("startDate") String startDate,
//                                                    @RequestParam("endDate") String endDate,
//                                                    @RequestParam("triggerGroup") String triggerGroup,
//                                                    @RequestParam("triggerName") String triggerName){
//        HashMap<String, List<ExecHistory>> rslt = new HashMap<>();
//        rslt.put("data", ehService.readDetailAllExecHistory(startDate, endDate, triggerGroup, triggerName));
//        return rslt;
//    }

    @RequestMapping("/search")
    @ResponseBody
    public Map<String,List<TBIBD760>> getHistory(@RequestParam("startDate") String startDate,
                                                    @RequestParam("endDate") String endDate,
                                                    @RequestParam("settmWorkDvsCd") String settmWorkDvsCd,
                                                    @RequestParam("settmJobGroupId") String settmJobGroupId){
        HashMap<String, List<TBIBD760>> rslt = new HashMap<>();
        rslt.put("data", ehService.readDetailAllExecHistory(startDate, endDate, settmWorkDvsCd, settmJobGroupId));
        return rslt;
    }

}
