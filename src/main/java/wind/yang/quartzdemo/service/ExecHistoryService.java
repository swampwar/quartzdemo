package wind.yang.quartzdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.ExecHistory;
import wind.yang.quartzdemo.mapper.ExecHistoryMapper;

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

    public void insertExecHistory(ExecHistory execHistory) {
        ehMapper.insertExecHistory(execHistory);
    }

    public void updateExecHistory(ExecHistory execHistory) {
        ehMapper.updateExecHistory(execHistory);
    }
}
