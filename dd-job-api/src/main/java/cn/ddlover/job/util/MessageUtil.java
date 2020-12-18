package cn.ddlover.job.util;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/18 17:54
 */
public class MessageUtil {

  private final static SnowFlakeUtil SNOW_FLAKE_UTIL = new SnowFlakeUtil(1, 1, 1);

  public static String generateMessageId() {
    return String.valueOf(SNOW_FLAKE_UTIL.nextId());
  }
}
