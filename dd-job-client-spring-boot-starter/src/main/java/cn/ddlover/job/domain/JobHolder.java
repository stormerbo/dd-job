package cn.ddlover.job.domain;

import cn.ddlover.job.entity.Job;
import lombok.Getter;
import lombok.Setter;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/6 10:38
 */
@Getter
@Setter
public class JobHolder {
  private Job job;

  private Class<?> jobClass;

  private String methodName;
}
