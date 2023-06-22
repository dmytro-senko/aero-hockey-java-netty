package game.client;

import game.Game;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String> {
    private Game game;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        game = new Game();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String message) {
        if (message.startsWith("Update:")) {
            game.updateClientsGame(message);
            System.out.println(message.split(":")[1]);
            game.initializeGameField();
            game.printGameField();
            System.out.println(message.split(":")[8]);
        } else {
            System.out.println(message);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Соединение с сервером потеряно!");
        ctx.close();
        System.exit(0);
    }
}
