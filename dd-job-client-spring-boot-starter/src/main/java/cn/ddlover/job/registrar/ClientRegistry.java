package cn.ddlover.job.registrar;

import cn.ddlover.job.entity.Executor;
import cn.ddlover.job.entity.ExecutorMachine;
import cn.ddlover.job.entity.requst.ExecutorRegisterReq;
import cn.ddlover.job.properties.ClientProperties;
import cn.ddlover.job.service.ExecutorService;
import cn.ddlover.job.service.JobService;
import cn.ddlover.job.util.GlobalChannel;
import cn.ddlover.job.util.JobRegistry;
import java.net.InetSocketAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/6 11:09
 */
@Component
public class ClientRegistry {

  @Autowired
  private ClientProperties clientProperties;
  @Autowired
  private ExecutorService executorService;
  @Autowired
  private JobService jobService;

  public void registerClient(){
    ExecutorRegisterReq executorRegisterReq = new ExecutorRegisterReq();
    Executor executor = new Executor();
    executor.setExecutorName(clientProperties.getName());
    ExecutorMachine executorMachine = new ExecutorMachine();
    InetSocketAddress socketAddress = (InetSocketAddress) GlobalChannel.getChannel().localAddress();
    executorMachine.setPort(socketAddress.getPort());
    executorMachine.setIp(socketAddress.getAddress().getHostAddress());
    executorRegisterReq.setExecutor(executor);
    executorRegisterReq.setExecutorMachine(executorMachine);
    executorService.registerExecutor(executorRegisterReq);
    jobService.registerJobs(clientProperties.getName(), JobRegistry.getAllJob());
  }
}
