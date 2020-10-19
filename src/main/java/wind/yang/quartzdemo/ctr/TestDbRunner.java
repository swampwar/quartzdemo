package wind.yang.quartzdemo.ctr;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import wind.yang.quartzdemo.mapper.TBFZD999Mapper;

@Slf4j
@Component
public class TestDbRunner implements ApplicationRunner {
    @Autowired
    TBFZD999Mapper tbfzd999Mapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String result = "";
        int cnt = tbfzd999Mapper.testDb();
        log.debug("popup cnt : " + cnt);
        System.out.println("popup cnt : " + cnt);

    }
}
