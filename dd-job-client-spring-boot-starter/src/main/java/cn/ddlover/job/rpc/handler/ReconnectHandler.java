package cn.ddlover.job.rpc.handler;

import cn.ddlover.job.properties.ClientProperties;
import cn.ddlover.job.util.GlobalChannel;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/4/23 22:27
 */
@Sharable
@Slf4j
public class ReconnectHandler extends ChannelInboundHandlerAdapter {

  private int retries = 0;
  private final ReconnectPolicy reconnectPolicy;
  private final Bootstrap bootstrap;
  private final ClientProperties clientProperties;

  public ReconnectHandler(ReconnectPolicy reconnectPolicy, Bootstrap bootstrap, ClientProperties clientProperties) {
    this.reconnectPolicy = reconnectPolicy;
    this.bootstrap = bootstrap;
    this.clientProperties = clientProperties;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    retries = 0;
    log.info("Successfully connected to the server.");
    ctx.fireChannelActive();
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) {
    if (retries == 0) {
      log.error("Lost the TCP connection with the server.");
      ctx.close();
    }
    boolean allowRetry = reconnectPolicy.allowRetry(retries);
    if (allowRetry) {
      long sleepTimeMs = reconnectPolicy.getSleepTimeMs(retries);
      log.info("Try to reconnect to the server after {}ms. Retry count: {}.", sleepTimeMs, ++retries);
      final EventLoop eventLoop = ctx.channel().eventLoop();
      eventLoop.schedule(() -> {
        log.info("Reconnecting ...");
        try {
          GlobalChannel.connect(bootstrap, clientProperties.getServerIp(), clientProperties.getServerPort());
        } catch (Exception e) {
          channelInactive(ctx);
        }
      }, sleepTimeMs, TimeUnit.MILLISECONDS);
      ctx.fireChannelInactive();
    } else {
      log.error("reconnect failed after " + retries + "times");
      ctx.close();
    }
  }
}
