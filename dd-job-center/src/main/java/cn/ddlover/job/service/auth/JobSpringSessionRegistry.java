package cn.ddlover.job.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.stereotype.Service;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/10 15:36
 */
@Service
public class JobSpringSessionRegistry extends SpringSessionBackedSessionRegistry {
  @Autowired
  public JobSpringSessionRegistry(FindByIndexNameSessionRepository sessionRepository) {
    super(sessionRepository);
  }
}
