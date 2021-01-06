package cn.ddlover.job.controller;

import cn.ddlover.job.entity.InvokeJobReq;
import cn.ddlover.job.entity.Response;
import cn.ddlover.job.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/6 17:27
 */
@RestController
public class JobController {

  @Autowired
  private JobService jobService;

  @PostMapping("/invoke-job")
  public Response<Void> invokeJob(@RequestBody InvokeJobReq invokeJobReq) throws InterruptedException {
    return jobService.invokeJob(invokeJobReq.getJobId(), invokeJobReq.getParam());
  }
}
