package wind.yang.quartzdemo.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.*;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import wind.yang.quartzdemo.dto.ExecProg;
import wind.yang.quartzdemo.service.QuartzService;

import java.io.*;

@Slf4j
public class SampleCronJob implements Job, InterruptableJob{ // TODO QuartzJobBean 을 구현하여 Job 작성
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

    private TriggerKey triggerKey = null;
    private String fileName;
    private int exitCode = -1;
    private static final int SUCCESS = 0;
    private DefaultExecutor executor = null;

    public SampleCronJob() {
        log.debug("SampleCronJob 인스턴스가 생성!");
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        this.triggerKey = jobExecutionContext.getTrigger().getKey();
        this.fileName = ((ExecProg)jobExecutionContext.getJobDetail().getJobDataMap().get("execProg")).getProgramName();
        log.info("Trigger[{}]에 의해 실행예정인 프로그램[{}]", triggerKey, fileName);

        // Trigger가 실행중인지 체크
        try {
            int runningTriggerCnt = service.getRunningTriggerCnt(triggerKey);
            if(runningTriggerCnt > 1){ // 본인을 제외하고 실행중인 Job이 있으면
                log.error("Trigger[{}]가 이미 실행중이므로 Job을 종료합니다.", triggerKey);
                throw new JobExecutionException("실행할 Trigger가 이미 실행중입니다. 실행 건수 : " + runningTriggerCnt);
            }
        } catch (SchedulerException e) {
            log.error("Trigger[{}] 실행여부 조회 중 에러발생으로 Job을 종료합니다.", triggerKey);
            throw new JobExecutionException(e);
        }

        // Trigger 실행
        try {
            // TODO 실행할 스크립트가 classpath에 있으면 안될 것 같다... 업로드시 classpath에만 저장된다. 소스 재배포시 없어짐! 제3의 폴더로 옮기자
            String scriptFileFullPath = new ClassPathResource(SH_PATH + fileName).getFile().getAbsolutePath(); // 실행할 쉘스크립트
            String command = CMD + " " + scriptFileFullPath; // 실행할 Command

            try(FileOutputStream os = new FileOutputStream(new File(LOG_PATH + fileName + ".log"), true)) {
                exitCode = executeShell(command, os, os); // 쉘스크립트 실행
            }
        } catch (IOException e) {
            log.error("Trigger[{}] 쉘스크립트에 대한 Path를 찾을 수 없습니다. : [{}]", triggerKey, e.getMessage());
            throw new JobExecutionException(e);
        }

        log.info("Trigger[{}] 실행이 완료되었습니다. exitCode : {}", triggerKey, exitCode);
        // TODO 0만 정상인가??
        if(exitCode != SUCCESS){
            throw new JobExecutionException("Trigger[" + triggerKey +"] Job 실행결과(exitCode)가 0이 아닙니다. exitCode : " + exitCode);
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        if(executor != null){
            executor.getWatchdog().destroyProcess(); // 자식 프로세스 종료
            log.info("Trigger[{}] 강제종료가 완료되었습니다.", triggerKey);
        }
    }

    public int executeShell(String command, OutputStream out, OutputStream err) throws JobExecutionException {
        executor = new DefaultExecutor();
        CommandLine cmdLine = CommandLine.parse(command);
        log.info("Trigger[{}] 실행을 위해 파싱된 CommandLine : [{}] 실행시작!", triggerKey, cmdLine);
        if (timeoutMillsecs > 0) {
            ExecuteWatchdog watchdog = new ExecuteWatchdog(timeoutMillsecs);
            executor.setWatchdog(watchdog);
        }

        ExecuteStreamHandler streamHandler = new PumpStreamHandler(out, err);
        executor.setStreamHandler(streamHandler);

        try {
            return executor.execute(cmdLine);
        } catch (ExecuteException e) {
            log.info("Trigger[{}] 쉘스크립트 실행 중 에러[{}]가 발생했습니다. 실행결과(exitCode)[{}]", triggerKey, e.getMessage(), e.getExitValue());
            throw new JobExecutionException(e);
        } catch (IOException e) {
            log.info("Trigger[{}] 쉘스크립트 실행을 위한 상태가 아닙니다. [{}]", triggerKey, e.getMessage());
            throw new JobExecutionException(e);
        }
    }


}