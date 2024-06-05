package com.streamflow.orchestrator;

import com.streamflow.errors.LogExceptEngine;
import com.streamflow.state.GlobalMetaVariables;
import com.streamflow.wire.SFlowChannel;
import com.streamflow.wire.SFlowDecoder;
import com.streamflow.wire.SFlowEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;



public class ServerEngineUnit {
    private final EventLoopGroup platform;
    private final EventLoopGroup worker;
    private final ServerBootstrap bootstrap;
    private Channel channel;


    public ServerEngineUnit() {
        bootstrap = new ServerBootstrap();
        platform = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        worker = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
    }


    /**
     * Start the server engine, starts listening to calls upon occurrence.
     */
    public void startup() {
        LogExceptEngine.log(ServerEngineUnit.class, "Starting server engine, initializing sockets and threads.", LogExceptEngine.Level.INFO);
        try {
            bootstrap.group(platform, worker)
                    .localAddress(GlobalMetaVariables.PORT())
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                              new SFlowEncoder(),
                                new SFlowDecoder(),
                                    new SFlowChannel()
                            );
                        }
                    }).option(ChannelOption.SO_BACKLOG, 10000);
            channel = bootstrap.bind().sync().channel();
            LogExceptEngine.log(ServerEngineUnit.class, "Server engine started successfully, now accepting connections in port: "+ GlobalMetaVariables.PORT() +".", LogExceptEngine.Level.INFO);
        } catch (Exception e) {
            LogExceptEngine.logWithNative(e, ServerEngineUnit.class, "Error starting server engine. Please check if the port is not already in use.", LogExceptEngine.Level.ERROR);
        }
    }


    public void terminate() {
        LogExceptEngine.log(ServerEngineUnit.class, "Terminating server engine. Closing all connections.", LogExceptEngine.Level.INFO);
        try {
            channel.close().sync();
            platform.shutdownGracefully().sync();
            worker.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            LogExceptEngine.logWithNative(e, ServerEngineUnit.class, "Error terminating server engine. Please check if the port is not already in use.", LogExceptEngine.Level.ERROR);
        }
        LogExceptEngine.log(ServerEngineUnit.class, "Server engine terminated successfully.", LogExceptEngine.Level.INFO);
    }


}
