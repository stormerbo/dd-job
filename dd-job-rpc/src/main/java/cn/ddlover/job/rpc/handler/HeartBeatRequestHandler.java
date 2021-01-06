package cn.ddlover.job.rpc.handler;

import cn.ddlover.job.constant.RpcMessageType;
import cn.ddlover.job.entity.rpc.RpcHeader;
import cn.ddlover.job.entity.rpc.RpcMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/4/23 15:21
 */
@Slf4j
public class HeartBeatRequestHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    RpcMessage rpcMessage = (RpcMessage) msg;
    if (rpcMessage.getRpcHeader().getType().equals(RpcMessageType.HEART_BEAT_REQUEST.getType())) {
      log.debug("收到心跳");
      RpcMessage<Void> resp = buildResponse();
      ctx.writeAndFlush(resp);
    } else {
      ctx.fireChannelRead(msg);
    }
  }

  private RpcMessage<Void> buildResponse() {
    RpcMessage<Void> response = new RpcMessage<>();
    RpcHeader rpcHeader = new RpcHeader();
    rpcHeader.setType(RpcMessageType.HEART_BEAT_RESPONSE.getType());
    response.setRpcHeader(rpcHeader);
    return response;
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
