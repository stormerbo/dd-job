package cn.ddlover.job.util;

import cn.ddlover.job.entity.Job;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/5 16:22
 * 定时任务注册器
 * 用于注册所有的定时任务
 */
public class JobRegistry {

  private static final Map<String, Job> JOB_REGISTRY = new ConcurrentHashMap<>();

  public static void registryJob(String name, Job job) {
    JOB_REGISTRY.put(name, job);
  }

  public static void registryJob(Job job) {
    JOB_REGISTRY.put(job.getJobName(), job);
  }

  public static  List<Job> getAllJob() {
    return new ArrayList<>(JOB_REGISTRY.values());
  }
}
