package cn.ddlover.job.constant;

import java.util.Arrays;
import lombok.Getter;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/1 16:56
 */
@Getter
public enum BlockTypeEnum {
  /**
   * 单机串行
   */
  SERIALIZE(0),
  /**
   * 丢弃后续调度
   */
  WAIT(1),
  /**
   * 覆盖之前调度
   */
  RECALL(2);

  private Integer type;

  BlockTypeEnum(Integer type) {
    this.type = type;
  }

  /**
   * 根据数据库存的type解析成枚举
   * 默认random
   */
  public static BlockTypeEnum reverse(Integer type) {
    return Arrays.stream(BlockTypeEnum.values()).filter(routeTypeEnum -> type.equals(routeTypeEnum.getType())).findFirst().orElse(SERIALIZE);
  }
}
