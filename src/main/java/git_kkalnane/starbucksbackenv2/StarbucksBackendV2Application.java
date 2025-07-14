package git_kkalnane.starbucksbackenv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class StarbucksBackendV2Application {

  public static void main(String[] args) {
    SpringApplication.run(StarbucksBackendV2Application.class, args);
  }

}
