package cn.ddlover.job.rpc.encode;

import cn.ddlover.job.entity.rpc.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/3/25 16:51
 */
public class ProtostuffEncoder extends MessageToByteEncoder<RpcMessage> {

  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage wrapper, ByteBuf byteBuf) throws Exception {
    Schema<RpcMessage> schema = RuntimeSchema.getSchema(RpcMessage.class);

    LinkedBuffer linkedBuffer = LinkedBuffer.allocate(512);
    final byte[] protostuff;
    try {
      protostuff = ProtobufIOUtil.toByteArray(wrapper, schema, linkedBuffer);
    } finally {
      linkedBuffer.clear();
    }
    byteBuf.writeBytes(protostuff);
  }


}
