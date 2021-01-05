package cn.ddlover.job.rpc.handler;

import cn.ddlover.job.constant.RpcMessageType;
import cn.ddlover.job.entity.rpc.RpcHeader;
import cn.ddlover.job.entity.rpc.RpcMessage;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.lang.reflect.Method;
import java.net.SocketAddress;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/11 15:27
 */
@Slf4j
@Sharable
public class InvokeHandler extends ChannelInboundHandlerAdapter {

  private final ApplicationContext applicationContext;

  public InvokeHandler(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * 反射调用的对应的服务
   */
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    RpcMessage message = (RpcMessage) msg;
    RpcHeader rpcHeader = message.getRpcHeader();
    if (rpcHeader.getType().equals(RpcMessageType.REQUEST.getType())) {
      Channel channel = ctx.channel();
      RpcMessage<List<Object>> rpcMessage = (RpcMessage<List<Object>>) msg;
      Object result = doInvoke(rpcMessage);
      RpcMessage<Object> objectRpcMessage = buildResponse(rpcMessage, result);
      Gson gson = new Gson();
      log.info("server response {}", gson.toJson(objectRpcMessage));
      channel.writeAndFlush(objectRpcMessage);
    } else {
      ctx.fireChannelRead(msg);
    }

  }

  /**
   * 匹配method ，并完成调用
   */
  private Object doInvoke(RpcMessage<List<Object>> rpcMessage) throws Exception {
    RpcHeader rpcHeader = rpcMessage.getRpcHeader();
    String targetClass = rpcHeader.getTargetClass();
    Class<?> clazz = Class.forName(targetClass);
    Object bean = this.applicationContext.getBean(clazz);
    Class[] paramClazz = getParamClassArr(rpcHeader.getParamClazz());
    Method method = clazz.getMethod(rpcHeader.getTargetMethod(), paramClazz);
    return method.invoke(bean, rpcMessage.getData().toArray());
  }

  /**
   * 封装响应
   */
  private RpcMessage<Object> buildResponse(RpcMessage<List<Object>> rpcMessage, Object result) {
    RpcMessage<Object> response = new RpcMessage<>();
    RpcHeader rpcHeader = rpcMessage.getRpcHeader();
    rpcHeader.setType(RpcMessageType.RESPONSE.getType());
    response.setRpcHeader(rpcHeader);
    response.setData(result);
    return response;
  }


  /**
   * 获取对应方法的每个参数的参数类型
   * 按顺序返回
   */
  private Class[] getParamClassArr(List<String> paramClassNameList) throws ClassNotFoundException {
    if (CollectionUtils.isEmpty(paramClassNameList)) {
      return null;
    }
    Class[] clazzArr = new Class[paramClassNameList.size()];
    for (int i = 0; i < paramClassNameList.size(); i++) {
      Class clazz = Class.forName(paramClassNameList.get(i));
      clazzArr[i] = clazz;
    }
    return clazzArr;
  }
}
