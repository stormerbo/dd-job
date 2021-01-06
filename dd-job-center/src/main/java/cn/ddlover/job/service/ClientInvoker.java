package cn.ddlover.job.service;

import cn.ddlover.job.constant.RpcMessageType;
import cn.ddlover.job.entity.Executor;
import cn.ddlover.job.entity.Job;
import cn.ddlover.job.entity.Response;
import cn.ddlover.job.entity.rpc.RpcHeader;
import cn.ddlover.job.entity.rpc.RpcMessage;
import cn.ddlover.job.mapper.ExecutorMapper;
import cn.ddlover.job.rpc.ClientRegistry;
import cn.ddlover.job.rpc.util.ResponseUtil;
import cn.ddlover.job.util.MessageUtil;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/6 17:32
 */
@Service
@Slf4j
public class ClientInvoker {

  @Autowired
  private ExecutorMapper executorMapper;

  public Response<Void> invoke(Job job) throws InterruptedException {
    Gson gson = new Gson();
    RpcMessage<List<Job>> rpcMessage = buildRequest(job);
    Executor executor = executorMapper.selectByExecutorId(job.getExecutorId());
    List<Channel> channels = ClientRegistry.listChannel(executor);
    if(CollectionUtils.isEmpty(channels)) {
      return Response.fail("没有可以使用的客户端");
    }
    Channel channel = channels.get(0);
    log.info("request: {}", gson.toJson(rpcMessage));
    // 发送消息
    channel.writeAndFlush(rpcMessage);
    String messageId = rpcMessage.getRpcHeader().getMessageId();
    return (Response<Void>)ResponseUtil.get(messageId);
  }


  private RpcMessage<List<Job>> buildRequest(Job job) {
    RpcMessage<List<Job>> rpcMessage = new RpcMessage<>();
    RpcHeader rpcHeader = new RpcHeader();
    rpcHeader.setMessageId(MessageUtil.generateMessageId());
    rpcHeader.setType(RpcMessageType.REQUEST.getType());
    List<String> paramList = new ArrayList<>();
    paramList.add(String.class.getName());
    rpcHeader.setParamClazz(paramList);
    rpcMessage.setRpcHeader(rpcHeader);
    List<Job> jobList = new ArrayList<>();
    jobList.add(job);
    rpcMessage.setData(jobList);
    return rpcMessage;
  }
}
