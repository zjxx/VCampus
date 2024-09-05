// File: kotlin/network/NettyClientProvider.kt
package network

object NettyClientProvider {
    val nettyClient: NettyClient by lazy {
        NettyClient("47.99.141.236", 8066)
    }
}