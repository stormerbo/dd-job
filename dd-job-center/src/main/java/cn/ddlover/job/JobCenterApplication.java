package cn.ddlover.job;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/1 16:33
 */

@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConfigurationPropertiesScan
@MapperScan
@SpringBootApplication
public class JobCenterApplication {

  public static void main(String[] args) {
    SpringApplication.run(JobCenterApplication.class);
    log.info("Job Center Start success");
  }

}
