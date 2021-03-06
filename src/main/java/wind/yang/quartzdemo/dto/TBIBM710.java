package wind.yang.quartzdemo.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TBIBM710 {
    String settmWorkDvsCd;
    String settmJobGroupId;
    String progId;
    String workTime;
    int workSeq;
    String sttDt;
    String endDt;
    String useYn;
    String registId;
    String registDtime;
    String updateId;
    String updateDtime;

    public TBIBM710(String settmWorkDvsCd, String settmJobGroupId, String progId, String workTime,
                    int workSeq, String sttDt, String endDt, String useYn) {
        this.settmWorkDvsCd = settmWorkDvsCd;
        this.settmJobGroupId = settmJobGroupId;
        this.progId = progId;
        this.workTime = workTime;
        this.workSeq = workSeq;
        this.sttDt = sttDt;
        this.endDt = endDt;
        this.useYn = useYn;
    }

    public TBIBM710(String settmWorkDvsCd, String settmJobGroupId, int workSeq) {
        this.settmWorkDvsCd = settmWorkDvsCd;
        this.settmJobGroupId = settmJobGroupId;
        this.workSeq = workSeq;
    }
}
