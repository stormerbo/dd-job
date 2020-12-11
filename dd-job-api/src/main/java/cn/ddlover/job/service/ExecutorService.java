package cn.ddlover.job.service;

import cn.ddlover.job.entity.Response;
import cn.ddlover.job.entity.requst.ExecutorRegisterReq;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/11 15:10
 */
public interface ExecutorService {

  /**
   * 注册执行器，以及自身的ip端口信息
   */
  public Response<Void> registerExecutor(ExecutorRegisterReq executorRegisterReq);
}
