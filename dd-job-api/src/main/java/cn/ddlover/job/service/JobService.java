package cn.ddlover.job.service;

import cn.ddlover.job.annotation.RpcInvoke;
import cn.ddlover.job.entity.Job;
import java.util.List;
import javax.xml.ws.Response;

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
   * @param jobList 任务列表
   * @return 成功或者异常
   */
  public Response<Void> registerJobs(List<Job> jobList);

  /**
   * 触发任务
   *
   * @param job 对应的任务
   * @param arg 需要传递的参数
   * @return 成功或者异常
   */
  public Response<Void> invokeJob(Job job, String arg);
}
