package cn.ddlover.job.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/11 10:32
 */

@Getter
@Setter
@ConfigurationProperties(prefix = "job.admin")
public class AdminProperties {

  private String username;

  private String password;
}
