package wind.yang.quartzdemo.dto;

import lombok.*;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import wind.yang.quartzdemo.code.JobExecutionStatusCode;
import wind.yang.quartzdemo.service.ExecProgService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class TBIBD760 {

    String settmWorkDvsCd;
    String settmJobGroupId;
    String settmJobId;
    String progId;
    String systAreaClassCd;
    String workDt;
    int workSeq;
    String workSttDtime;
    String workEndDtime;
    String workResultCd;
    String workDesc;
    String registId;
    String registDtime;
    String updateId;
    String updateDtime;

    /**
     * 신규 마스터 실행이력 생성
     */
    public static TBIBD760 newMaster(TBIBM710 tbibm710, TBIBM713 tbibm713){
        String sttDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

//        String triggerName = triggerKey.getName();
        // FORCE인 경우 이력 등록시 원래 트리거 이름으로 마스터 저장
//        if(triggerName.indexOf(".") != -1) {
//            System.out.println(triggerName);
//            triggerName = triggerName.substring(0, triggerName.indexOf("."));
//            System.out.println(triggerName);
//        }

        TBIBD760 master = newExecHistory(tbibm710, tbibm713, sttDtm);
        master.setWorkResultCd("00");
        return master;
    }

    /**
     * 신규 상세 실행이력 생성
     */
    public static TBIBD760 newDetail(TBIBM710 tbibm710, TBIBM713 tbibm713, TBIBD760 master){
//        JobKey jobkey = new JobKey(master.getSettmJobGroupId(), master.getSettmWorkDvsCd());
        return newExecHistory(tbibm710, tbibm713, master.getWorkSttDtime());
    }

    /**
     * 신규 실행이력 생성
     */
    public static TBIBD760 newExecHistory(TBIBM710 tbibm710, TBIBM713 tbibm713, String workSttDtm){
        TBIBD760 tbibd760 = TBIBD760.of(tbibm710);
        tbibd760.setWorkDt(workSttDtm.substring(0,8));
        tbibd760.setWorkSttDtime(workSttDtm);
        tbibd760.setRegistDtime(workSttDtm);
        tbibd760.setRegistDtime(workSttDtm);
        tbibd760.setSettmWorkDvsCd(tbibm710.settmWorkDvsCd);
        tbibd760.setSettmJobGroupId(tbibm710.settmJobGroupId);
        if (tbibm713 != null) {
//            String progType = "";
//            if("00".equals(tbibm713.getProgDvsCd())) {
//                progType = ".sh";
//            }
            tbibd760.setWorkDesc(tbibm713.progPath + tbibm713.progNm);
        }
        return tbibd760;
    }

    public static TBIBD760 of(TBIBM710 tbibm710) {
        return TBIBD760.builder()
                .settmJobId("")
                .progId(tbibm710.getProgId())
                .systAreaClassCd("0")
                .workResultCd("00")
                .registId("quartz scheduler")
                .build();
    }

    public static TBIBD760 of(TBIBM711 tbibm711) {
        return TBIBD760.builder()
                .settmJobId(tbibm711.getSettmJobId())
                .progId(tbibm711.getProgId())
                .systAreaClassCd("0")
                .workResultCd("READY")
                .registId("quartz scheduler")
                .build();
    }
}
