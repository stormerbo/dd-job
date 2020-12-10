package cn.ddlover.job.entity.requst;

import cn.ddlover.job.entity.Executor;
import cn.ddlover.job.entity.ExecutorMachine;
import lombok.Getter;
import lombok.Setter;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/10 18:48
 */
@Getter
@Setter
public class ExecutorRegisterReq {

  private Executor executor;

  private ExecutorMachine executorMachine;
}
