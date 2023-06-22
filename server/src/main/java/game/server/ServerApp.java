package game.server;

/*
Для запуска:
Реализовать обработку объекта вместо строки


D:
cd D:\Java\IdeaProjects\server-client-game\server\target
java -jar server-1.0-SNAPSHOT.jar

 */


public class ServerApp {
    private static final int PORT = 8888;

    public static void main(String[] args) {

        Server server = new Server(PORT);
        server.run();

    }
}
