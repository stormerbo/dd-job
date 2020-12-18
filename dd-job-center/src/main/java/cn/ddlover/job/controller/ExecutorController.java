package cn.ddlover.job.controller;

import cn.ddlover.job.entity.Response;
import cn.ddlover.job.entity.requst.ExecutorRegisterReq;
import cn.ddlover.job.service.ExecutorManageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/1 17:32
 */
@RestController
public class ExecutorController {

  @Autowired
  private ExecutorManageServiceImpl executorManageService;

  /**
   * 注册 executor
   */
  @PutMapping("executor")
  public Response<Void> addExecutor(@RequestBody ExecutorRegisterReq executorRegisterReq) {
    executorManageService.addExecutor(executorRegisterReq);
    return Response.success();
  }
}
