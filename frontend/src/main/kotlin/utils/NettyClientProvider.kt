// File: kotlin/network/NettyClientProvider.kt
package utils

object NettyClientProvider {
    val nettyClient: NettyClient by lazy {
        NettyClient("10.208.90.113", 8066)
    }
}
//10.208.90.113：zjx 宿舍
//10.203.201.197 zjx 教室
//47.99.141.236