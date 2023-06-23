package game.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static game.Constants.MESSAGE_FOR_EXIT;

public class Client {
    private int port;
    private String host;

    public Client(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer());

            ChannelFuture f = b.connect(host, port).sync();
            System.out.println("[Server]: Вы подключились к серверу " + port + ". Для выхода отправить: 'q'");

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String message = in.readLine();
                if (message.equalsIgnoreCase(MESSAGE_FOR_EXIT)) {
                    break;
                } else if (message.equalsIgnoreCase("a")) {
                    f.channel().writeAndFlush("LEFT");
                } else if (message.equalsIgnoreCase("d")) {
                    f.channel().writeAndFlush("RIGHT");
                } else {
                    f.channel().writeAndFlush(message);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't connect with server on port:" + port, e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
