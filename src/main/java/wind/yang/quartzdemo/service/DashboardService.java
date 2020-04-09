package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.code.JobExecutionStatusCode;
import wind.yang.quartzdemo.dto.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DashboardService {
    @Autowired TriggerGroupService triggerGroupService;
    @Autowired ExecProgService execProgService;
    @Autowired ExecHistoryService execHistoryService;

    @Value("${quartzdemo.shell.script-path}")
    protected String SH_PATH;

    @Value("${quartzdemo.shell.log-path}")
    private String LOG_PATH;

    public String getPopupData(String target, String progName) {
        String contents = "";
        if(target.equals("execProg")) {
            contents = getExecProgContents(progName);
        }else if (target.equals("execLog")) {
            contents = getExecLog(progName);
        }
//        log.info("Popup 화면에 전달한 컨텐츠 : {}", contents);
        return contents;
    }

    public String getExecProgContents(String execProgName){
        return getContentsFromFile(SH_PATH, execProgName);
    }

    public String getExecLog(String execLogName) {
        return getContentsFromFile(LOG_PATH, execLogName + ".log");
    }

    private String getContentsFromFile(String path, String name){
        String contents = "";
        FileSystemResource fileSystemResource = new FileSystemResource(path + name);
        try {
            contents = IOUtils.toString(new FileReader(fileSystemResource.getFile()));
        } catch (IOException e) {
            contents = "내용이 없거나 파일을 찾을 수 없습니다.";
            log.error("읽어올 파일을 찾을 수 없습니다. : " + path + name, e);
        }
        return contents;
    }

    public List<TriggerGroup> getTriggerGroup(String active) {
        return triggerGroupService.findByTriggerGroup(active);
    }

    public List<JobResponse> getTriggerMaster(List<JobResponse> jobResponseList) {
        for (JobResponse jobResponse : jobResponseList) {
            ExecProg execProgSample = new ExecProg();

            // 검색 조건을 넣어준다
            execProgSample.setTriggerName(jobResponse.getTriggerName());
            execProgSample.setTriggerGroup(jobResponse.getTriggerGroup());
            execProgSample.setSeq(0);

            ExecHistory execHistory = execHistoryService.readLastExecHistory(execProgSample);
            if (execHistory != null) {
                String triggerExecStaCd = execHistory.getJobExecStaCd().toString();
                log.info("{} => triggerExecStaCd : {}", jobResponse.getTriggerName(), triggerExecStaCd);
                jobResponse.setTriggerExecStaCd(triggerExecStaCd);
            }else {
                jobResponse.setTriggerExecStaCd(JobExecutionStatusCode.READY.toString());
            }
        }
        return jobResponseList;
    }

    public List<ExecProgAndHistory> getExecJobAndHistory(JobRequest jobRequest) {
        List<ExecProgAndHistory> execProgAndHistoryList = new ArrayList<>();

        // Job list 호출
        List<ExecProg> execProgList = execProgService.findByTrigger(new TriggerKey(jobRequest.getTriggerName(), jobRequest.getTriggerGroup()));

        for (ExecProg execProg : execProgList) {
            ExecHistory execHistory = execHistoryService.readLastExecHistory(execProg);
            ExecProgAndHistory execProgAndHistory;
            if (execHistory != null) {
                execProgAndHistory = new ExecProgAndHistory(execProg.getTriggerGroup(), execProg.getTriggerName(), execProg.getSeq(), execProg.getProgramName(),
                        execProg.getSummary(), execProg.getDescription(), execProg.getExecParam1(), execProg.getExecParam2(), execProg.getExecParam3(),
                        execHistory.getTriggerSttDtm(), execHistory.getJobSttDtm(), execHistory.getJobEndDtm(), execHistory.getJobGroup(), execHistory.getJobName(),
                        execHistory.getJobExecStaCd(), execHistory.getJobExecRslt());

            } else {
                execProgAndHistory = new ExecProgAndHistory(execProg.getTriggerGroup(), execProg.getTriggerName(), execProg.getSeq(), execProg.getProgramName(),
                        execProg.getSummary(), execProg.getDescription(), execProg.getExecParam1(), execProg.getExecParam2(), execProg.getExecParam3(),
                        JobExecutionStatusCode.READY, "DEFAULT_JOB");
            }
            execProgAndHistoryList.add(execProgAndHistory);
        }

        return execProgAndHistoryList;
    }
}
