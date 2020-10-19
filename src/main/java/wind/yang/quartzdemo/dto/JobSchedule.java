package wind.yang.quartzdemo.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobSchedule {
    private String jobVerId;
    private String jobGroup;
    private String fullPath;
    private String statusCd;
    private String execParam1;
    private String execParam2;
    private String execParam3;
    private String rgtDtm;
    private String rgtId;
    private String udtDtm;
    private String udtId;
}
