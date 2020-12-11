package cn.ddlover.job.rpc;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/11 15:01
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "job.server")
public class ServerProperties {

  private Integer port;

  private Integer connectTimeout = 30;

  private Integer readTimeout = 30;

  private Integer heartTimeout = 30;
}
