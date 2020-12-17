package cn.ddlover.job.proxy;

import cn.ddlover.job.entity.rpc.RpcHeader;
import cn.ddlover.job.entity.rpc.RpcMessage;
import cn.ddlover.job.util.GlobalChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.aop.support.AopUtils;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/11 19:12
 */
@Component
public class RpcInvokeProxy implements MethodInterceptor {

  @Override
  public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
    if (shouldInvoke(method)) {
      RpcMessage<List<Object>> rpcMessage = buildRequest(proxy, method, args);
      Channel channel = GlobalChannel.getChannel();
      // 发送消息
      ChannelFuture channelFuture = channel.writeAndFlush(rpcMessage);
      return channelFuture.await().get();
    } else {
      return method.invoke(proxy, args);
    }
  }

  private RpcMessage<List<Object>> buildRequest(Object proxy, Method method, Object[] objects) {
    RpcMessage<List<Object>> rpcMessage = new RpcMessage<>();
    RpcHeader rpcHeader = new RpcHeader();
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
