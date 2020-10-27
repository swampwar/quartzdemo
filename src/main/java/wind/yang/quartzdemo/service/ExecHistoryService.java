package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.*;
import wind.yang.quartzdemo.mapper.ExecHistoryMapper;
import wind.yang.quartzdemo.mapper.TBIBD760Mapper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class ExecHistoryService {
    @Autowired
    ExecHistoryMapper ehMapper;

    @Autowired
    TBIBD760Mapper tbibd760Mapper;

    @Autowired
    JobService jobService;

    public List<ExecHistory> readExecHistoryAll() {
        List<ExecHistory> historyList = null;
        historyList = ehMapper.findAll();
        return historyList;
    }

//    public List<ExecHistory> readDetailAllExecHistory(String startDate, String endDate, String triggerGroup, String triggerName) {
//        return ehMapper.findDetailAll(startDate, endDate, triggerGroup, triggerName);
//    }

    public List<TBIBD760> readDetailAllExecHistory(String startDate, String endDate, String settmWorkDvsCd, String settmJobGroupId) {
        return tbibd760Mapper.findDetailAll(startDate, endDate, settmWorkDvsCd, settmJobGroupId);
    }

//    public List<ExecHistory> readLastAllExecHistory(String group, String name) {
//        ExecHistory lastMaster = readLastMasterExecHistory(group, name);
//        ExecHistory searchParam = ExecHistory.builder()
//                                            .triggerGroup(group)
//                                            .triggerName(name)
//                                            .triggerSttDtm(lastMaster.getTriggerSttDtm())
//                                            .execProgSeq(-1)
//                                            .build();
//        return ehMapper.findByExecHistory(searchParam);
//    }

    public List<TBIBD760> readLastAllExecHistory(String workDvsCd, String jobGroupId, String type) {
        List<TBIBD760> allJob = new ArrayList<>();
        TBIBD760 lastMaster = readLastMasterExecHistory(workDvsCd, jobGroupId);
        TBIBD760 startMaster = readLastStartMasterExecHistroy(workDvsCd, jobGroupId);

        List<TBIBM711> tbibm711List = jobService.getJobList(workDvsCd, jobGroupId);

        for (TBIBM711 tbibm711 : tbibm711List) {
            TBIBD760 searchParam = TBIBD760.builder()
                    .settmWorkDvsCd(workDvsCd)
                    .settmJobGroupId(jobGroupId)
                    .settmJobId(tbibm711.getSettmJobId())
                    .progId(tbibm711.getProgId())
                    .workSttDtime(startMaster.getWorkSttDtime())
                    .workEndDtime(lastMaster.getWorkSttDtime())
                    .build();

            List<TBIBD760> data = tbibd760Mapper.findByExecHistory(searchParam);
            TBIBD760 startData = new TBIBD760(); TBIBD760 lastData = new TBIBD760();
            if (data.size() == 1) {
                lastData = data.get(0);
                lastData.setWorkEndDtime(null);
            }else if(data.size() == 2) {
                lastData = data.get(0);
                startData = data.get(1);

                lastData.setWorkSttDtime(startData.getWorkSttDtime());
            }else {
                lastData.setSettmWorkDvsCd(tbibm711.getSettmWorkDvsCd());
                lastData.setSettmJobGroupId(tbibm711.getSettmJobGroupId());
                lastData.setSettmJobId(tbibm711.getSettmJobId());
                lastData.setWorkResultCd("READY");
            }

            if (data != null) {
                String status = lastData.getWorkResultCd();
                if ("00".equals(status) || "01".equals(status)) {
                    status = "READY";
                } else if ("1".equals(status) || "4".equals(status)) {
                    status = "START";
                } else if ("2".equals(status) || "5".equals(status)) {
                    status = "SUCCESS";
                } else if ("7".equals(status)) {
                    status = "DUPLICATE";
                } else if ("6".equals(status) || "3".equals(status)) {
                    status = "ERROR";
                }
                lastData.setWorkResultCd(status);
                lastData.setWorkSeq(tbibm711.getJobSeq());
                lastData.setSettmJobId(tbibm711.getSettmJobId() + "(" + tbibm711.getSettmJobName() + ")");
                allJob.add(lastData);
            }
        }
        return allJob;
    }

    private TBIBD760 readLastStartMasterExecHistroy(String workDvsCd, String jobGroupId) {
        TBIBD760 tbibd760 = TBIBD760.builder()
                .settmWorkDvsCd(workDvsCd)
                .settmJobGroupId(jobGroupId)
                .build();
        return tbibd760Mapper.findLastStartExecHistory(tbibd760);
    }

    public List<TBIBD760> readLastDetailExecHistory(String workDvsCd, String jobGroupId, String type) {
        List<TBIBD760> all = readLastAllExecHistory(workDvsCd, jobGroupId, type);
//        all.remove(0); // 마스터이력 제외

        return all;
    }

//    public ExecHistory readLastMasterExecHistory(String group, String name){
//        ExecHistory searchParam = ExecHistory.builder()
//                                            .triggerGroup(group)
//                                            .triggerName(name)
//                                            .execProgSeq(0)
//                                            .build();
//        return ehMapper.findLastExecHistory(searchParam);
//    }

    public TBIBD760 readLastMasterExecHistory(String workDvsCd, String jobGroupId){
        TBIBD760 tbibd760 = TBIBD760.builder()
                                            .settmWorkDvsCd(workDvsCd)
                                            .settmJobGroupId(jobGroupId)
                                            .build();
        return tbibd760Mapper.findLastExecHistory(tbibd760);
    }

//    public ExecHistory insertStartExecHistory(TriggerKey triggerKey, JobKey jobKey, List<ExecProg> execProgList) {
//        // 마스터 이력 등록
//        ExecHistory master = ExecHistory.newMaster(triggerKey, jobKey);
//        insertExecHistory(master);
//
//        // 상세 이력 등록
//        for(ExecProg execProg : execProgList){
//            ExecHistory detail = ExecHistory.newDetail(execProg, master);
//            insertExecHistory(detail);
//        }
//        return master;
//
//    }

    public TBIBD760 insertStartExecHistory(TBIBM710 tbibm710, TBIBM713 tbibm713) {
        // 마스터 이력 등록
        TBIBD760 master = TBIBD760.newMaster(tbibm710, tbibm713);

        // 기존에 마스터가 있는지 확인 (있으면 지역구분코드 +1)
        TBIBD760 bfMaster = tbibd760Mapper.checkJobHistoryMaster(master);
        if(bfMaster != null) {
            log.debug("before histroy master TBIBD760 : {}", bfMaster.toString());
            String seq = bfMaster.getSystAreaClassCd();
            master.setSystAreaClassCd(Integer.toString(Integer.parseInt(seq) + 1));
        }

        insertExecHistory(master);

//        // 상세 이력 등록
//        for(ExecProg execProg : execProgList){
//            ExecHistory detail = ExecHistory.newDetail(execProg, master);
//            insertExecHistory(detail);
//        }
        return master;

    }

//    public void insertExecHistory(ExecHistory execHistory) {
//        ehMapper.insertExecHistory(execHistory);
//    }
    public void insertExecHistory(TBIBD760 tbibd760) {
        tbibd760Mapper.insertExecHistory(tbibd760);
    }

//    public void updateExecHistory(ExecHistory execHistory) {
//        ehMapper.updateExecHistory(execHistory);
//    }
    public void updateExecHistory(TBIBD760 tbibd760) {
        tbibd760Mapper.updateExecHistory(tbibd760);
    }

    /**
     * 트리거가 당일 실행됬는지 확인
     */
    public boolean isFiredToday(String triggerGroup, String triggerName) {
        TBIBD760 lastMaster = readLastMasterExecHistory(triggerGroup, triggerName);
        if(lastMaster == null) return false;

        String sttDt = lastMaster.getWorkDt();
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return today.equals(sttDt);
    }


}
