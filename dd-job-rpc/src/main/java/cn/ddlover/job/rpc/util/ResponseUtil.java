package cn.ddlover.job.rpc.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.sun.corba.se.impl.orbutil.graph.Graph;
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

  private final static Cache<String, BlockingDeque<Object>> RESULT_MAP = Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).build();
  private final static ReentrantLock LOCK = new ReentrantLock();


  public static Object get(String key) throws InterruptedException {
    LOCK.lock();
    try {
      BlockingDeque<Object> queue = RESULT_MAP.get(key, temp -> new LinkedBlockingDeque<>());
      if (Objects.isNull(queue)) {
        queue = new LinkedBlockingDeque<>();
        RESULT_MAP.put(key, queue);
      }
      LOCK.unlock();
      return queue.take();
    } finally {
      if (LOCK.isHeldByCurrentThread()) {
        LOCK.unlock();
      }
    }
  }

  public static Object get(String key, Long timeout, TimeUnit timeUnit) throws InterruptedException {
    BlockingDeque<Object> queue = RESULT_MAP.get(key, temp -> new LinkedBlockingDeque<>());
    if (Objects.isNull(queue)) {
      queue = new LinkedBlockingDeque<>();
      RESULT_MAP.put(key, queue);
    }
    return queue.poll(timeout, timeUnit);
  }

  public static void put(String key, Object result) {
    LOCK.lock();
    try {
      BlockingDeque<Object> queue = RESULT_MAP.get(key, temp -> new LinkedBlockingDeque<>());
      if (Objects.isNull(queue)) {
        queue = new LinkedBlockingDeque<>();
      }
      queue.push(result);
      RESULT_MAP.put(key, queue);
    } finally {
      if (LOCK.isHeldByCurrentThread()) {
        LOCK.unlock();
      }
    }
  }
}
