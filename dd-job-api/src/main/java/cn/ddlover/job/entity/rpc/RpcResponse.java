package cn.ddlover.job.entity.rpc;

import lombok.Getter;
import lombok.Setter;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/4/16 11:08
 */
@Getter
@Setter
public class RpcResponse<T> {
  private int code;

  private String message;

  private T data;
}
