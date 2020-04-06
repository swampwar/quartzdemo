package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;

@Slf4j
@Service
public class DashboardService {
    @Value("${quartzdemo.shell.script-path}")
    protected String SH_PATH;

    @Value("${quartzdemo.shell.log-path}")
    private String LOG_PATH;

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
            log.error("읽어올 파일을 찾을 수 없습니다. : " + path + name, e);
        }
        return contents;
    }
}
