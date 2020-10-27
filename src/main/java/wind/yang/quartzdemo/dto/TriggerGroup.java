package wind.yang.quartzdemo.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TriggerGroup {
    String triggerGroup;
    String description;
    String useYn;
    String rgtDtm;
    String udtDtm;
    String prstId;
    String prmtId;
    int prmtSeq;
}
