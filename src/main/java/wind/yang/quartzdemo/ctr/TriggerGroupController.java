package wind.yang.quartzdemo.ctr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wind.yang.quartzdemo.dto.ApiResponse;
import wind.yang.quartzdemo.dto.TriggerGroup;
import wind.yang.quartzdemo.service.TriggerGroupService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/triggerGroup")
public class TriggerGroupController {
    @Autowired
    private TriggerGroupService triggerGroupService;

    @RequestMapping("/all")
    @ResponseBody
    public Map<String, List<TriggerGroup>> getTriggerGroupDatas(Model model) {
        HashMap<String, List<TriggerGroup>> rslt = new HashMap<>();
        rslt.put("data", triggerGroupService.findAllTriggerGroup());
        return rslt;
    }

    @PostMapping("/one/{triggerGroup}")
    @ResponseBody
    public TriggerGroup getTriggerGroupData(@PathVariable(required = false) String triggerGroup) {
        return triggerGroupService.findAllTriggerGroup(triggerGroup);
    }

    @PostMapping("/save")
    @ResponseBody
    public Map<String, Object> saveTriggerGroup(@RequestBody TriggerGroup triggerGroup) {
        return triggerGroupService.saveTriggerGroup(triggerGroup);
    }

    @PostMapping("/delete/{triggerGroup}")
    @ResponseBody
    public ApiResponse deleteTriggerGroup(@PathVariable(required = false) String triggerGroup) {
        if(triggerGroupService.deleteTriggerGroup(triggerGroup)) {
            return new ApiResponse(true, "트리거 그룹 삭제를 완료했습니다.");
        }else {
            return new ApiResponse(false, "서버오류로 작업에 실패했습니다.");
        }
    }
}
