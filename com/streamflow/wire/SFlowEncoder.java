package com.streamflow.wire;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class SFlowEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        var bytes = message.encode().getBytes(StandardCharsets.UTF_8);
        int length = bytes.length;
        byteBuf.writeBytes(intToByteArrayBitwise(length));
        byteBuf.writeBytes(bytes);
    }

    public static byte[] intToByteArrayBitwise(int value) {
        byte[] byteArray = new byte[4];
        byteArray[0] = (byte) (value >> 24);
        byteArray[1] = (byte) (value >> 16);
        byteArray[2] = (byte) (value >> 8);
        byteArray[3] = (byte) value;
        return byteArray;
    }
}
