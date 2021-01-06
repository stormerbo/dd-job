package cn.ddlover.job.rpc.handler;

import cn.ddlover.job.constant.RpcMessageType;
import cn.ddlover.job.entity.rpc.RpcHeader;
import cn.ddlover.job.entity.rpc.RpcMessage;
import cn.ddlover.job.rpc.util.ResponseUtil;
import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/12/18 16:58
 */
@Slf4j
public class ResponseHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    RpcMessage rpcMessage = (RpcMessage) msg;
    RpcHeader rpcHeader = rpcMessage.getRpcHeader();
    if (rpcHeader.getType().equals(RpcMessageType.RESPONSE.getType())) {
      String messageId = rpcHeader.getMessageId();
      ResponseUtil.put(messageId, rpcMessage.getData());
      Gson gson = new Gson();
      log.info("repsonse: {}", gson.toJson(rpcMessage));
    } else {
      ctx.fireChannelRead(msg);
    }
  }
}
