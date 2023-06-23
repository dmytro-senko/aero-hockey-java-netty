package game.server;

import static game.Constants.PORT;

public class ServerApp {
    public static void main(String[] args) {
        Server server = new Server(PORT);
        server.run();
    }
}
