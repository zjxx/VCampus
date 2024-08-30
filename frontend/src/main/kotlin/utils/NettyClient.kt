import com.google.gson.Gson
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import java.util.Base64

class NettyClient(private val host: String, private val port: Int) {
    private var role: String = "null"
    private val gson = Gson()

    fun setRole(role: String) {
        this.role = role
    }

    fun sendRequest(request: Any, type: String, responseHandler: (String) -> Unit) {
        Thread {
            val group = NioEventLoopGroup()
            try {
                val bootstrap = Bootstrap()
                bootstrap.group(group)
                    .channel(NioSocketChannel::class.java)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(object : ChannelInitializer<SocketChannel>() {
                        override fun initChannel(ch: SocketChannel) {
                            ch.pipeline().addLast(
                                LoggingHandler(LogLevel.INFO),
                                StringDecoder(),
                                StringEncoder(),
                                NettyClientHandler(responseHandler)
                            )
                        }
                    })

                // 启动客户端
                val future: ChannelFuture = bootstrap.connect(host, port).sync()

                // 将请求转换为 JSON 并发送
                val req = gson.fromJson(gson.toJson(request), MutableMap::class.java) as MutableMap<String, Any>
                req["role"] = role // 添加新的键值对
                req["type"] = type // 添加新的键值对
                val jsonRequest = gson.toJson(req)

                future.channel().writeAndFlush(jsonRequest)

                // 等待连接关闭
                future.channel().closeFuture().sync()
            } finally {
                group.shutdownGracefully()
            }
        }.start()
    }
}