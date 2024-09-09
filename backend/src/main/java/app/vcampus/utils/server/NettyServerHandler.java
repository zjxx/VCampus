package app.vcampus.utils.server;

import app.vcampus.utils.ControllerManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.io.FileOutputStream;
import java.io.IOException;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private final Gson gson = new Gson();
    private final ControllerManager controllerManager = new ControllerManager();
    private final StringBuilder stringBuilder = new StringBuilder();
    private boolean isFileUpload = false;
    private String initJson;
    private FileOutputStream fileOutputStream;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        String chunk = byteBuf.toString(CharsetUtil.UTF_8);


        if (chunk.contains("file_upload")) {
            isFileUpload = true;
            // Initialize file output stream
            //初始化initJson,保留}及之前的字符串
            initJson = chunk.substring(0,chunk.indexOf("}")+1);
            if(chunk.contains("}")){
                //将chunk变为}之后的字符串
                chunk = chunk.substring(chunk.indexOf("}")+1);
                //转换回byteBuf
                byteBuf = Unpooled.copiedBuffer(chunk, CharsetUtil.UTF_8);
            }
            stringBuilder.setLength(0);

        }

        if (isFileUpload) {

            // Check for a special end marker to determine the end of the message
            if (chunk.endsWith("END_OF_MESSAGE")) {
                //将stringbuilder base64解密
                byte[] bytes = java.util.Base64.getDecoder().decode(stringBuilder.toString());

                String jsonResponse = controllerManager.handleRequestWithParams(initJson,stringBuilder.toString());

                // Write JSON response
                ByteBuf responseBuf = Unpooled.copiedBuffer(jsonResponse, CharsetUtil.UTF_8);
                ctx.write(responseBuf);
                isFileUpload = false;
                stringBuilder.setLength(0);
            }
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            //删除掉换行符
            String str = new String(bytes);
            str = str.replace("\r\n","");
            bytes = str.getBytes();
            stringBuilder.append(chunk);
        } else {
            System.out.println("Received: " + chunk);
            String jsonResponse = controllerManager.handleRequest(chunk);

            // Write JSON response
            ByteBuf responseBuf = Unpooled.copiedBuffer(jsonResponse, CharsetUtil.UTF_8);
            ctx.write(responseBuf);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush(); // Flush messages
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close(); // Close connection on exception
    }
}