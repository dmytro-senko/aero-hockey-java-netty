package game;

import game.model.Ball;
import game.model.Rocket;
import game.service.GameUpdater;
import java.util.ArrayList;
import java.util.List;
import static game.Constants.BALL_CHAR;
import static game.Constants.EMPTY_CHAR;
import static game.Constants.INDEX_FIRST_ROCKET;
import static game.Constants.INDEX_SECOND_ROCKET;
import static game.Constants.INDEX_VALUE_BALL_X;
import static game.Constants.INDEX_VALUE_BALL_Y;
import static game.Constants.INDEX_VALUE_ROCKET_ONE_Y_PLAYER_ONE;
import static game.Constants.INDEX_VALUE_ROCKET_ONE_Y_PLAYER_TWO;
import static game.Constants.INDEX_VALUE_ROCKET_TWO_Y_PLAYER_ONE;
import static game.Constants.INDEX_VALUE_ROCKET_TWO_Y_PLAYER_TWO;
import static game.Constants.LEFT_BOUND_GAME_FIELD;
import static game.Constants.LEFT_COMMAND;
import static game.Constants.LENGTH_GAME_FIELD;
import static game.Constants.LOWER_BOUND_GAME_FIELD;
import static game.Constants.RIGHT_BOUND_GAME_FIELD;
import static game.Constants.RIGHT_COMMAND;
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
    private final char[][] gameField;
    private List<Rocket> rockets;
    private Ball ball;

    public Game() {
        this.rockets = new ArrayList<>(List.of(new Rocket(VALUE_X_START_ROCKET_PLAYER_ONE),
                new Rocket(VALUE_X_START_ROCKET_PLAYER_TWO)));
        this.ball = new Ball();
        this.gameField = new char[LENGTH_GAME_FIELD][WIDTH_GAME_FIELD];
        initializeGameField();
    }

    public void initializeGameField() {
        // Заполнение игрового поля
        for (int i = 0; i < LENGTH_GAME_FIELD; i++) {
            for (int j = 0; j < WIDTH_GAME_FIELD; j++) {
                if (j == 0 || j == (WIDTH_GAME_FIELD - 1)) {
                    gameField[i][j] = WALL_CHAR;
                } else {
                    gameField[i][j] = EMPTY_CHAR;
                }
            }
        }
        // мяч
        gameField[ball.getBallX()][ball.getBallY()] = BALL_CHAR;
        Rocket firstRocket = getFirstRocket();
        Rocket secondRocket = getSecondRocket();
        // ракетки
        gameField[firstRocket.getRocketX()][firstRocket.getRocketOneY()] =
                gameField[firstRocket.getRocketX()][firstRocket.getRocketTwoY()] =
                        gameField[secondRocket.getRocketX()][secondRocket.getRocketOneY()] =
                                gameField[secondRocket.getRocketX()][secondRocket.getRocketTwoY()] = ROCKET_CHAR;
    }

    public void printGameField() {
        // Вывод игрового поля в консоль
        for (char[] chars : gameField) {
            for (char aChar : chars) {
                System.out.print(aChar);
            }
            System.out.println();
        }
    }

    public void updateRockets(String namePlayer, String msg) {
        //Обновление положения клюшек на сервере на основе полученного от клиента сообщения
        int rocketOneYOnePlayer = getFirstRocket().getRocketOneY();
        int rocketTwoYOnePlayer = getFirstRocket().getRocketTwoY();
        int rocketOneYTwoPlayer = getSecondRocket().getRocketOneY();
        int rocketTwoYTwoPlayer = getSecondRocket().getRocketTwoY();
        if (msg.equals(LEFT_COMMAND)) {
            if (getFirstRocket().getNamePlayer().equals(namePlayer) && rocketOneYOnePlayer != LEFT_BOUND_GAME_FIELD) {
                rocketOneYOnePlayer--;
                rocketTwoYOnePlayer--;
            } else if (getSecondRocket().getNamePlayer().equals(namePlayer) && rocketOneYTwoPlayer != LEFT_BOUND_GAME_FIELD){
                rocketOneYTwoPlayer--;
                rocketTwoYTwoPlayer--;
            }
        } else if (msg.equals(RIGHT_COMMAND)) {
            if (getFirstRocket().getNamePlayer().equals(namePlayer) && rocketTwoYOnePlayer != RIGHT_BOUND_GAME_FIELD) {
                rocketOneYOnePlayer++;
                rocketTwoYOnePlayer++;
            } else if (getSecondRocket().getNamePlayer().equals(namePlayer) && rocketTwoYTwoPlayer != RIGHT_BOUND_GAME_FIELD){
                rocketOneYTwoPlayer++;
                rocketTwoYTwoPlayer++;
            }
        }
        getFirstRocket().setRocketOneY(rocketOneYOnePlayer);
        getFirstRocket().setRocketTwoY(rocketTwoYOnePlayer);
        getSecondRocket().setRocketOneY(rocketOneYTwoPlayer);
        getSecondRocket().setRocketTwoY(rocketTwoYTwoPlayer);
    }

    public void updateClientsGame(String msg) {
        // обновление игры на стороне клиента на основе полученного сообщения от сервера
        String[] split = msg.split(String.valueOf(SEPARATOR_CHAR));
        getFirstRocket().setRocketOneY(Integer.parseInt(split[INDEX_VALUE_ROCKET_ONE_Y_PLAYER_ONE]));
        getFirstRocket().setRocketTwoY(Integer.parseInt(split[INDEX_VALUE_ROCKET_TWO_Y_PLAYER_ONE]));
        getSecondRocket().setRocketOneY(Integer.parseInt(split[INDEX_VALUE_ROCKET_ONE_Y_PLAYER_TWO]));
        getSecondRocket().setRocketTwoY(Integer.parseInt(split[INDEX_VALUE_ROCKET_TWO_Y_PLAYER_TWO]));
        ball.setBallX(Integer.parseInt(split[INDEX_VALUE_BALL_X]));
        ball.setBallY(Integer.parseInt(split[INDEX_VALUE_BALL_Y]));
    }

    public void updateGameAfterExitPlayer() {
        //обновление игры после выхода игрока
        getFirstRocket().setPoint(0);
        getSecondRocket().setPoint(0);
        ball = new Ball();
        getFirstRocket().initializeRocketPlayer();
        getSecondRocket().initializeRocketPlayer();
    }

    public String getMessageForClients() {
        //формируем сообщение на сервере для клиентов
        movedBall();
        GameUpdater gameUpdater = new GameUpdater(this);
        return gameUpdater.generateMessageForClients();
    }

    public Rocket getFirstRocket() {
        return rockets.get(INDEX_FIRST_ROCKET);
    }

    public Rocket getSecondRocket() {
        return rockets.get(INDEX_SECOND_ROCKET);
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

    private void movedBall() {
        int spedBallX = ball.getSpedBallX();
        int spedBallY = ball.getSpedBallY();
        int ballX = ball.getBallX();
        int ballY = ball.getBallY();
        if (ballX == UPPER_BOUND_GAME_FIELD || ballX == LOWER_BOUND_GAME_FIELD) {
            updateBallAndPointsIfGoalWasScored(spedBallX, spedBallY, ballX, ballY);
            return;
        }
        if (conditionReboundBallFromRocket(ballX, ballY) ) {
            spedBallX = -spedBallX;
        }
        if (ballY == LEFT_BOUND_GAME_FIELD || ballY == RIGHT_BOUND_GAME_FIELD) {
            spedBallY = -spedBallY;
        }
        ballX = ballX + spedBallX;
        ballY = ballY + spedBallY;
        updateBall(ballX, ballY, spedBallX, spedBallY);
    }

    private void updateBallAndPointsIfGoalWasScored(int spedBallX, int spedBallY, int ballX, int ballY) {
        //изменение счета и положения мяча при забитом голе
        int point;
        if (ballX == UPPER_BOUND_GAME_FIELD) {
            point = getSecondRocket().getPoint() + 1;
            getSecondRocket().setPoint(point);
            ballX = START_BALL_NEAR_PLAYER_TWO_X;
            ballY = START_BALL_NEAR_PLAYER_TWO_Y;
            spedBallX = -SPED_BALL_DEFAULT;
            spedBallY = -SPED_BALL_DEFAULT;
        }
        if (ballX == LOWER_BOUND_GAME_FIELD) {
            point = getFirstRocket().getPoint() + 1;
            getFirstRocket().setPoint(point);
            ballX = START_BALL_NEAR_PLAYER_ONE_X;
            ballY = START_BALL_NEAR_PLAYER_ONE_Y;
            spedBallX = SPED_BALL_DEFAULT;
            spedBallY = SPED_BALL_DEFAULT;
        }
        updateBall(ballX, ballY, spedBallX, spedBallY);
    }

    private void updateBall(int ballX, int ballY, int spedBallX, int spedBallY) {
        ball.setBallX(ballX);
        ball.setBallY(ballY);
        ball.setSpedBallX(spedBallX);
        ball.setSpedBallY(spedBallY);
    }

    private boolean conditionReboundBallFromRocket(int ballX, int ballY) {
        return (ballX == getFirstRocket().getRocketX()
                && (ballY == getFirstRocket().getRocketOneY()
                || ballY == getFirstRocket().getRocketTwoY()))
                || (ballX == getSecondRocket().getRocketX()
                && (ballY == getSecondRocket().getRocketOneY()
                || ballY == getSecondRocket().getRocketTwoY()));
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

}
