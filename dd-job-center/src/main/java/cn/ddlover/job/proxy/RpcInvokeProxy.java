package cn.ddlover.job.proxy;

import cn.ddlover.job.constant.RpcMessageType;
import cn.ddlover.job.entity.Executor;
import cn.ddlover.job.entity.Job;
import cn.ddlover.job.entity.rpc.RpcHeader;
import cn.ddlover.job.entity.rpc.RpcMessage;
import cn.ddlover.job.mapper.ExecutorMapper;
import cn.ddlover.job.rpc.ClientRegistry;
import cn.ddlover.job.rpc.util.ResponseUtil;
import cn.ddlover.job.util.MessageUtil;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/11 19:12
 */
@Component
@Slf4j
public class RpcInvokeProxy implements MethodInterceptor {

  @Autowired
  private ExecutorMapper executorMapper;

  @Override
  public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
    Gson gson = new Gson();
    if (shouldInvoke(method)) {
      RpcMessage<List<Object>> rpcMessage = buildRequest(proxy, method, args);

      // 参数只有一个，且必为job
      Job job = (Job) args[0];
      Executor executor = executorMapper.selectByExecutorId(job.getExecutorId());
      List<Channel> channels = ClientRegistry.listChannel(executor);
      Channel channel = channels.get(0);
      log.info("request: {}", gson.toJson(rpcMessage));
      // 发送消息
      channel.writeAndFlush(rpcMessage);
      String messageId = rpcMessage.getRpcHeader().getMessageId();
      return ResponseUtil.get(messageId);
    } else {
      return method.invoke(proxy, args);
    }
  }

  private RpcMessage<List<Object>> buildRequest(Object proxy, Method method, Object[] objects) {
    RpcMessage<List<Object>> rpcMessage = new RpcMessage<>();
    RpcHeader rpcHeader = new RpcHeader();
    rpcHeader.setMessageId(MessageUtil.generateMessageId());
    rpcHeader.setType(RpcMessageType.REQUEST.getType());
    rpcHeader.setTargetClass(AopUtils.getTargetClass(proxy).getInterfaces()[0].getName());
    rpcHeader.setTargetMethod(method.getName());
    List<String> parameterList = Arrays.stream(method.getParameters()).map(parameter -> parameter.getType().getName()).collect(Collectors.toList());
    rpcHeader.setParamClazz(parameterList);
    rpcMessage.setRpcHeader(rpcHeader);
    rpcMessage.setData(Arrays.asList(objects));
    return rpcMessage;
  }


  public boolean shouldInvoke(Method method) {
    return Arrays.stream(Object.class.getDeclaredMethods()).noneMatch(objMethod -> objMethod.equals(method));
  }
}
