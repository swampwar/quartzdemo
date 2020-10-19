package wind.yang.quartzdemo.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgDetailInfo {
    private String triggerGroup;
    private String triggerName1;
    private String triggerName2;
    private String triggerDescription;
    private String cronExpression;
    private List<TBIBM713> execProgInfoList;
    private List<MultipartFile> execProgFileList;

    @Override
    public String toString() {
        return "TriggerJobInfo{" +
                "triggerGroup='" + triggerGroup + '\'' +
                ", triggerName1='" + triggerName1 + '\'' +
                ", triggerName2='" + triggerName2 + '\'' +
                ", triggerDescription='" + triggerDescription + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                '}';
    }
}
