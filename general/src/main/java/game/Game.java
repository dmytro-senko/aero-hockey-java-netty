package game;

import java.util.ArrayList;
import java.util.List;

import static game.Constants.BALL_CHAR;
import static game.Constants.EMPTY_CHAR;
import static game.Constants.INDEX_FIRST_ROCKET;
import static game.Constants.INDEX_SECOND_ROCKET;
import static game.Constants.INDEX_VALUE_BALL_X;
import static game.Constants.INDEX_VALUE_BALL_Y;
import static game.Constants.INDEX_VALUE_ROCKET_ONE_Y_ONE_PLAYER;
import static game.Constants.INDEX_VALUE_ROCKET_ONE_Y_TWO_PLAYER;
import static game.Constants.INDEX_VALUE_ROCKET_TWO_Y_ONE_PLAYER;
import static game.Constants.INDEX_VALUE_ROCKET_TWO_Y_TWO_PLAYER;
import static game.Constants.LEFT_BOUND_GAME_FIELD;
import static game.Constants.LENGTH_GAME_FIELD;
import static game.Constants.LOWER_BOUND_GAME_FIELD;
import static game.Constants.RIGHT_BOUND_GAME_FIELD;
import static game.Constants.ROCKET_CHAR;
import static game.Constants.SEPARATOR_CHAR;
import static game.Constants.SPED_BALL_DEFAULT;
import static game.Constants.START_BALL_NEAR_PLAYER_ONE_X;
import static game.Constants.START_BALL_NEAR_PLAYER_ONE_Y;
import static game.Constants.START_BALL_NEAR_PLAYER_TWO_X;
import static game.Constants.START_BALL_NEAR_PLAYER_TWO_Y;
import static game.Constants.UPPER_BOUND_GAME_FIELD;
import static game.Constants.VALUE_X_START_ROCKET_PLAYER_ONE;
import static game.Constants.VALUE_X_START_ROCKET_PLAYER_TWO;
import static game.Constants.WALL_CHAR;
import static game.Constants.WIDTH_GAME_FIELD;

public class Game {
    private static final char[][] GAME_FIELD = new char[LENGTH_GAME_FIELD][WIDTH_GAME_FIELD];
    private List<Rocket> rockets;
    private Ball ball;

    public Game() {
        this.rockets = new ArrayList<>(List.of(new Rocket(VALUE_X_START_ROCKET_PLAYER_ONE),
                new Rocket(VALUE_X_START_ROCKET_PLAYER_TWO)));
        this.ball = new Ball();
        initializeGameField();
    }

    public void initializeGameField() {
        // Заполнение игрового поля
        for (int i = 0; i < LENGTH_GAME_FIELD; i++) {
            for (int j = 0; j < WIDTH_GAME_FIELD; j++) {
                if (j == 0 || j == (WIDTH_GAME_FIELD - 1)) {
                    GAME_FIELD[i][j] = WALL_CHAR;
                } else {
                    GAME_FIELD[i][j] = EMPTY_CHAR;
                }
            }
        }
        // мяч
        GAME_FIELD[ball.getBallX()][ball.getBallY()] = BALL_CHAR;
        Rocket firstRocket = rockets.get(INDEX_FIRST_ROCKET);
        Rocket secondRocket = rockets.get(INDEX_SECOND_ROCKET);
        // ракетки
        GAME_FIELD[firstRocket.getRocketX()][firstRocket.getRocketOneY()] =
                GAME_FIELD[firstRocket.getRocketX()][firstRocket.getRocketTwoY()] =
                        GAME_FIELD[secondRocket.getRocketX()][secondRocket.getRocketOneY()] =
                                GAME_FIELD[secondRocket.getRocketX()][secondRocket.getRocketTwoY()] = ROCKET_CHAR;
    }

    public void printGameField() {
        // Вывод игрового поля в консоль
        for (char[] chars : GAME_FIELD) {
            for (char aChar : chars) {
                System.out.print(aChar);
            }
            System.out.println();
        }
    }

