package cn.ddlover.job.listener;

import cn.ddlover.job.event.ClientStartupEvent;
import cn.ddlover.job.registrar.ClientRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/6 16:13
 */
@Component
public class ClientStartupListener implements ApplicationListener<ClientStartupEvent> {

  @Autowired
  private ClientRegistry clientRegistry;

  @Override
  public void onApplicationEvent(ClientStartupEvent event) {
    clientRegistry.registerClient();
  }
}
