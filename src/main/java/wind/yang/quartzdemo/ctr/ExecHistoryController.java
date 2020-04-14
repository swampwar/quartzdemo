package wind.yang.quartzdemo.ctr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wind.yang.quartzdemo.dto.ExecHistory;
import wind.yang.quartzdemo.service.ExecHistoryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/history")
public class ExecHistoryController {
    @Autowired
    ExecHistoryService ehService;

    @RequestMapping("/{group}/{name}")
    @ResponseBody
    public Map<String,List<ExecHistory>> history(@PathVariable(required = false) String group,
                                                 @PathVariable(required = false) String name){
        HashMap<String, List<ExecHistory>> rslt = new HashMap<>();
        rslt.put("data", ehService.readLastDetailExecHistory(group, name));
        return rslt;
    }

    @RequestMapping("/all")
    @ResponseBody
    public Map<String,List<ExecHistory>> historyAll(){
        HashMap<String, List<ExecHistory>> rslt = new HashMap<>();
        rslt.put("data", ehService.readDetailAllExecHistory());
        return rslt;
    }
}
