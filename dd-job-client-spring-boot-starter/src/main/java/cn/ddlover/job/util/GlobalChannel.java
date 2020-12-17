package cn.ddlover.job.util;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/11 17:19
 */
public class GlobalChannel {

  private GlobalChannel() {
  }

  private static Channel CHANNEL_INSTANCE = null;

  public static synchronized void connect(Bootstrap bootstrap, String ip, Integer port) throws InterruptedException {
    Channel channel = bootstrap.connect(ip, port).await().channel();
    GlobalChannel.setChannel(channel);
  }

  public static synchronized void setChannel(Channel channel) {
    CHANNEL_INSTANCE = channel;
  }

  public static Channel getChannel() {
    return CHANNEL_INSTANCE;
  }
}
