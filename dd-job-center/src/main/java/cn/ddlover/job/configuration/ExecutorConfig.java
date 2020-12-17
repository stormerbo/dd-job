package cn.ddlover.job.configuration;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author stormer.xia
 * createTime 2019/9/4 11:50 上午
 */
@Configuration
public class ExecutorConfig {


  @Bean
  public Executor issueExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(100);
    executor.setMaxPoolSize(100);
    executor.setQueueCapacity(1024);
    executor.setThreadNamePrefix("common-executor-");
    executor.initialize();
    return executor;
  }
}
