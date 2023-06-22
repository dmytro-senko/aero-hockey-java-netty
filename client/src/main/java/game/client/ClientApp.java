package game.client;

/*
D:
cd D:\Java\IdeaProjects\server-client-game\client\target
java -jar client-1.0-SNAPSHOT.jar

 */


public class ClientApp {
    private static final int PORT = 8888;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        Client client = new Client(HOST, PORT);
        client.run();
    }
}
