package wind.yang.quartzdemo.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TBIBM711 {
    String settmWorkDvsCd;
    String settmJobGroupId;
    String settmJobId;
    String progId;
    String settmJobName;
    int jobSeq;
    String sttDt;
    String endDt;
    String useYn;
    String registId;
    String registDtime;
    String updateId;
    String updateDtime;

    public TBIBM711(String settmWorkDvsCd, String settmJobGroupId, String progId, String settmJobId, String settmJobName,
                    int jobSeq, String sttDt, String endDt, String useYn) {
        this.settmWorkDvsCd = settmWorkDvsCd;
        this.settmJobGroupId = settmJobGroupId;
        this.progId = progId;
        this.settmJobId = settmJobId;
        this.settmJobName = settmJobName;
        this.jobSeq = jobSeq;
        this.sttDt = sttDt;
        this.endDt = endDt;
        this.useYn = useYn;
    }
}
