package cn.ddlover.job.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/6 17:28
 */
@Getter
@Setter
public class InvokeJobReq {
  private Long jobId;

  private String param;
}
