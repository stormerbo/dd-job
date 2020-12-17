package cn.ddlover.job.rpc.handler;

import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/4/23 22:25
 */
@Slf4j
@Component
public class ExponentialBackOffRetry implements ReconnectPolicy {

  private static final int MAX_RETRIES_LIMIT = 29;
  private static final int DEFAULT_MAX_SLEEP_MS = Integer.MAX_VALUE;

  private final Random random = new Random();
  private long baseSleepTimeMs = 5;
  private int maxRetries = 10;
  private int maxSleepMs = 30;

  public ExponentialBackOffRetry() {
    this(5, 10, 30);
  }

  public ExponentialBackOffRetry(int baseSleepTimeMs, int maxRetries, int maxSleepMs) {
    this.maxRetries = maxRetries;
    this.baseSleepTimeMs = baseSleepTimeMs;
    this.maxSleepMs = maxSleepMs;
  }

  @Override
  public boolean allowRetry(int retryCount) {
    if (retryCount < maxRetries) {
      return true;
    }
    return false;
  }

  @Override
  public long getSleepTimeMs(int retryCount) {
    if (retryCount < 0) {
      throw new IllegalArgumentException("retries count must greater than 0.");
    }
    if (retryCount > MAX_RETRIES_LIMIT) {
      log.info("maxRetries too large ({}). Pinning to {}", maxRetries, MAX_RETRIES_LIMIT);
      retryCount = MAX_RETRIES_LIMIT;
    }
    long sleepMs = baseSleepTimeMs * Math.max(1, random.nextInt(1 << retryCount));
    if (sleepMs > maxSleepMs) {
      log.info("Sleep extension too large ({}). Pinning to {}", sleepMs, maxSleepMs);
      sleepMs = maxSleepMs;
    }
    return sleepMs;
  }
}
