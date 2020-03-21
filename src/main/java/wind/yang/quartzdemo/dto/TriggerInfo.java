package wind.yang.quartzdemo.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TriggerInfo {
    private String triggerGroup;
    private String triggerName;
    private String triggerType;
}
