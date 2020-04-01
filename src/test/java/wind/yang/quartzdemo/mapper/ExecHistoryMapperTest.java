package wind.yang.quartzdemo.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobListener;
import org.quartz.TriggerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wind.yang.quartzdemo.code.JobExecutionStatusCode;
import wind.yang.quartzdemo.dto.ExecHistory;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ExecHistoryMapperTest {
    @Autowired
    ExecHistoryMapper mapper;

    @Test
    public void 등록_테스트(){
        ExecHistory insertHist = new ExecHistory();
        insertHist.setJobSttDtm("20200317160101");
        insertHist.setJobEndDtm(null);
        insertHist.setTriggerGroup("TEST");
        insertHist.setTriggerName("TEST_TRIGGER1");
        insertHist.setJobGroup("TEST_GROUP");
        insertHist.setJobName("TEST_JOB");
        insertHist.setExecProgName("trigger1.sh");
        insertHist.setJobExecStaCd(JobExecutionStatusCode.READY);
        insertHist.setJobExecRslt("정상 시작");

        mapper.insertExecHistory(insertHist);

        ExecHistory searchCon = new ExecHistory();
        searchCon.setTriggerGroup("TEST");
        searchCon.setTriggerName("TEST_TRIGGER1");

        List<ExecHistory> histories = mapper.findByExecHistory(searchCon);
        Assert.assertEquals(1, histories.size());

        ExecHistory execHistory = histories.get(0);
        log.info("등록된 실행히스토리 : {}", execHistory);

        mapper.deleteExecHistory(insertHist);
    }

    @Test
    public void 수정_테스트(){
        ExecHistory insertHist = new ExecHistory();
        insertHist.setJobSttDtm("20200317160101");
        insertHist.setJobEndDtm(null);
        insertHist.setTriggerGroup("TEST");
        insertHist.setTriggerName("TEST_TRIGGER1");
        insertHist.setJobGroup("TEST_GROUP");
        insertHist.setJobName("TEST_JOB");
        insertHist.setExecProgName("trigger1.sh");
        insertHist.setJobExecStaCd(JobExecutionStatusCode.READY);
        insertHist.setJobExecRslt("정상 시작");

        mapper.insertExecHistory(insertHist);

        insertHist.setJobEndDtm("20200317160109");
        insertHist.setJobExecStaCd(JobExecutionStatusCode.SUCCESS);
        insertHist.setJobExecRslt("정상 종료");
        mapper.updateExecHistory(insertHist);

        ExecHistory searchCon = new ExecHistory();
        searchCon.setTriggerGroup("TEST");
        searchCon.setTriggerName("TEST_TRIGGER1");
        List<ExecHistory> histories = mapper.findByExecHistory(searchCon);
        Assert.assertEquals(1, histories.size());

        ExecHistory execHistory = histories.get(0);
        assertEquals(JobExecutionStatusCode.SUCCESS, execHistory.getJobExecStaCd());
        assertEquals("20200317160109", execHistory.getJobEndDtm());
        assertEquals("정상 종료", execHistory.getJobExecRslt());
        log.info("수정된 실행히스토리 : {}", execHistory);

        mapper.deleteExecHistory(insertHist);
    }
}
