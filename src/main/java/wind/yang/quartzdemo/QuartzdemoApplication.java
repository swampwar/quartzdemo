package wind.yang.quartzdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import wind.yang.quartzdemo.quartz.TestScheduler;

@SpringBootApplication
public class QuartzdemoApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(QuartzdemoApplication.class);
		app.setWebApplicationType(WebApplicationType.SERVLET);
		app.run(args);
	}

}
