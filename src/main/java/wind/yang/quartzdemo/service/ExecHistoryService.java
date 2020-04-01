package wind.yang.quartzdemo.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import wind.yang.quartzdemo.code.JobExecutionStatusCode;
import wind.yang.quartzdemo.dto.ExecHistory;
import wind.yang.quartzdemo.dto.ExecProg;
import wind.yang.quartzdemo.mapper.ExecHistoryMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExecHistoryService {
    @Autowired
    ExecHistoryMapper ehMapper;

    public List<ExecHistory> readExecHistory() {
        List<ExecHistory> historyList = null;
        historyList = ehMapper.findAll();
        return historyList;
    }


    public ExecHistory readLastExecHistory(ExecProg execProg) {
        ExecHistory execHistory = null;
        execHistory = ehMapper.findLastExecHistory(execProg);
        return execHistory;
    }

    public ExecHistory insertStartExecHistory(TriggerKey triggerKey, JobKey jobKey, List<ExecProg> execProgList) {
        // 마스터 이력 등록
        ExecHistory master = ExecHistory.newMaster(triggerKey, jobKey);
        insertExecHistory(master);

        // 상세 이력 등록
        for(ExecProg execProg : execProgList){
            ExecHistory detail = ExecHistory.newDetail(master, execProg.getProgramName(), execProg.getSeq());
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



}
