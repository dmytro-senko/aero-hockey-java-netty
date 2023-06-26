package game.service;

import game.Game;
import game.model.Rocket;

import static game.Constants.SEPARATOR_CHAR;

public class GameUpdater {
    private Game game;

    public GameUpdater(Game game) {
        this.game = game;
    }

    public String generateMessageForClients() {
        //формируем сообщение на сервере для клиентов
        StringBuilder sb = new StringBuilder();
        sb.append("Update:")
                .append(getGameScore(game.getFirstRocket()))
                .append(SEPARATOR_CHAR)
                .append(generateUpdateRocketsMessage())
                .append(generateUpdatedBallMessage())
                .append(getGameScore(game.getSecondRocket()));
        return sb.toString();
    }

    private String getGameScore(Rocket rocket) {
        StringBuilder sb = new StringBuilder();
        sb.append("Player ")
                .append(rocket.getNamePlayer())
                .append(" - ")
                .append(rocket.getPoint());
        return sb.toString();
    }

    private String generateUpdateRocketsMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(game.getFirstRocket().getRocketOneY()).append(SEPARATOR_CHAR)
                .append(game.getFirstRocket().getRocketTwoY()).append(SEPARATOR_CHAR)
                .append(game.getSecondRocket().getRocketOneY()).append(SEPARATOR_CHAR)
                .append(game.getSecondRocket().getRocketTwoY()).append(SEPARATOR_CHAR);
        return sb.toString();
    }

    private String generateUpdatedBallMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(game.getBall().getBallX())
                .append(SEPARATOR_CHAR)
                .append(game.getBall().getBallY())
                .append(SEPARATOR_CHAR);
        return sb.toString();
    }
}
