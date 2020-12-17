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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/4/23 22:27
 */
@Sharable
@Component
@Slf4j
public class ReconnectHandler extends ChannelInboundHandlerAdapter {

  private int retries = 0;
  @Autowired
  private ReconnectPolicy reconnectPolicy;
  @Autowired
  private Bootstrap bootstrap;
  @Autowired
  private ClientProperties clientProperties;


  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    log.info("Successfully established a connection to the server.");
    retries = 0;
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
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }, sleepTimeMs, TimeUnit.MILLISECONDS);
      ctx.fireChannelInactive();
    } else {
      log.error("reconnect failed after " + retries + "times");
      ctx.close();
    }
  }
}
