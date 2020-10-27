package wind.yang.quartzdemo.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalJobInfo {
    String insertType;
    String workDvsCd;
    String jobGroupId;
    String jobId;
    String hgrnJobId;
    String progId;
    String jobTime;
    String jobSeq;
    String jobName;
    String jobProdDt;
    String sttDt;
    String endDt;
    String useYn;
    String registId;
    String registDtime;
    String updateId;
    String updateDtime;


    @Override
    public String toString() {
        return "TotalJobInfo{" +
                "insertType='" + insertType + '\'' +
                ", workDvsCd='" + workDvsCd + '\'' +
                ", jobGroupId='" + jobGroupId + '\'' +
                ", jobId='" + jobId + '\'' +
                ", hgrnJobId='" + hgrnJobId + '\'' +
                ", progId='" + progId + '\'' +
                ", jobTime='" + jobTime + '\'' +
                ", jobSeq='" + jobSeq + '\'' +
                ", jobName='" + jobName + '\'' +
                ", jobProdDt='" + jobProdDt + '\'' +
                ", sttDt='" + sttDt + '\'' +
                ", endDt='" + endDt + '\'' +
                ", useYn='" + useYn + '\'' +
                ", registId='" + registId + '\'' +
                ", registDtime='" + registDtime + '\'' +
                ", updateId='" + updateId + '\'' +
                ", updateDtime='" + updateDtime + '\'' +
                '}';
    }
}
