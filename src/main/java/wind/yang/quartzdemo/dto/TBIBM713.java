package wind.yang.quartzdemo.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TBIBM713 {
    String settmWorkDvsCd;
    String progId;
    String progDvsCd;
    String progNm;
    String progDesc;
    String progPath;
    String registId;
    String registDtime;
    String updateId;
    String updateDtime;

    public TBIBM713(String settmWorkDvsCd, String progId, String progDvsCd, String progNm,
                    String progDesc, String progPath) {
        this.settmWorkDvsCd = settmWorkDvsCd;
        this.progId = progId;
        this.progDvsCd = progDvsCd;
        this.progNm = progNm;
        this.progDesc = progDesc;
        this.progPath = progPath;
    }
}
