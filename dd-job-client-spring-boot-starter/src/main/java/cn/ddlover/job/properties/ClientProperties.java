package cn.ddlover.job.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/11 11:16
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "job.client")
public class ClientProperties {

  private final static String HTTP_SCHEMA = "http://";
  private final static String HTTPS_SCHEMA = "https://";

  private String name;

  private Integer port;

  private Integer connectTimeout = 30;

  private Integer readTimeout = 30;

  private String serverUrl;

  private Integer heartBeatInterval = 5;

  public String getServerIp() {
    String ip = serverUrl.replace(HTTP_SCHEMA, "").replace(HTTPS_SCHEMA, "");
    ip = ip.substring(0, ip.lastIndexOf(":"));
    return ip;
  }

  public Integer getServerPort() {
    // 带协议
    if (serverUrl.startsWith(HTTP_SCHEMA)) {
      int i = serverUrl.replace(HTTP_SCHEMA, "").lastIndexOf(":");
      if (i < 0) {
        return 80;
      } else {
        return Integer.parseInt(serverUrl.substring(i + 1));
      }
    } else if (serverUrl.startsWith(HTTPS_SCHEMA)) {
      int i = serverUrl.replace(HTTPS_SCHEMA, "").lastIndexOf(":");
      if (i < 0) {
        return 443;
      } else {
        return Integer.parseInt(serverUrl.substring(i + 1));
      }
    }
    return Integer.parseInt(serverUrl.substring(serverUrl.lastIndexOf(":") + 1));
  }
}
