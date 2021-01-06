package cn.ddlover.job.rpc.encode;

import cn.ddlover.job.entity.rpc.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import java.io.Serializable;
import java.util.List;

/**
 * @author stormer.xia
 * @version 1.0
 * @date 2020/3/25 17:00
 */
public class ProtostuffDecoder extends MessageToMessageDecoder<ByteBuf> {

  @Override
  protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
    final byte[] array;
    final int length = byteBuf.readableBytes();
    array = new byte[length];
    byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);
    Schema<RpcMessage> schema = RuntimeSchema.getSchema(RpcMessage.class);
    RpcMessage<Serializable> wrapper = schema.newMessage();
    ProtobufIOUtil.mergeFrom(array, wrapper, schema);
    list.add(wrapper);
  }
}
