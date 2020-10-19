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
import java.util.List;

@Slf4j
@Service
public class DashboardService {
    @Autowired private TriggerGroupService triggerGroupService;
    @Autowired private ExecProgService execProgService;
    @Autowired private ExecHistoryService execHistoryService;
    @Autowired private QuartzService quartzService;
    @Autowired private JobScheduleService jobScheduleService;

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


    public List<ExecHistory> getExecHistoryList(JobRequest jobRequest) {
        List<ExecHistory> historyList;
        boolean isFiredToday = execHistoryService.isFiredToday(jobRequest.getTriggerGroup(), jobRequest.getTriggerName());

        if(isFiredToday){ // 마지막 실행이력(TB_EXEC_HISTORY)을 보여준다.
            historyList = execHistoryService.readLastDetailExecHistory(jobRequest.getTriggerGroup(), jobRequest.getTriggerName());
        }else{ // 실행프로그램(TB_EXEC_PROG)기준 실행될 계획을 보여준다.
            List<ExecProg> execProgs = execProgService.findByTrigger(new TriggerKey(jobRequest.getTriggerName(), jobRequest.getTriggerGroup()));
            historyList = new ArrayList<>();
            for(ExecProg execProg : execProgs){
                historyList.add(ExecHistory.of(execProg));
            }
        }

        return historyList;
    }

    /**
     * Dashboard에 출력할 트리거 정보를 조회한다.
     */
    public List<JobResponse> readTriggers(String triggerGroup) {
        // 스케쥴러의 트리거 정보를 조회
        List<JobResponse> jobResponseList = quartzService.readTriggersByTriggerGroup(triggerGroup);
        List<JobResponse> removeForceJob = new ArrayList<>();

        // 트리거의 최종 실행이력을 조회
        for(JobResponse job : jobResponseList){
            if (!"PAUSED".equals(job.getTriggerStatus())) {
                ExecHistory lastMaster = execHistoryService.readLastMasterExecHistory(job.getTriggerGroup(), job.getTriggerName());
                if (lastMaster != null) {
                    job.setTriggerExecStaCd(lastMaster.getJobExecStaCd().toString());
                } else {
                    // FORCE인 경우 제외
                    if (job.getTriggerName().indexOf(".") != -1) {
                        removeForceJob.add(job);
                    }
                    job.setTriggerExecStaCd(JobExecutionStatusCode.READY.toString());
                }
            }else {
                job.setTriggerExecStaCd(job.getTriggerStatus());
            }
        }
        // FORCE인 경우 제외
        if (removeForceJob.size() != 0) {
            for (JobResponse forceJob : removeForceJob) {
                jobResponseList.remove(forceJob);
            }
        }

        return jobResponseList;
    }

    public String dbtest() {
        return jobScheduleService.dbtest();
    }
}
