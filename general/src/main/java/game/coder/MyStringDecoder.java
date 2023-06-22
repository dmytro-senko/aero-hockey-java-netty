package game.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

public class MyStringDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        String decodedData = in.toString(CharsetUtil.UTF_8);
        out.add(decodedData);
        in.clear();
    }
}
