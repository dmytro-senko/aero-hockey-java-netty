package game.client;

import static game.Constants.HOST;
import static game.Constants.PORT;

public class ClientApp {
    public static void main(String[] args) {
        Client client = new Client(HOST, PORT);
        client.run();
    }
}
