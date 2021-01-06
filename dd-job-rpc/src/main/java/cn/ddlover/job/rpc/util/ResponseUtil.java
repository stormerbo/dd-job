package cn.ddlover.job.rpc.util;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/18 17:33
 */
public class ResponseUtil {

  private final static Map<String, BlockingDeque<Object>> RESULT_MAP = new ConcurrentHashMap<>();
  private final static ReentrantLock lock = new ReentrantLock();

  public static Object get(String key) throws InterruptedException {
    BlockingDeque<Object> queue = RESULT_MAP.get(key);
    if (Objects.isNull(queue)) {
      queue = new LinkedBlockingDeque<>();
      RESULT_MAP.put(key, queue);
    }
    return queue.take();
  }

  public static Object get(String key, Long timeout, TimeUnit timeUnit) throws InterruptedException {
    BlockingDeque<Object> queue = RESULT_MAP.get(key);
    if (Objects.isNull(queue)) {
      queue = new LinkedBlockingDeque<>();
      RESULT_MAP.put(key, queue);
    }
    return queue.poll(timeout, timeUnit);
  }

  public static void put(String key, Object result) {
    lock.lock();
    try {
      BlockingDeque<Object> queue = RESULT_MAP.get(key);
      if (Objects.isNull(queue)) {
        queue = new LinkedBlockingDeque<>();
      }
      queue.push(result);
      RESULT_MAP.put(key, queue);
    } finally {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
  }
}