    public void updateRockets(String namePlayer, String msg) {
        //Обновление положения клюшек на сервере на основе полученного от клиента сообщения
        int rocketOneYOnePlayer = rockets.get(INDEX_FIRST_ROCKET).getRocketOneY();
        int rocketTwoYOnePlayer = rockets.get(INDEX_FIRST_ROCKET).getRocketTwoY();
        int rocketOneYTwoPlayer = rockets.get(INDEX_SECOND_ROCKET).getRocketOneY();
        int rocketTwoYTwoPlayer = rockets.get(INDEX_SECOND_ROCKET).getRocketTwoY();
        if (msg.equals("LEFT")) {
            if (rockets.get(INDEX_FIRST_ROCKET).getNamePlayer().equals(namePlayer) && rocketOneYOnePlayer != LEFT_BOUND_GAME_FIELD) {
                rocketOneYOnePlayer--;
                rocketTwoYOnePlayer--;
            } else if (rockets.get(INDEX_SECOND_ROCKET).getNamePlayer().equals(namePlayer) && rocketOneYTwoPlayer != LEFT_BOUND_GAME_FIELD){
                rocketOneYTwoPlayer--;
                rocketTwoYTwoPlayer--;
            }
        } else if (msg.equals("RIGHT")) {
            if (rockets.get(INDEX_FIRST_ROCKET).getNamePlayer().equals(namePlayer) && rocketTwoYOnePlayer != RIGHT_BOUND_GAME_FIELD) {
                rocketOneYOnePlayer++;
                rocketTwoYOnePlayer++;
            } else if (rockets.get(INDEX_SECOND_ROCKET).getNamePlayer().equals(namePlayer) && rocketTwoYTwoPlayer != RIGHT_BOUND_GAME_FIELD){
                rocketOneYTwoPlayer++;
                rocketTwoYTwoPlayer++;
            }
        }
        rockets.get(INDEX_FIRST_ROCKET).setRocketOneY(rocketOneYOnePlayer);
        rockets.get(INDEX_FIRST_ROCKET).setRocketTwoY(rocketTwoYOnePlayer);
        rockets.get(INDEX_SECOND_ROCKET).setRocketOneY(rocketOneYTwoPlayer);
        rockets.get(INDEX_SECOND_ROCKET).setRocketTwoY(rocketTwoYTwoPlayer);
    }

    public void updateClientsGame(String msg) {
        // Обновляем Игру на стороне клиента на основе полученного сообщения от сервера
        String[] split = msg.split(String.valueOf(SEPARATOR_CHAR));
        rockets.get(INDEX_FIRST_ROCKET).setRocketOneY(Integer.parseInt(split[INDEX_VALUE_ROCKET_ONE_Y_ONE_PLAYER]));
        rockets.get(INDEX_FIRST_ROCKET).setRocketTwoY(Integer.parseInt(split[INDEX_VALUE_ROCKET_TWO_Y_ONE_PLAYER]));
        rockets.get(INDEX_SECOND_ROCKET).setRocketOneY(Integer.parseInt(split[INDEX_VALUE_ROCKET_ONE_Y_TWO_PLAYER]));
        rockets.get(INDEX_SECOND_ROCKET).setRocketTwoY(Integer.parseInt(split[INDEX_VALUE_ROCKET_TWO_Y_TWO_PLAYER]));
        ball.setBallX(Integer.parseInt(split[INDEX_VALUE_BALL_X]));
        ball.setBallY(Integer.parseInt(split[INDEX_VALUE_BALL_Y]));
    }

    public void updateGameAfterExitPlayer() {
        //обновление игры после выхода игрока
        rockets.get(INDEX_FIRST_ROCKET).setPoint(0);
        rockets.get(INDEX_SECOND_ROCKET).setPoint(0);
        ball = new Ball();
        rockets.get(INDEX_FIRST_ROCKET).initializeRocketPlayer();
        rockets.get(INDEX_SECOND_ROCKET).initializeRocketPlayer();
    }

    public String getMessageForClients() {
        //формируем сообщение для клиентов на сервере
        StringBuilder sb = new StringBuilder();
        sb.append("Update:")
                .append(getGameScoreOnePlayer())
                .append(updateRockets())
                .append(updateMovedBallString())
                .append(getGameScoreTwoPlayer());
        return sb.toString();
    }

    public Rocket getRocketByPlayerName(String namePlayer) {
        return rockets.stream()
                .filter(r -> r.getNamePlayer().equals(namePlayer))
                .findFirst().get();
    }

    public Rocket getRocketByPlayerNameIsNull() {
        return rockets.stream()
                .filter(r -> r.getNamePlayer() == null)
                .findFirst().get();
    }

