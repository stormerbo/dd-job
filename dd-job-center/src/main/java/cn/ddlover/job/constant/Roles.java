package cn.ddlover.job.constant;

import lombok.Getter;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/8 19:26
 */
@Getter
public enum Roles {

  ADMIN("admin"),
  USER("user");

  public String role;

  Roles(String role) {
    this.role = role;
  }
}
