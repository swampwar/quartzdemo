package wind.yang.quartzdemo.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.*;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import wind.yang.quartzdemo.quartz.service.QuartzService;

import java.io.*;

@Slf4j
public class SampleCronJob implements Job { // TODO QuartzJobBean 을 구현하여 Job 작성
    @Value("${quartzdemo.shell.timeout}")
    private int timeoutMillsecs;

    @Value("${quartzdemo.shell.cmd}")
    private String CMD;

    @Value("${quartzdemo.shell.script-path}")
    private String SH_PATH;

    @Value("${quartzdemo.shell.log-path}")
    private String LOG_PATH;

    @Autowired
    QuartzService service;

    private String fileName;
    private int exitCode = -1;

    public SampleCronJob() {
        log.debug("SampleCronJob 인스턴스가 생성!");
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        TriggerKey triggerKey = jobExecutionContext.getTrigger().getKey();
        fileName = jobExecutionContext.getJobDetail().getJobDataMap().getString("shFileName");
        log.info("Trigger[{}]에 의해 실행예정인 CMD[{}]", triggerKey, fileName);

        // Trigger가 실행중인지 체크
        try {
            int runningTriggerCnt = service.getRunningTriggerCnt(triggerKey);
            if(runningTriggerCnt > 1){ // 본인을 제외하고 실행중인 Job이 있으면
                log.error("Trigger[{}]가 이미 실행중이므로 종료합니다.", triggerKey);
                // TODO 실행결과에 대한 DB처리, 메세지와 코드를 저장하자.
                return;
            }
        } catch (SchedulerException e) {
            log.error("Trigger[{}] 실행여부 조회 중 에러발생으로 Job을 종료합니다.", triggerKey);
            // TODO 실행결과에 대한 DB처리, 메세지와 코드를 저장하자.
            return;
        }

        // Trigger 실행
        try {
            String scriptFileFullPath = new ClassPathResource(SH_PATH + fileName).getFile().getAbsolutePath(); // 실행할 쉘스크립트
            String command = CMD + " " + scriptFileFullPath; // 실행할 Command

            try(FileOutputStream os = new FileOutputStream(new File(LOG_PATH + fileName + ".log"), true)) {
                exitCode = executeShell(command, os, os); // 쉘스크립트 실행
            } catch (IOException e) {
                log.error("쉘스크립트 로그파일 에러발생 : [{}]", e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            log.error("쉘스크립트에 대한 Path를 찾을 수 없습니다. : [{}]", e.getMessage());
            e.printStackTrace();
        }

        log.info("Trigger[{}] 실행이 완료되었습니다. exitCode : {}", triggerKey, exitCode);
        // TODO 실행결과에 대한 DB처리(exitCode에 따른 분기처리)
    }

    public int executeShell(String command, OutputStream out, OutputStream err) {
        int errorExitCode = -1;
        DefaultExecutor executor = new DefaultExecutor();
        CommandLine cmdLine = CommandLine.parse(command);
        log.info("실행을 위해 파싱된 CommandLine : [{}]", cmdLine);

        if (timeoutMillsecs > 0) {
            ExecuteWatchdog watchdog = new ExecuteWatchdog(timeoutMillsecs);
            executor.setWatchdog(watchdog);
        }

        ExecuteStreamHandler streamHandler = new PumpStreamHandler(out, err);
        executor.setStreamHandler(streamHandler);

        try {
            return executor.execute(cmdLine);
        } catch (ExecuteException e) {
            log.info("쉘스크립트 실행 중 에러[{}]가 발생했습니다. 실행결과(exitCode)[{}]", e.getMessage(), e.getExitValue());
            e.printStackTrace();
            return e.getExitValue();
        } catch (IOException e) {
            log.info("쉘스크립트 실행을 위한 상태가 아닙니다. [{}]", e.getMessage());
            e.printStackTrace();
            return errorExitCode;
        }
    }
}