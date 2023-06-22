package game.server;

public class ServerApp {
    private static final int PORT = 8888;

    public static void main(String[] args) {

        Server server = new Server(PORT);
        server.run();

    }
}
