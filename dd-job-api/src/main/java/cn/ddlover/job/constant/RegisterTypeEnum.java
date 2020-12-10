package cn.ddlover.job.constant;

import java.util.Arrays;
import lombok.Getter;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/1 17:26
 * 注册方式
 */
@Getter
public enum RegisterTypeEnum {
  /**
   * 自动注册
   */
  AUTO(0),
  /**
   * 手动注册
   */
  MANUAL(1);

  private Integer type;


  RegisterTypeEnum(Integer type) {
    this.type = type;
  }

  /**
   * 默认是手动注册
   */
  public static RegisterTypeEnum reverse(Integer type) {
    return Arrays.stream(RegisterTypeEnum.values()).filter(routeTypeEnum -> type.equals(routeTypeEnum.getType())).findFirst().orElse(AUTO);
  }
}
