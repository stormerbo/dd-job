package cn.ddlover.job.entity;

import cn.ddlover.job.constant.RegisterTypeEnum;
import java.io.Serializable;
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
public class Executor implements Serializable {

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
  private Integer registerType = RegisterTypeEnum.AUTO.getType();

  /**
   * 机器地址信息，包括ip和端口
   */
  private List<ExecutorMachine> executorMachineList;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Executor executor = (Executor) o;

    return executorName.equals(executor.executorName);
  }

  @Override
  public int hashCode() {
    return executorName.hashCode();
  }
}
