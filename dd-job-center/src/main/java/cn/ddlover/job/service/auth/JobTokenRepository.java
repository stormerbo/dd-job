package cn.ddlover.job.service.auth;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.stereotype.Component;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/8 19:32
 *
 * 实现token 入库，系统重启后token不会失效
 * 对应这个表 persistent_logins
 */
@Component
public class JobTokenRepository extends JdbcTokenRepositoryImpl {


  @Autowired
  public JobTokenRepository(DataSource dataSource) {
    super();
    super.setDataSource(dataSource);
  }
}
