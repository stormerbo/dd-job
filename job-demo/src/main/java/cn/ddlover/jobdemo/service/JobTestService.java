package cn.ddlover.jobdemo.service;

import cn.ddlover.job.annotation.JobProvider;
import org.springframework.stereotype.Service;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/5 15:57
 */
@Service
public class JobTestService {

  @JobProvider(value = "0 0 0/1 * * ?", owner = "stormer.xia")
  public void testJobAnnotation(String param) {
    System.out.println("testJobAnnotation");
  }
}
