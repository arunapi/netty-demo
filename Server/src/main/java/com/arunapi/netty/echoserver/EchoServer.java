package com.arunapi.netty.echoserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
    private int port;
    public EchoServer(int port){
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if(args.length !=1){
            System.err.println("enter port");
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup(); //Nio Transport => NioEventLoopGroup
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class) //Nio Transport channel
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() { //add the handler to channel's pipeline
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(serverHandler); //EchoServerHandler is @Sharable so we can always use the same one.
                        }
                    });
            ChannelFuture f = b.bind().sync(); //binds the server async; sync() waits for bind to complete.
            f.channel().closeFuture().sync(); //Get the CloseFuture of the channel and blocks the current thread until its complete
        }
        finally {
            group.shutdownGracefully().sync();
        }
    }
}
