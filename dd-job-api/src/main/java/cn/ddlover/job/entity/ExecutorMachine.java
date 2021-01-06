package cn.ddlover.job.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/1 17:29
 *
 * 负责执行的机器的信息
 */
@Getter
@Setter
public class ExecutorMachine implements Serializable {

  private Long executorMachineId;

  private Long executorId;

  private String ip;

  private Integer port;

  /**
   * 机器在线状态
   * 0 - 掉线
   * 1 - 在线
   */
  private Integer status;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ExecutorMachine that = (ExecutorMachine) o;

    if (!ip.equals(that.ip)) {
      return false;
    }
    return port.equals(that.port);
  }

  @Override
  public int hashCode() {
    int result = ip.hashCode();
    result = 31 * result + port.hashCode();
    return result;
  }
}