    private String updateRockets() {
        StringBuilder sb = new StringBuilder();
        sb.append(rockets.get(INDEX_FIRST_ROCKET).getRocketOneY()).append(SEPARATOR_CHAR)
                .append(rockets.get(INDEX_FIRST_ROCKET).getRocketTwoY()).append(SEPARATOR_CHAR)
                .append(rockets.get(INDEX_SECOND_ROCKET).getRocketOneY()).append(SEPARATOR_CHAR)
                .append(rockets.get(INDEX_SECOND_ROCKET).getRocketTwoY()).append(SEPARATOR_CHAR);
        return sb.toString();
    }

    private String updateMovedBallString() {
        int spedBallX = ball.getSpedBallX();
        int spedBallY = ball.getSpedBallY();
        int ballX = ball.getBallX();
        int ballY = ball.getBallY();
        if (ballX == UPPER_BOUND_GAME_FIELD || ballX == LOWER_BOUND_GAME_FIELD) {
            return updateBallAndPointsIfGoalWasScored(spedBallX, spedBallY, ballX, ballY);
        }
        if (conditionReboundBallFromRocket(ballX, ballY) ) {
            spedBallX = -spedBallX;
        }
        if (ballY == LEFT_BOUND_GAME_FIELD || ballY == RIGHT_BOUND_GAME_FIELD) {
            spedBallY = -spedBallY;
        }
        ballX = ballX + spedBallX;
        ballY = ballY + spedBallY;

        StringBuilder sb = new StringBuilder();
        sb.append(ballX).append(SEPARATOR_CHAR).append(ballY).append(SEPARATOR_CHAR);
        ball.setBallX(ballX);
        ball.setBallY(ballY);
        ball.setSpedBallX(spedBallX);
        ball.setSpedBallY(spedBallY);
        return sb.toString();
    }

    private String updateBallAndPointsIfGoalWasScored(int spedBallX, int spedBallY, int ballX, int ballY) {
        //изменение счета и положения мяча при забитом голе
        StringBuilder sb = new StringBuilder();
        int point;
        if (ballX == UPPER_BOUND_GAME_FIELD) {
            point = rockets.get(INDEX_SECOND_ROCKET).getPoint() + 1;
            rockets.get(INDEX_SECOND_ROCKET).setPoint(point);
            ballX = START_BALL_NEAR_PLAYER_TWO_X;
            ballY = START_BALL_NEAR_PLAYER_TWO_Y;
            spedBallX = -SPED_BALL_DEFAULT;
            spedBallY = -SPED_BALL_DEFAULT;
        }
        if (ballX == LOWER_BOUND_GAME_FIELD) {
            point = rockets.get(INDEX_FIRST_ROCKET).getPoint() + 1;
            rockets.get(INDEX_FIRST_ROCKET).setPoint(point);
            ballX = START_BALL_NEAR_PLAYER_ONE_X;
            ballY = START_BALL_NEAR_PLAYER_ONE_Y;
            spedBallX = SPED_BALL_DEFAULT;
            spedBallY = SPED_BALL_DEFAULT;
        }
        sb.append(ballX).append(SEPARATOR_CHAR).append(ballY).append(SEPARATOR_CHAR);
        ball.setBallX(ballX);
        ball.setBallY(ballY);
        ball.setSpedBallX(spedBallX);
        ball.setSpedBallY(spedBallY);
        return sb.toString();
    }

    private String getGameScoreOnePlayer() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player ")
                .append(rockets.get(INDEX_FIRST_ROCKET).getNamePlayer())
                .append(" - ")
                .append(rockets.get(INDEX_FIRST_ROCKET).getPoint())
                .append(":");
        return sb.toString();
    }

    private String getGameScoreTwoPlayer() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player ")
                .append(rockets.get(INDEX_SECOND_ROCKET).getNamePlayer())
                .append(" - ")
                .append(rockets.get(INDEX_SECOND_ROCKET).getPoint());
        return sb.toString();
    }

    private boolean conditionReboundBallFromRocket(int ballX, int ballY) {
        return (ballX ==  rockets.get(INDEX_FIRST_ROCKET).getRocketX()
                && (ballY ==  rockets.get(INDEX_FIRST_ROCKET).getRocketOneY()
                || ballY ==  rockets.get(INDEX_FIRST_ROCKET).getRocketTwoY()))
                || (ballX ==  rockets.get(INDEX_SECOND_ROCKET).getRocketX()
                && (ballY == rockets.get(INDEX_SECOND_ROCKET).getRocketOneY()
                || ballY == rockets.get(INDEX_SECOND_ROCKET).getRocketTwoY()));
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public List<Rocket> getRockets() {
        return rockets;
    }

    public void setRockets(List<Rocket> rockets) {
        this.rockets = rockets;
    }

}
