package game.server;

import static game.Constants.DEFAULT_PORT;

public class ServerApp {
    public static void main(String[] args) {
        Server server = new Server(DEFAULT_PORT);
        server.run();
    }
}
