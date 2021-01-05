package cn.ddlover.job.rpc.handler;

import cn.ddlover.job.constant.RpcMessageType;
import cn.ddlover.job.entity.rpc.RpcHeader;
import cn.ddlover.job.entity.rpc.RpcMessage;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2021/1/5 14:52
 *
 * 客户端心跳实现类
 */
@Slf4j
@Sharable
public class HeartBeatTimerHandler extends ChannelInboundHandlerAdapter {
  private static final int HEARTBEAT_INTERVAL = 5;

  private int heartBeatInterval;


  public HeartBeatTimerHandler() {
    this.heartBeatInterval = HEARTBEAT_INTERVAL;
  }

  public HeartBeatTimerHandler(int heartBeatInterval) {
    this.heartBeatInterval = heartBeatInterval;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    scheduleSendHeartBeat(ctx);
    super.channelActive(ctx);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    RpcMessage rpcMessage = (RpcMessage) msg;
    if (rpcMessage.getRpcHeader().getType().equals(RpcMessageType.HEART_BEAT_RESPONSE.getType())) {
      log.debug("收到心跳");
    } else {
      ctx.fireChannelRead(msg);
    }
  }

  private void scheduleSendHeartBeat(ChannelHandlerContext ctx) {
    ctx.executor().schedule(() -> {
      if (ctx.channel().isActive()) {
        ctx.channel().writeAndFlush(buildHeartBeatRequest());
        scheduleSendHeartBeat(ctx);
      }
    }, this.heartBeatInterval, TimeUnit.SECONDS);
  }


  private RpcMessage<Void> buildHeartBeatRequest() {
    RpcMessage<Void> response = new RpcMessage<>();
    RpcHeader rpcHeader = new RpcHeader();
    rpcHeader.setType(RpcMessageType.HEART_BEAT_REQUEST.getType());
    response.setRpcHeader(rpcHeader);
    return response;
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
