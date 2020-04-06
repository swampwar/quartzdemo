package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import wind.yang.quartzdemo.dto.JobRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest
public class DashboardServiceTest {
    @Autowired
    DashboardService dashboardService;

    @Value("${quartzdemo.shell.script-path}")
    protected String SH_PATH;

    @Value("${quartzdemo.shell.log-path}")
    private String LOG_PATH;

    String testFileName = "testfile.sh";
    String extension = ".log";

    @After
    public void clean(){
        new File(SH_PATH + testFileName).delete();
        new File(LOG_PATH + testFileName + extension).delete();
    }

    @Test
    public void 실행프로그램을_스트링으로_반환한다_Test() throws IOException {
        // given
        String expectedContents = "HI HELLO BYE";
        IOUtils.write(expectedContents,
                new FileOutputStream(SH_PATH + testFileName, false),
                StandardCharsets.UTF_8);

        // when
        String execProgContents = dashboardService.getExecProgContents(testFileName);
        log.info("조회된 실행프로그램 내용 : {}", execProgContents);

        // then
        Assert.assertEquals(expectedContents, execProgContents);

        // clean
        new File(SH_PATH + testFileName).delete();
    }

    @Test
    public void 실행로그를_스트링으로_반환한다_Test() throws IOException {
        // given
        String expectedContents = "HI HELLO BYE LOG";
        IOUtils.write(expectedContents,
                new FileOutputStream(LOG_PATH + testFileName + extension, false),
                StandardCharsets.UTF_8);

        // when
        String execLogContents = dashboardService.getExecLog(testFileName);
        log.info("조회된 실행로그 내용 : {}", execLogContents);

        // then
        Assert.assertEquals(expectedContents, execLogContents);

        // clean
        new File(LOG_PATH + testFileName + extension).delete();
    }
}