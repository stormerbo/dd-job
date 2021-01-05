package cn.ddlover.job.entity;


import lombok.Getter;
import lombok.Setter;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/1 16:26
 *
 * 对一个 job 的封装
 */
@Getter
@Setter
public class Job {

  /**
   * 主键id
   */
  private Long jobId;

  /**
   * 对应的执行的id
   */
  private Long executorId;

  /**
   * job的名称
   */
  private String jobName;

  /**
   * 路由策略
   * @see cn.ddlover.job.constant.RouteTypeEnum
   */
  private Integer routeType;
  /**
   * 对应的job的cron表达式
   */
  private String cron;

  /**
   * 对job的简要描述
   */
  private String desc;

  /**
   * 任务超时时间
   */
  private Long timeout;
  /**
   * 失败重试次数
   */
  private Integer retryTime;

  /**
   * 负责人
   */
  private String owner;

  /**
   * 服务报警邮箱
   * 多个email地址逗号分开
   */
  private String warningEmail;

  /**
   * 任务的参数，可选
   */
  private String jobParam;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Job job = (Job) o;

    return jobName.equals(job.jobName);
  }

  @Override
  public int hashCode() {
    return jobName.hashCode();
  }
}
