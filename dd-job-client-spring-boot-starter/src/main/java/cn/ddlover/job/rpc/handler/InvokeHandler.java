package cn.ddlover.job.rpc.handler;

import cn.ddlover.job.constant.RpcMessageType;
import cn.ddlover.job.domain.JobHolder;
import cn.ddlover.job.entity.Job;
import cn.ddlover.job.entity.Response;
import cn.ddlover.job.entity.rpc.RpcHeader;
import cn.ddlover.job.entity.rpc.RpcMessage;
import cn.ddlover.job.util.JobRegistry;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.lang.reflect.Method;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/11 15:27
 */
@Slf4j
@Component
@Sharable
public class InvokeHandler extends ChannelInboundHandlerAdapter implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  /**
   * 反射调用的对应的服务
   */
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    RpcMessage message = (RpcMessage) msg;
    RpcHeader rpcHeader = message.getRpcHeader();
    if (rpcHeader.getType().equals(RpcMessageType.REQUEST.getType())) {
      RpcMessage<List<Job>> rpcMessage = (RpcMessage<List<Job>>) msg;
      Job job = rpcMessage.getData().get(0);
      JobHolder jobHolder = JobRegistry.getJobHolder(job.getJobName());
      Response<Void> result = (Response<Void>) doInvoke(jobHolder, job);
      RpcMessage<Response<Void>> objectRpcMessage = buildResponse(rpcMessage, result);
      Channel channel = ctx.channel();
      channel.writeAndFlush(objectRpcMessage);
    } else {
      ctx.fireChannelRead(msg);
    }
  }

  /**
   * 匹配method ，并完成调用
   */
  private Object doInvoke(JobHolder jobHolder, Job job) throws Exception {
    Class<?> clazz = jobHolder.getJobClass();
    Object bean = this.applicationContext.getBean(clazz);
    Class[] paramClazz = new Class[]{String.class};
    Method method = clazz.getMethod(jobHolder.getMethodName(), paramClazz);
    return method.invoke(bean, job.getJobParam());
  }

  /**
   * 封装响应
   */
  private RpcMessage<Response<Void>> buildResponse(RpcMessage<List<Job>> rpcMessage, Response<Void> result) {
    RpcMessage<Response<Void>> response = new RpcMessage<>();
    RpcHeader rpcHeader = rpcMessage.getRpcHeader();
    rpcHeader.setType(RpcMessageType.RESPONSE.getType());
    response.setRpcHeader(rpcHeader);
    response.setData(result);
    return response;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
