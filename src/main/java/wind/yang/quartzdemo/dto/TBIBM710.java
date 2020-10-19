package wind.yang.quartzdemo.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TBIBM710 {
    String workDvsCd;
    String jobGroupId;
    String progId;
    String workTime;
    String workSeq;
    String sttDt;
    String endDt;
    String registId;
    String registDtime;
    String updateId;
    String updateDtime;

    public TBIBM710(String workDvsCd, String jobGroupId, String progId, String workTime,
                    String workSeq, String sttDt, String endDt) {
        this.workDvsCd = workDvsCd;
        this.jobGroupId = jobGroupId;
        this.progId = progId;
        this.workTime = workTime;
        this.workSeq = workSeq;
        this.sttDt = sttDt;
        this.endDt = endDt;
    }
}
