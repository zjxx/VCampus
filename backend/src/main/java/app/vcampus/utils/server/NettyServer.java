package app.vcampus.utils.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // 用于接收客户端连接
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // 用于处理 I/O 操作

        try {
            ServerBootstrap b = new ServerBootstrap(); // 创建 ServerBootstrap 实例
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class) // 指定使用 NIO 传输 Channel
             .childHandler(new ChannelInitializer<SocketChannel>() { // 配置新接入的 Channel
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(new NettyServerHandler()); // 添加自定义的处理器
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 128) // 设置 TCP 参数
             .childOption(ChannelOption.SO_KEEPALIVE, true); // 保持连接

            // 绑定端口并启动服务
            ChannelFuture f = b.bind(port).sync();
            // 等待服务器 socket 关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅地关闭线程池
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}