// File: kotlin/network/NettyClientProvider.kt
package utils

object NettyClientProvider {
    val nettyClient: NettyClient by lazy {
        NettyClient("47.99.141.236", 8066)
    }
}
//10.203.201.197: zjx教室
//10.208.90.113：zjx宿舍
//47.99.141.236