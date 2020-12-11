package cn.ddlover.job.service;

import cn.ddlover.job.entity.Executor;
import cn.ddlover.job.entity.ExecutorMachine;
import cn.ddlover.job.entity.Response;
import cn.ddlover.job.entity.requst.ExecutorRegisterReq;
import cn.ddlover.job.mapper.ExecutorMachineMapper;
import cn.ddlover.job.mapper.ExecutorMapper;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/2 15:31
 */
@Service
public class ExecutorManageService implements ExecutorService {

  @Autowired
  private ExecutorMapper executorMapper;
  @Autowired
  private ExecutorMachineMapper executorMachineMapper;

  /**
   * 添加处理器
   */
  public void addExecutor(ExecutorRegisterReq executorRegisterReq) {
    Executor executor = executorRegisterReq.getExecutor();
    ExecutorMachine executorMachine = executorRegisterReq.getExecutorMachine();
    Executor dbExecutor = executorMapper.selectByExecutorName(executor.getExecutorName());
    boolean registered = false;
    // 先判断当前执行器有没有注册，
    if (Objects.isNull(dbExecutor)) {
      executorMapper.insert(executor);
      executorMachine.setExecutorId(executor.getExecutorId());
    } else {
      List<ExecutorMachine> executorMachineList = dbExecutor.getExecutorMachineList();
      registered = executorMachineList.stream().anyMatch(machine -> machine.equals(executorMachine));

    }
    // 判断机器是否已经入库了
    if (!registered) {
      executorMachineMapper.insert(executorMachine);
    }
  }

  @Override
  public Response<Void> registerExecutor(ExecutorRegisterReq executorRegisterReq) {
    addExecutor(executorRegisterReq);
    return Response.success();
  }
}
