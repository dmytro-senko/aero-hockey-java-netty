package game.server;

import game.Game;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static List<ChannelHandlerContext> clients = new ArrayList<>();
    private static List<ChannelHandlerContext> lookers = new ArrayList<>();
    private String namePlayer;
    private String message;
    private static Game game;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        InetSocketAddress clientAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        namePlayer = String.valueOf(clientAddress.getPort());
// если удаляется 1-й игрок, тогда при подключении нового второй перезаписівается а
        if (clients.size() < 2) {
            if (clients.size() == 0) {
                game = new Game();
                game.getRockets().get(0).setNamePlayer(namePlayer);
            } else {
                game.getRocketByPlayerNameIsNull().setNamePlayer(namePlayer);
            }
            clients.add(ctx);
            message = "[Server]: " + "Новый игрок " + namePlayer + " вошел в комнату" ;
        } else {
            lookers.add(ctx);
            message = "[Server]: " + "Игрок " + namePlayer + " вошел в комнату как наблюдатель" ;
        }

        //broadcastMessage(message);
        System.out.println(message);

        sendMessageEverySecond();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        if (msg.equalsIgnoreCase("q")) {
            channelInactive(ctx);
        } else {
            //Изменение игрового поля и отправка его всем клиентам
            game.updateRockets(ctx, msg, clients);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (lookers.contains(ctx)) {
            message = "[Server]: " + "Наблюдатель " + namePlayer + " вышел из комнаты!";
            lookers.remove(ctx);

        } else {
            message = "[Server]: " + "Player " + namePlayer + " вышел из комнаты!";
            clients.remove(ctx);
            broadcastMessage(message);
            game.getRocketByPlayerName(namePlayer).setNamePlayer(null);
            game.updateGameAfterExitPlayer();
        }
        //broadcastMessage(message);
        System.out.println(message);
        ctx.close();
    }

    private void broadcastMessage(String message) {
        List<ChannelHandlerContext> listClients = new ArrayList<>();
        listClients.addAll(clients);
        listClients.addAll(lookers);

        for (ChannelHandlerContext context : listClients) {
            context.writeAndFlush(message);

        }
    }

    private void sendMessageEverySecond() {
        if (clients.size() == 2) {
            // Отправка сообщений каждую секунду
            scheduler.scheduleAtFixedRate(() -> {
                // логика движения шайбі ballX:ballY
                if (clients.size() == 2) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Update:")
                            .append(game.getGameScoreOnePlayer())
                            .append(game.updateRockets())
                            .append(game.updateMovedBallString())
                            .append(game.getGameScoreTwoPlayer());

                    message = sb.toString();
                    // Отправка сообщения всем подключенным клиентам
                    broadcastMessage(message);
                } else {
                    scheduler.shutdown();
                }
            }, 0, 1, TimeUnit.SECONDS);
        }
    }
}
