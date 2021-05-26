package cn.ddlover.job;

import cn.ddlover.job.event.ClientStartupEvent;
import cn.ddlover.job.properties.ClientProperties;
import cn.ddlover.job.rpc.encode.ProtostuffDecoder;
import cn.ddlover.job.rpc.encode.ProtostuffEncoder;
import cn.ddlover.job.rpc.handler.HeartBeatTimerHandler;
import cn.ddlover.job.rpc.handler.InvokeHandler;
import cn.ddlover.job.rpc.handler.ReconnectHandler;
import cn.ddlover.job.rpc.handler.ReconnectPolicy;
import cn.ddlover.job.rpc.handler.ResponseHandler;
import cn.ddlover.job.util.GlobalChannel;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/11 11:21
 */
@Slf4j
@EnableConfigurationProperties(ClientProperties.class)
@ComponentScan
@Configuration
public class RpcClientAutoConfiguration implements ApplicationContextAware {

  private ApplicationContext applicationContext;


  @Bean
  @Autowired
  public Bootstrap doConnect(ClientProperties clientProperties, ReconnectPolicy reconnectPolicy, InvokeHandler invokeHandler) {
    EventLoopGroup group = new NioEventLoopGroup();
    // bootstrap 可重用, 只需在TcpClient实例化的时候初始化即可.
    Bootstrap bootstrap = new Bootstrap();
    ReconnectHandler reconnectHandler = new ReconnectHandler(applicationContext,reconnectPolicy, bootstrap, clientProperties);
    bootstrap.group(group)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, clientProperties.getConnectTimeout() * 1000)
        .handler(new ChildChannelHandler(reconnectHandler, clientProperties.getHeartBeatInterval(),invokeHandler));
    try {
      GlobalChannel.connect(bootstrap, clientProperties.getServerIp(), clientProperties.getServerPort());
      applicationContext.publishEvent(new ClientStartupEvent("客户端启动完成"));
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
    }
    return bootstrap;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }


  private static class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
    private final ReconnectHandler reconnectHandler;
    private final HeartBeatTimerHandler heartBeatInterval;
    private final InvokeHandler invokeHandler;

    public ChildChannelHandler(ReconnectHandler reconnectHandler, Integer heartBeatInterval, InvokeHandler invokeHandler) {
      this.reconnectHandler = reconnectHandler;
      if (Objects.isNull(heartBeatInterval) || heartBeatInterval <= 0) {
        this.heartBeatInterval = new HeartBeatTimerHandler();
      } else {
        this.heartBeatInterval = new HeartBeatTimerHandler(heartBeatInterval);
      }
      this.invokeHandler= invokeHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
      socketChannel.pipeline().addLast(this.reconnectHandler);
      socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
      socketChannel.pipeline().addLast("protostuff decoder", new ProtostuffDecoder());
      socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
      socketChannel.pipeline().addLast(this.heartBeatInterval);
      socketChannel.pipeline().addLast("protostuff encoder", new ProtostuffEncoder());
      socketChannel.pipeline().addLast("request handler", this.invokeHandler);
      socketChannel.pipeline().addLast("response handler", new ResponseHandler());
    }
  }
}
