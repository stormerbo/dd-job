package cn.ddlover.job.entity.rpc;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/4/15 17:47
 */
@Getter
@Setter
public class RpcHeader {

  /**
   * 消息类型
   */
  private Integer type;

  /**
   * 唯一的消息编号
   */
  private String messageId;

  private String targetClass;

  private String targetMethod;

  private List<String> paramClazz;
}
