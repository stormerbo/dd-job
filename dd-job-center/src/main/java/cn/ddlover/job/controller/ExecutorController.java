package cn.ddlover.job.controller;

import cn.ddlover.job.entity.Executor;
import cn.ddlover.job.entity.Response;
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

  @PutMapping("executor")
  public Response<Void> addExecutor(@RequestBody Executor executor) {

    return Response.success();
  }
}
