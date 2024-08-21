package app.vcampus.utils.server;

import app.vcampus.utils.ControllerManager;
import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private final Gson gson = new Gson();
    private final ControllerManager controllerManager = new ControllerManager();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        String jsonRequest = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println("Server received: " + jsonRequest);

        // 使用 ControllerManager 处理请求
        String jsonResponse = controllerManager.handleRequest(jsonRequest);

        // 写入 JSON 响应
        ByteBuf responseBuf = Unpooled.copiedBuffer(jsonResponse, CharsetUtil.UTF_8);
        ctx.write(responseBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush(); // 刷新消息
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close(); // 发生异常时关闭连接
    }
}