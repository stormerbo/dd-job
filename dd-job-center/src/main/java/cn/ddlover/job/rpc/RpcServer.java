package cn.ddlover.job.rpc;

import cn.ddlover.job.rpc.encode.ProtostuffDecoder;
import cn.ddlover.job.rpc.encode.ProtostuffEncoder;
import cn.ddlover.job.rpc.handler.HeartBeatRequestHandler;
import cn.ddlover.job.rpc.handler.InvokeHandler;
import cn.ddlover.job.rpc.handler.JobIdleStateHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/11 15:08
 */
@Slf4j
@Component
public class RpcServer implements InitializingBean, ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Autowired
  private ServerProperties serverProperties;
  @Autowired
  Executor issueExecutor;

  @Override
  public void afterPropertiesSet() {
    issueExecutor.execute(() -> {
      try {
        doBind();
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    });
  }


  public void doBind() throws InterruptedException {
    NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    NioEventLoopGroup childGroup = new NioEventLoopGroup();
    ChannelFuture channelFuture = null;
    try {
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap.group(bossGroup, childGroup)
          .channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, 1024)
          .childOption(ChannelOption.SO_KEEPALIVE, true)
          //TCP协议中，TCP总是希望每次发送的数据足够大，避免网络中充满了小数据块。
          // Nagle算法就是为了尽可能的发送大数据快。
          // TCP_NODELAY就是控制是否启用Nagle算法。
          // 如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法；
          // 如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false。
          .childOption(ChannelOption.TCP_NODELAY, true)
          .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, serverProperties.getConnectTimeout() * 1000)
          .option(ChannelOption.SO_TIMEOUT, 5000)
          //缓冲池
          .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
          .handler(new LoggingHandler(LogLevel.INFO))
          .childHandler(new ChildChannelHandler(applicationContext, serverProperties.getHeartTimeout()));
      channelFuture = serverBootstrap.bind(serverProperties.getPort()).sync();
      log.info("dd-job rpc server start successfully.Bin on port:{}", serverProperties.getPort());
      channelFuture.channel().closeFuture().sync();
    } finally {
      bossGroup.shutdownGracefully();
      childGroup.shutdownGracefully();
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  private static class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

    private ApplicationContext applicationContext;
    private Integer idleTime;

    public ChildChannelHandler(ApplicationContext applicationContext, Integer idleTime) {
      this.applicationContext = applicationContext;
      this.idleTime = idleTime;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
      socketChannel.pipeline().addLast(new JobIdleStateHandler(this.idleTime));
      socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
      socketChannel.pipeline().addLast("protostuff decoder", new ProtostuffDecoder());
      socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
      socketChannel.pipeline().addLast("protostuff encoder", new ProtostuffEncoder());
      socketChannel.pipeline().addLast(new HeartBeatRequestHandler());
      socketChannel.pipeline().addLast(new InvokeHandler(applicationContext));
    }
  }
}
