package cn.ddlover.job.util;

import cn.ddlover.job.domain.JobHolder;
import cn.ddlover.job.entity.Job;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/5 16:22
 * 定时任务注册器
 * 用于注册所有的定时任务
 */
public class JobRegistry {

  private static final Map<String, JobHolder> JOB_REGISTRY = new ConcurrentHashMap<>();

  public static void registryJob(String name, JobHolder jobHolder) {
    JOB_REGISTRY.put(name, jobHolder);
  }

  public static void registryJob(JobHolder jobHolder) {
    JOB_REGISTRY.put(jobHolder.getJob().getJobName(), jobHolder);
  }

  public static List<Job> getAllJob() {
    return JOB_REGISTRY.values().stream().map(JobHolder::getJob).collect(Collectors.toList());
  }

  public static JobHolder getJobHolder(String name){
    return JOB_REGISTRY.get(name);
  }
}
