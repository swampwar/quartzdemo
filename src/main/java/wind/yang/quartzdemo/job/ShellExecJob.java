package wind.yang.quartzdemo.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.*;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import wind.yang.quartzdemo.dto.ExecProg;
import wind.yang.quartzdemo.dto.TBIBD760;
import wind.yang.quartzdemo.dto.TBIBM710;
import wind.yang.quartzdemo.dto.TBIBM713;
import wind.yang.quartzdemo.service.JobScheduleService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class ShellExecJob extends BaseJob {
    private String fileName;
    private int exitCode = -1;
    private static final int SUCCESS = 0;
    @Autowired
    JobScheduleService jobScheduleService;

    @Override
    protected void executeInternal(TBIBD760 tbibd760) throws JobExecutionException {
        fileName = tbibd760.getSettmJobGroupId();
        log.info("Trigger[{}]에 의해 실행예정인 프로그램[{}]", triggerKey, fileName);
        log.debug("TBIBD760 : {}", tbibd760);

        // Trigger 실행
        try {
            String scriptFileFullPath = tbibd760.getWorkDesc();
            String command = CMD + " " + scriptFileFullPath; // 실행할 Command
//            command = addParameters(command, execProg);

            /** 2020.09.14 - 쉘 스크립트 실행하는 소스 대신 DB에 저장하는 소스로 변경*/
//            try(FileOutputStream os = new FileOutputStream(new File(LOG_PATH + fileName + ".log"), true)) {
//                exitCode = executeShell(command, os, os); // 쉘스크립트 실행
//            }
            exitCode = jobScheduleService.checkJobHistroy(tbibd760);

        } catch (Exception e) {
            log.error("Trigger[{}] 쉘스크립트에 대한 Path를 찾을 수 없습니다. : [{}]", triggerKey, e.getMessage());
            throw new JobExecutionException(e);
        }

        log.info("Trigger[{}] 실행이 완료되었습니다. exitCode : {}", triggerKey, exitCode);
        // TODO 0만 정상인가??
        if(exitCode != SUCCESS){
            throw new JobExecutionException("Trigger[" + triggerKey +"] Job 실행결과(exitCode)가 0이 아닙니다. exitCode : " + exitCode);

        }
    }

    private String addParameters(String command, ExecProg execProg) {
        StringBuilder builder = new StringBuilder(command);
        addParameter(builder, execProg.getExecParam1());
        addParameter(builder, execProg.getExecParam2());
        addParameter(builder, execProg.getExecParam3());
        return builder.toString();
    }

    private void addParameter(StringBuilder builder, String execParam) {
        if(!StringUtils.isEmpty(execParam)){
            builder.append(" " + execParam);
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
