package cn.ddlover.job.service;

import cn.ddlover.job.entity.Executor;
import cn.ddlover.job.entity.Job;
import cn.ddlover.job.entity.Response;
import cn.ddlover.job.exception.ExecutorNotExistException;
import cn.ddlover.job.mapper.ExecutorMapper;
import cn.ddlover.job.mapper.JobMapper;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/5 16:38
 */
@Service
public class JobManageServiceImpl implements JobService {

  @Autowired
  private ExecutorMapper executorMapper;

  @Autowired
  private JobMapper jobMapper;

  @Override
  public Response<Void> registerJobs(String executorName, List<Job> jobList) {
    Executor executor = executorMapper.selectByExecutorName(executorName);
    if (Objects.isNull(executor)) {
      throw new ExecutorNotExistException();
    }
    List<Job> registeredJobList = jobMapper.listByExecutorId(executor.getExecutorId());
    jobList.forEach(job -> {
      if (CollectionUtils.isEmpty(registeredJobList) || !registeredJobList.contains(job)) {
        job.setExecutorId(executor.getExecutorId());
        jobMapper.insert(job);
      }
    });
    return Response.success();
  }

  @Override
  public Response<Void> invokeJob(Job job, String arg) {
    return null;
  }
}
