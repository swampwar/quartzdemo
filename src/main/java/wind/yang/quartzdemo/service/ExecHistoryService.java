package wind.yang.quartzdemo.service;

import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.ExecHistory;
import wind.yang.quartzdemo.dto.ExecProg;
import wind.yang.quartzdemo.mapper.ExecHistoryMapper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class ExecHistoryService {
    @Autowired
    ExecHistoryMapper ehMapper;

    public List<ExecHistory> readExecHistoryAll() {
        List<ExecHistory> historyList = null;
        historyList = ehMapper.findAll();
        return historyList;
    }

    public List<ExecHistory> readDetailAllExecHistory(String startDate, String endDate, String triggerGroup, String triggerName) {
        return ehMapper.findDetailAll(startDate, endDate, triggerGroup, triggerName);
    }

    public List<ExecHistory> readLastAllExecHistory(String group, String name) {
        ExecHistory lastMaster = readLastMasterExecHistory(group, name);
        ExecHistory searchParam = ExecHistory.builder()
                                            .triggerGroup(group)
                                            .triggerName(name)
                                            .triggerSttDtm(lastMaster.getTriggerSttDtm())
                                            .execProgSeq(-1)
                                            .build();
        return ehMapper.findByExecHistory(searchParam);
    }

    public List<ExecHistory> readLastDetailExecHistory(String group, String name) {
        List<ExecHistory> all = readLastAllExecHistory(group, name);
        all.remove(0); // 마스터이력 제외

        return all;
    }

    public ExecHistory readLastMasterExecHistory(String group, String name){
        ExecHistory searchParam = ExecHistory.builder()
                                            .triggerGroup(group)
                                            .triggerName(name)
                                            .execProgSeq(0)
                                            .build();
        return ehMapper.findLastExecHistory(searchParam);
    }

    public ExecHistory insertStartExecHistory(TriggerKey triggerKey, JobKey jobKey, List<ExecProg> execProgList) {
        // 마스터 이력 등록
        ExecHistory master = ExecHistory.newMaster(triggerKey, jobKey);
        insertExecHistory(master);

        // 상세 이력 등록
        for(ExecProg execProg : execProgList){
            ExecHistory detail = ExecHistory.newDetail(execProg, master);
            insertExecHistory(detail);
        }
        return master;

    }

    public void insertExecHistory(ExecHistory execHistory) {
        ehMapper.insertExecHistory(execHistory);
    }

    public void updateExecHistory(ExecHistory execHistory) {
        ehMapper.updateExecHistory(execHistory);
    }

    /**
     * 트리거가 당일 실행됬는지 확인
     */
    public boolean isFiredToday(String triggerGroup, String triggerName) {
        ExecHistory lastMaster = readLastMasterExecHistory(triggerGroup, triggerName);
        if(lastMaster == null) return false;

        String sttDt = lastMaster.getTriggerSttDtm().substring(0, 8);
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return today.equals(sttDt);
    }


}
