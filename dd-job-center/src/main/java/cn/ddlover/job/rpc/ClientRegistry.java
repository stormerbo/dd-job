package cn.ddlover.job.rpc;

import cn.ddlover.job.entity.Executor;
import io.netty.channel.Channel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/6 17:00
 */
public class ClientRegistry {

  private static Map<Executor, List<Channel>> EXECUTOR_CHANNEL_MAP = new ConcurrentHashMap<>();

  /**
   * 注册channel
   */
  public static synchronized void registerChannel(Channel channel, Executor executor) {
    List<Channel> channels = EXECUTOR_CHANNEL_MAP.getOrDefault(executor, new ArrayList<>());
    channels.add(channel);
    EXECUTOR_CHANNEL_MAP.put(executor, channels);
  }

  public static synchronized List<Channel> listChannel(Executor executor) {
    return EXECUTOR_CHANNEL_MAP.get(executor);
  }

  public static synchronized void removeChannel(Channel channel) {
    EXECUTOR_CHANNEL_MAP.values().forEach(channels -> channels.remove(channel));
  }

}
