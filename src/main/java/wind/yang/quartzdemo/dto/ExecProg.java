package wind.yang.quartzdemo.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExecProg {
    String triggerGroup;
    String triggerName;
    int seq;
    String programName;
    String summary;
    String description;
    String execParam1;
    String execParam2;
    String execParam3;
}
