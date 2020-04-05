package wind.yang.quartzdemo.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobListener;
import org.quartz.TriggerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import wind.yang.quartzdemo.code.JobExecutionStatusCode;
import wind.yang.quartzdemo.dto.ExecHistory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@Slf4j
public class ExecHistoryMapperTest {
    @Autowired
    ExecHistoryMapper mapper;
    List<ExecHistory> delList = new ArrayList<>();

    @After
    public void del(){
        for(ExecHistory execHistory : delList){
            mapper.deleteExecHistory(execHistory);
        }
    }

    @Test
    public void 등록_테스트(){
        // given
        ExecHistory insertHist = getExecHistory();

        // when
        mapper.insertExecHistory(insertHist);
        delList.add(insertHist);

        // then
        ExecHistory searchCon = ExecHistory.builder()
                .triggerSttDtm(insertHist.getTriggerSttDtm())
                .triggerGroup(insertHist.getTriggerGroup())
                .triggerName(insertHist.getTriggerName())
                .execProgSeq(insertHist.getExecProgSeq())
                .build();

        List<ExecHistory> histories = mapper.findByExecHistory(searchCon);
        Assert.assertEquals(1, histories.size());
    }

    @Test
    public void 수정_테스트(){
        // given
        ExecHistory insertHist = getExecHistory();
        mapper.insertExecHistory(insertHist);
        delList.add(insertHist);

        // when
        insertHist.setJobSttDtm("20200402000000");
        insertHist.setJobEndDtm("20200402000001");
        insertHist.setJobExecStaCd(JobExecutionStatusCode.SUCCESS);
        insertHist.setJobExecRslt("정상");
        mapper.updateExecHistory(insertHist);

        // then
        ExecHistory searchCon = ExecHistory.builder()
                .triggerSttDtm(insertHist.getTriggerSttDtm())
                .triggerGroup(insertHist.getTriggerGroup())
                .triggerName(insertHist.getTriggerName())
                .execProgSeq(insertHist.getExecProgSeq())
                .build();
        List<ExecHistory> histories = mapper.findByExecHistory(searchCon);
        Assert.assertEquals(1, histories.size());

        ExecHistory execHistory = histories.get(0);
        assertEquals(JobExecutionStatusCode.SUCCESS, execHistory.getJobExecStaCd());
        assertEquals(insertHist.getJobSttDtm(), execHistory.getJobSttDtm());
        assertEquals(insertHist.getJobEndDtm(), execHistory.getJobEndDtm());
        assertEquals(insertHist.getJobExecRslt(), execHistory.getJobExecRslt());
    }

    public ExecHistory getExecHistory(){
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        ExecHistory execHistory = ExecHistory.builder()
                .triggerSttDtm(now)
                .triggerGroup("TEST_GROUP")
                .triggerName("TEST_NAME")
                .jobGroup("TEST_JOB_GROUP")
                .jobName("TEST_JOB_NAME")
                .execProgSeq(1)
                .jobSttDtm(null)
                .jobExecStaCd(JobExecutionStatusCode.START)
                .build();
        return execHistory;
    }
}
