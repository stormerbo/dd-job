package cn.ddlover.job.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/1 17:24
 *
 * 执行器，对执行的机器进行封装
 */
@Getter
@Setter
public class Executor {

  /**
   * 执行器的id
   */
  private Long executorId;

  /**
   * 执行器的名称
   */
  private String executorName;

  /**
   * 执行器简介
   */
  private String desc;

  /**
   * 执行器注册方式
   */
  private Integer registerType;

  /**
   * 机器地址信息，包括ip和端口
   */
  private List<ExecutorMachine> executorMachineList;
}
