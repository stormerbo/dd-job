package cn.ddlover.job.service;

import cn.ddlover.job.entity.Executor;
import cn.ddlover.job.entity.ExecutorMachine;
import cn.ddlover.job.entity.ListExecutorReq;
import cn.ddlover.job.entity.Response;
import cn.ddlover.job.entity.requst.ExecutorRegisterReq;
import cn.ddlover.job.mapper.ExecutorMapper;
import java.util.List;
import java.util.Objects;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/2 15:31
 */
@Service
public class ExecutorManageServiceImpl implements ExecutorService {

  @Autowired
  private ExecutorMapper executorMapper;
  @Autowired
  private RedissonClient client;

  /**
   * 添加处理器
   */
  public void addExecutor(ExecutorRegisterReq executorRegisterReq) {
    Executor executor = executorRegisterReq.getExecutor();
    ExecutorMachine executorMachine = executorRegisterReq.getExecutorMachine();
    Executor dbExecutor = executorMapper.selectByExecutorName(executor.getExecutorName());
    // 先判断当前执行器有没有注册，
    if (Objects.isNull(dbExecutor)) {
      executorMapper.insert(executor);
    }
    executorMachine.setExecutorId(dbExecutor.getExecutorId());
    List<ExecutorMachine> executorMachineList = client.getList("executor:list:" + executor.getExecutorName());
    boolean registered = executorMachineList.stream().anyMatch(machine -> machine.equals(executorMachine));
    // 判断机器是否已经入库了
    if (!registered) {
      executorMachineList.add(executorMachine);
    }
  }

  @Override
  public Response<Void> registerExecutor(ExecutorRegisterReq executorRegisterReq) {
    addExecutor(executorRegisterReq);
    return Response.success();
  }

  public List<Executor> listExecutor(ListExecutorReq listExecutorReq) {
    List<Executor> executorList = executorMapper.listExecutor(listExecutorReq);
    executorList.forEach(executor -> {
      List<ExecutorMachine> executorMachineList = client.getList("executor:list:" + executor.getExecutorName());
      executor.setExecutorMachineList(executorMachineList);
    });
    return executorList;
  }

  public Integer countExecutor(ListExecutorReq listExecutorReq) {
    return executorMapper.countExecutor(listExecutorReq);
  }
}
