package wind.yang.quartzdemo.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TBIBM712 {
    String settmWorkDvsCd;
    String settmJobId;
    String hgrnSettmJobId;
    String progId;
    int jobSeq;
    String workProdDt;
    String sttDt;
    String endDt;
    String useYn;
    String registId;
    String registDtime;
    String updateId;
    String updateDtime;

    public TBIBM712(String settmWorkDvsCd, String settmJobId, String progId, String hgrnSettmJobId, String workProdDt,
                    int jobSeq, String sttDt, String endDt, String useYn) {
        this.settmWorkDvsCd = settmWorkDvsCd;
        this.settmJobId = settmJobId;
        this.progId = progId;
        this.hgrnSettmJobId = hgrnSettmJobId;
        this.workProdDt = workProdDt;
        this.jobSeq = jobSeq;
        this.sttDt = sttDt;
        this.endDt = endDt;
        this.useYn = useYn;
    }
}
