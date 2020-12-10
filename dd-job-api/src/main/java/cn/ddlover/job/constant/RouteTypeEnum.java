package cn.ddlover.job.constant;

import java.util.Arrays;
import lombok.Getter;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/1 16:44
 *
 * 路由策略
 */
@Getter
public enum RouteTypeEnum {
  /**
   * 第一个
   */
  FIRST(0),
  /**
   * 最后一个
   */
  LAST(1),
  /**
   * 轮询
   */
  ROUND(2),
  /**
   * 随机
   */
  RANDOM(3),
  /**
   * 一致性hash
   */
  UNIFORMITY_HASH(4),
  /**
   * 最不经常使用
   */
  USED_LOWEST(5),
  /**
   * 最近最久未使用
   */
  NOT_USED_MOST(6),
  /**
   * 故障转移
   */
  FAIL_OVER(7),
  /**
   * 忙碌转移
   */
  BUSY_OVER(8),
  /**
   * 分片广播
   */
  CLUSTER_BOARDING(9);

  private Integer type;

  RouteTypeEnum(Integer type) {
    this.type = type;
  }

  /**
   * 根据数据库存的type解析成枚举
   * 默认random
   */
  public static RouteTypeEnum reverse(Integer type) {
    return Arrays.stream(RouteTypeEnum.values()).filter(routeTypeEnum -> type.equals(routeTypeEnum.getType())).findFirst().orElse(RANDOM);
  }
}
