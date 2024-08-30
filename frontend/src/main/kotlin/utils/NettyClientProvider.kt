// File: kotlin/network/NettyClientProvider.kt
package utils

import NettyClient

object NettyClientProvider {
    val nettyClient: NettyClient by lazy {
        NettyClient("localhost", 8066)
    }
}