package cn.ddlover.job.entity.rpc;

import lombok.Getter;
import lombok.Setter;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/4/15 17:46
 */
@Getter
@Setter
public class RpcMessage<T> {

  private RpcHeader rpcHeader;

  private T data;
}
