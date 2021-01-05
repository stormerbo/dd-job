package cn.ddlover.job.service;

import cn.ddlover.job.annotation.RpcInvoke;
import cn.ddlover.job.entity.Job;
import cn.ddlover.job.entity.Response;
import java.util.List;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/11 15:11
 */
@RpcInvoke
public interface JobService {

  /**
   * 批量注册job
   *
   * @param executorName 执行器的名称，用于注册时的筛选
   * @param jobList      任务列表
   * @return 成功或者异常
   */
  public Response<Void> registerJobs(String executorName, List<Job> jobList);

  /**
   * 暂时未前端所使用的 触发任务 的功能
   *
   * @param job 对应的任务
   * @param arg 需要传递的参数
   * @return 成功或者异常
   */
  public Response<Void> invokeJob(Job job, String arg);
}
