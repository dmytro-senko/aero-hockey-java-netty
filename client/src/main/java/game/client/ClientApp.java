package game.client;

import static game.Constants.DEFAULT_PORT;
import static game.Constants.HOST;

public class ClientApp {
    public static void main(String[] args) {
        Client client = new Client(HOST, DEFAULT_PORT);
        client.run();
    }
}
