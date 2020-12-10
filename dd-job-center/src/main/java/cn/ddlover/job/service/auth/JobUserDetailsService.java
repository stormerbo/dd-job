package cn.ddlover.job.service.auth;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/8 18:10
 */
@Service
public class JobUserDetailsService extends JdbcUserDetailsManager {

  @Autowired
  public JobUserDetailsService(DataSource dataSource) {
    super(dataSource);
  }

}
