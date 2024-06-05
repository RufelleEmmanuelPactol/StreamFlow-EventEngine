package com.streamflow.wire;

import com.streamflow.errors.ClientErrors;
import com.streamflow.errors.LogExceptEngine;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.util.AttributeKey;

public class SFlowChannel implements ChannelInboundHandler {
    private static AttributeKey<Boolean> channelAuthentication = AttributeKey.newInstance("channelAuthentication");
    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        ///this.channelAuthentication = channelAuthentication;
        channelHandlerContext.channel().attr(channelAuthentication).set(false);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if (!channelHandlerContext.channel().attr(channelAuthentication).get()) {
            LogExceptEngine.log(SFlowChannel.class, "Channel is not authenticated, rejecting request. This connection will now be closed.", LogExceptEngine.Level.WARN);
            channelHandlerContext.channel().close();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

    }
}
