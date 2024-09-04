// File: kotlin/network/NettyClientProvider.kt
package network

object NettyClientProvider {
    val nettyClient: NettyClient by lazy {
        NettyClient("localhost", 8066)
    }
}