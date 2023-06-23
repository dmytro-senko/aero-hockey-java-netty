package game.client;

import game.coder.MyStringDecoder;
import game.coder.MyStringEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

public class ClientChannelInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("stringDecoder", new MyStringDecoder());
        pipeline.addLast("stringEncoder", new MyStringEncoder());
        pipeline.addLast("handler", new ClientHandler());
    }
}
