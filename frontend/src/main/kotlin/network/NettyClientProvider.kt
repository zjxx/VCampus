// File: kotlin/network/NettyClientProvider.kt
package network

object NettyClientProvider {
    val nettyClient: NettyClient by lazy {
        NettyClient("10.208.90.113", 8066)
    }
}