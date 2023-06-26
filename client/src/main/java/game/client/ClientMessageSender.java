package game.client;

import io.netty.channel.Channel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static game.Constants.EXIT_COMMAND;
import static game.Constants.LEFT_COMMAND;
import static game.Constants.RIGHT_COMMAND;

public class ClientMessageSender {
    private final Channel channel;

    public ClientMessageSender(Channel channel) {
        this.channel = channel;
    }

    public void sendInputToServer() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                String message = in.readLine();
                if (message.equalsIgnoreCase(EXIT_COMMAND)) {
                    break;
                } else if (message.equalsIgnoreCase("a")) {
                    sendMessageToServer(LEFT_COMMAND);
                } else if (message.equalsIgnoreCase("d")) {
                    sendMessageToServer(RIGHT_COMMAND);
                } else {
                    sendMessageToServer(message);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessageToServer(String message) {
        channel.writeAndFlush(message);
    }
}