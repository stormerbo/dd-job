package cn.ddlover.job;

import cn.ddlover.job.entity.Executor;
import cn.ddlover.job.entity.ExecutorMachine;
import cn.ddlover.job.entity.Response;
import cn.ddlover.job.entity.requst.ExecutorRegisterReq;
import cn.ddlover.job.properties.ClientProperties;
import cn.ddlover.job.rpc.encode.ProtostuffDecoder;
import cn.ddlover.job.rpc.encode.ProtostuffEncoder;
import cn.ddlover.job.rpc.handler.HeartBeatTimerHandler;
import cn.ddlover.job.rpc.handler.ReconnectHandler;
import cn.ddlover.job.rpc.handler.ResponseHandler;
import cn.ddlover.job.service.ExecutorService;
import cn.ddlover.job.service.JobService;
import cn.ddlover.job.util.GlobalChannel;
import cn.ddlover.job.util.JobRegistry;
import com.google.gson.Gson;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
public class RpcClientAutoConfiguration {

  @Autowired
  private ClientProperties clientProperties;
  @Autowired
  private ExecutorService executorService;
  @Autowired
  private JobService jobService;

  @Bean
  public Bootstrap doBind(ReconnectHandler reconnectHandler) throws InterruptedException {
    EventLoopGroup group = new NioEventLoopGroup();
    // bootstrap 可重用, 只需在TcpClient实例化的时候初始化即可.
    Bootstrap bootstrap = new Bootstrap();
    try {
      bootstrap.group(group)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, clientProperties.getConnectTimeout() * 1000)
          .handler(new ChildChannelHandler(reconnectHandler, clientProperties.getHeartBeatInterval()));
      GlobalChannel.connect(bootstrap, clientProperties.getServerIp(), clientProperties.getServerPort());
      doRegister();
      return bootstrap;
    } catch (Exception e) {
      return bootstrap;
    }

  }

  private void doRegister() {
    ExecutorRegisterReq executorRegisterReq = new ExecutorRegisterReq();
    Executor executor = new Executor();
    executor.setExecutorName(clientProperties.getName());
    ExecutorMachine executorMachine = new ExecutorMachine();
    InetSocketAddress socketAddress = (InetSocketAddress) GlobalChannel.getChannel().localAddress();
    executorMachine.setPort(clientProperties.getPort());
    executorMachine.setIp(socketAddress.getHostName());
    executorRegisterReq.setExecutor(executor);
    executorRegisterReq.setExecutorMachine(executorMachine);
    executorService.registerExecutor(executorRegisterReq);
    jobService.registerJobs(clientProperties.getName(), JobRegistry.getAllJob());
  }


  private static class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

    private final ReconnectHandler reconnectHandler;
    private HeartBeatTimerHandler heartBeatInterval;

    public ChildChannelHandler(ReconnectHandler reconnectHandler) {
      this.reconnectHandler = reconnectHandler;
    }

    public ChildChannelHandler(ReconnectHandler reconnectHandler, Integer heartBeatInterval) {
      this.reconnectHandler = reconnectHandler;
      if (Objects.isNull(heartBeatInterval) || heartBeatInterval <= 0) {
        this.heartBeatInterval = new HeartBeatTimerHandler();
      } else {
        this.heartBeatInterval = new HeartBeatTimerHandler(heartBeatInterval);
      }
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
      socketChannel.pipeline().addLast(this.reconnectHandler);
      socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
      socketChannel.pipeline().addLast("protostuff decoder", new ProtostuffDecoder());
      socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
      socketChannel.pipeline().addLast(heartBeatInterval);
      socketChannel.pipeline().addLast("protostuff encoder", new ProtostuffEncoder());
      socketChannel.pipeline().addLast("response handler", new ResponseHandler());
    }
  }
}
