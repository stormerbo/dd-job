package cn.ddlover.job.rpc.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/4/23 15:01
 */
@Slf4j
public class JobIdleStateHandler extends IdleStateHandler {


  /**
   * 15s 未收到数据
   */
  public JobIdleStateHandler(Integer idleTime) {
    super(idleTime, 0, 0, TimeUnit.SECONDS);
  }

  @Override
  protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
    log.error(TimeUnit.MILLISECONDS.toSeconds(super.getReaderIdleTimeInMillis()) + "秒未读到数据，关闭连接");
    ctx.channel().disconnect();
  }
}
