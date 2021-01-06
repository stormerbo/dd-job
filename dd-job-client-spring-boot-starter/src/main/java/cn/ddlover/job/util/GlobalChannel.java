package cn.ddlover.job.util;

import cn.ddlover.job.exception.ConnectFailException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import java.util.Objects;

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
    if(Objects.isNull(channel.remoteAddress())) {
      throw new ConnectFailException("链接服务器失败");
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
