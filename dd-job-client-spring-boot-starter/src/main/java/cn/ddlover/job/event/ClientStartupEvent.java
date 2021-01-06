package cn.ddlover.job.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/6 14:16
 */
public class ClientStartupEvent extends ApplicationEvent {

  /**
   * Create a new {@code ApplicationEvent}.
   *
   * @param source the object on which the event initially occurred or with
   *               which the event is associated (never {@code null})
   */
  public ClientStartupEvent(Object source) {
    super(source);
  }
}
