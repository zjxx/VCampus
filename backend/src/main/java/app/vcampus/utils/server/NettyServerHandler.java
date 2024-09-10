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
    private final StringBuilder stringBuilder = new StringBuilder();
    private boolean isFileUpload = false;
    private String initJson;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        String chunk = byteBuf.toString(CharsetUtil.UTF_8);
        String clientIp = ctx.channel().remoteAddress().toString();

        if (chunk.contains("file_upload")) {
            isFileUpload = true;
            initJson = chunk.substring(0, chunk.indexOf("}") + 1);
            if (chunk.contains("}")) {
                chunk = chunk.substring(chunk.indexOf("}") + 1);
                byteBuf = Unpooled.copiedBuffer(chunk, CharsetUtil.UTF_8);
            }
            stringBuilder.setLength(0);
        }

        if (isFileUpload) {
            if (chunk.endsWith("END_OF_MESSAGE")) {
                byte[] bytes = java.util.Base64.getDecoder().decode(stringBuilder.toString());
                String jsonResponse = controllerManager.handleRequestWithParams(initJson, stringBuilder.toString(), clientIp);
                ByteBuf responseBuf = Unpooled.copiedBuffer(jsonResponse, CharsetUtil.UTF_8);
                ctx.write(responseBuf);
                isFileUpload = false;
                stringBuilder.setLength(0);
            }
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            String str = new String(bytes).replace("\r\n", "");
            stringBuilder.append(str);
        } else {
            System.out.println("Received: " + chunk);
            String jsonResponse = controllerManager.handleRequest(chunk, clientIp);
            ByteBuf responseBuf = Unpooled.copiedBuffer(jsonResponse, CharsetUtil.UTF_8);
            ctx.write(responseBuf);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}