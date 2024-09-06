package utils

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
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Base64

class NettyClient(private val host: String, private val port: Int) {
    private var role: String = "null"
    private val gson = Gson()

    fun setRole(role: String) {
        this.role = role
    }

    private fun splitData(data: String, chunkSize: Int): List<String> {
        val chunks = mutableListOf<String>()
        var start = 0
        while (start < data.length) {
            val end = Math.min(data.length, start + chunkSize)
            chunks.add(data.substring(start, end))
            start = end
        }
        return chunks
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

    fun sendFile(request: Any,type: String,filePath: String, responseHandler: (String) -> Unit) {
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

                // 读取文件并编码为 Base64
                val fileBytes = Files.readAllBytes(Paths.get(filePath))
                val encodedFile = Base64.getEncoder().encodeToString(fileBytes)

                val req = gson.fromJson(gson.toJson(request), MutableMap::class.java) as MutableMap<String, Any>
                req["role"] = role // 添加新的键值对
                req["type"] = type // 添加新的键值对
                val jsonRequest = gson.toJson(req)
                val chunks = splitData(encodedFile, 512) // Split data into 512B chunks

                // 发送请求
                future.channel().writeAndFlush(jsonRequest)
                for (chunk in chunks) {
                    future.channel().writeAndFlush(chunk)
                }
                future.channel().writeAndFlush("END_OF_MESSAGE")
                // 等待连接关闭
                future.channel().closeFuture().sync()
            } finally {
                group.shutdownGracefully()
            }
        }.start()
    }
}