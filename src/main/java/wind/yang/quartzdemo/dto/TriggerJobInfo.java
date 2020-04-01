package wind.yang.quartzdemo.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TriggerJobInfo {
    private TriggerGroup triggerGroup;
    private String triggerName;
    private String triggerDescription;
    private String cronExpression;
    private List<ExecProg> execProgInfoList;
    private List<MultipartFile> execProgFileList;

    @Override
    public String toString() {
        return "TriggerJobInfo{" +
                "triggerGroup=" + triggerGroup +
                ", triggerName='" + triggerName + '\'' +
                ", triggerDescription='" + triggerDescription + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                '}';
    }
}
