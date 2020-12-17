package cn.ddlover.job.rpc.handler;

import cn.ddlover.job.entity.rpc.RpcHeader;
import cn.ddlover.job.entity.rpc.RpcMessage;
import io.netty.channel.Channel;
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
public class InvokeHandler extends ChannelInboundHandlerAdapter implements ApplicationContextAware {

  private ApplicationContext applicationContext;


  /**
   * 反射调用的对应的服务
   */
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    RpcMessage<List<Object>> rpcMessage = (RpcMessage<List<Object>>) msg;
    Object result = doInvoke(rpcMessage);
    RpcMessage<Object> objectRpcMessage = buildResponse(rpcMessage, result);
    Channel channel = ctx.channel();
    channel.writeAndFlush(objectRpcMessage);
  }

  /**
   * 匹配method ，并完成调用
   */
  private Object doInvoke(RpcMessage<List<Object>> rpcMessage) throws Exception {
    RpcHeader rpcHeader = rpcMessage.getRpcHeader();
    String targetClass = rpcHeader.getTargetClass();
    Object bean = this.applicationContext.getBean(Class.forName(targetClass));
    Class[] paramClazz = getParamClassArr(rpcHeader.getParamClazz());
    Class clazz = bean.getClass();
    Method method = clazz.getMethod(rpcHeader.getTargetMethod(), paramClazz);
    return method.invoke(bean, rpcMessage.getData().toArray());
  }

  /**
   * 封装响应
   */
  private RpcMessage<Object> buildResponse(RpcMessage<List<Object>> rpcMessage, Object result) {
    RpcMessage<Object> response = new RpcMessage<>();
    response.setRpcHeader(rpcMessage.getRpcHeader());
    response.setData(result);
    return response;
  }


  /**
   * 获取对应方法的每个参数的参数类型
   * 按顺序返回
   */
  private Class[] getParamClassArr(List<String> paramClassNameList) throws ClassNotFoundException {
    Class[] clazzArr = new Class[paramClassNameList.size()];
    for (int i = 0; i < paramClassNameList.size(); i++) {
      Class clazz = Class.forName(paramClassNameList.get(i));
      clazzArr[i] = clazz;
    }
    return clazzArr;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
