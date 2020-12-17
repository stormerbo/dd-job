package cn.ddlover.jobdemo;

import cn.ddlover.job.annotation.EnableDdJobClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDdJobClient
@SpringBootApplication
public class JobDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(JobDemoApplication.class, args);
  }

}
