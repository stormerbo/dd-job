package cn.ddlover.job.entity;

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
public class ExecutorMachineInfo {

  private String ip;

  private Integer port;
}
