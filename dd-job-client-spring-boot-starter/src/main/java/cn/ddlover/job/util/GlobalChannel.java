package cn.ddlover.job.util;

import cn.ddlover.job.exception.ConnectFailException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/11 17:19
 */
@Slf4j
public class GlobalChannel {

  private GlobalChannel() {
  }

  private static Channel CHANNEL_INSTANCE = null;

  public static synchronized void connect(Bootstrap bootstrap, String ip, Integer port) throws InterruptedException {
    ChannelFuture channelFuture = bootstrap.connect(ip, port);
    Channel channel = channelFuture.await().channel();
    if(Objects.isNull(channel.remoteAddress())) {
      log.error("连接服务器失败, 3s后重新连接。。。");
      channelFuture.channel().eventLoop().schedule(()-> {
        try {
          connect(bootstrap, ip, port);
        } catch (InterruptedException e) {
          log.error("连接服务器异常:{}", ExceptionUtils.getStackTrace(e));
        }
      },5, TimeUnit.SECONDS);
      throw new ConnectFailException("连接服务器失败");

    }
    GlobalChannel.setChannel(channel);
  }

  public static synchronized void setChannel(Channel channel) {
    CHANNEL_INSTANCE = channel;
  }

  public static synchronized Channel getChannel() {
    return CHANNEL_INSTANCE;
  }
}
