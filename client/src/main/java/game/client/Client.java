package game.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static game.Constants.EXIT_COMMAND;

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
            System.out.println("[Server]: Вы подключились к серверу " + port + ". Для выхода отправить: " + EXIT_COMMAND);

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            ClientMessageSender clientMessageSender = new ClientMessageSender(f.channel());
            clientMessageSender.sendInputToServer();
        } catch (Exception e) {
            throw new RuntimeException("Can't connect with server on port:" + port, e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
