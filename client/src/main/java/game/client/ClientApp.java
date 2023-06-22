package game.client;

public class ClientApp {
    private static final int PORT = 8888;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        Client client = new Client(HOST, PORT);
        client.run();
    }
}
