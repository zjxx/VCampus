package app.vcampus.utils.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * NettyServer is responsible for setting up and starting a Netty server.
 * It configures the server bootstrap, initializes the channel pipeline,
 * and manages the event loop groups for handling client connections and I/O operations.
 */
public class NettyServer {

    private final int port;

    /**
     * Constructs a NettyServer with the specified port.
     *
     * @param port the port on which the server will listen for incoming connections
     */
    public NettyServer(int port) {
        this.port = port;
    }

    /**
     * Starts the Netty server.
     *
     * @throws Exception if an error occurs while starting the server
     */
    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // Used to accept client connections
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // Used to handle I/O operations

        try {
            ServerBootstrap b = new ServerBootstrap(); // Create ServerBootstrap instance
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class) // Specify using NIO transport Channel
             .childHandler(new ChannelInitializer<SocketChannel>() { // Configure new incoming Channel
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(new NettyServerHandler()); // Add custom handler
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 128) // Set TCP parameters
             .childOption(ChannelOption.SO_KEEPALIVE, true); // Keep connections alive

            // Bind to port and start the server
            ChannelFuture f = b.bind(port).sync();
            // Wait for the server socket to close
            f.channel().closeFuture().sync();
        } finally {
            // Gracefully shut down the event loop groups
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}