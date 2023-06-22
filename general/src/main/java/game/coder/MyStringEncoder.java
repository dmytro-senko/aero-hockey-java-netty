package game.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

public class MyStringEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) {
        out.writeBytes(msg.getBytes(CharsetUtil.UTF_8));
    }
}
