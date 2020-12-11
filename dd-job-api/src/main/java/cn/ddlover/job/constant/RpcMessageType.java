package cn.ddlover.job.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/4/15 18:11
 */
@Getter
@AllArgsConstructor
public enum RpcMessageType {
  HEART_BEAT_REQUEST(0),
  HEART_BEAT_RESPONSE(1),
  EXECUTOR_REGISTER(2),
  EXECUTOR_REGISTER_RESPONSE(3);

  private Integer type;
}
