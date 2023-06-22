package game;

import game.model.Ball;
import game.model.Rocket;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class Game  {
    private static final char[][] GAME_FIELD = new char[10][10];
    private List<Rocket> rockets;
    private Ball ball;
    private char ballChar = 'o';
    private char emptyChar = ' ';
    private char rocketChar = '-';
    private char wallChar = '|';


    public Game() {
        this.rockets = new ArrayList<>(List.of(new Rocket(1), new Rocket(8)));
        this.ball = new Ball();
        initializeGameField();
    }

    public void initializeGameField() {
        // Заполнение игрового поля
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (j == 0 || j == 9) {
                    GAME_FIELD[i][j] = wallChar;
                } else {
                    GAME_FIELD[i][j] = emptyChar;
                }
            }
        }

        // мяч
        GAME_FIELD[ball.getBallX()][ball.getBallY()] = ballChar;
        //игроки
        GAME_FIELD[rockets.get(0).getRocketX()][rockets.get(0).getRocketOneY()] =
                GAME_FIELD[rockets.get(0).getRocketX()][rockets.get(0).getRocketTwoY()] =
                        GAME_FIELD[rockets.get(1).getRocketX()][rockets.get(1).getRocketOneY()] =
                                GAME_FIELD[rockets.get(1).getRocketX()][rockets.get(1).getRocketTwoY()] = rocketChar;
    }

    public void printGameField() {
        // Вывод игрового поля в консоль
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(GAME_FIELD[i][j]);
            }
            System.out.println();
        }
    }

    public String updateMovedBallString() {
        StringBuilder sb = new StringBuilder();
        int spedBallX = ball.getSpedBallX();
        int spedBallY = ball.getSpedBallY();
        int ballX = ball.getBallX();
        int ballY = ball.getBallY();
        int point;
        if (ballX == 0) {
            point = rockets.get(1).getPoint() + 1;
            rockets.get(1).setPoint(point);
            ballX = 7;
            ballY = 5;
            spedBallX = -1;
            spedBallY = -1;
            sb.append(ballX).append(':').append(ballY).append(':');
            ball.setBallX(ballX);
            ball.setBallY(ballY);
            ball.setSpedBallX(spedBallX);
            ball.setSpedBallY(spedBallY);
            return sb.toString();
        }
        if (ballX == 9) {
            point = rockets.get(0).getPoint() + 1;
            rockets.get(0).setPoint(point);
            ballX = 2;
            ballY = 4;
            spedBallX = 1;
            spedBallY = 1;
            sb.append(ballX).append(':').append(ballY).append(':');
            ball.setBallX(ballX);
            ball.setBallY(ballY);
            ball.setSpedBallX(spedBallX);
            ball.setSpedBallY(spedBallY);
            return sb.toString();
        }
        if ((ballX ==  rockets.get(0).getRocketX()
                && (ballY ==  rockets.get(0).getRocketOneY()
                || ballY ==  rockets.get(0).getRocketTwoY()))
                || (ballX ==  rockets.get(1).getRocketX()
                && (ballY == rockets.get(1).getRocketOneY()
                || ballY == rockets.get(1).getRocketTwoY())) ) {
            spedBallX = -spedBallX;
        }
        if (ballY == 1 || ballY == 8) {
            spedBallY = -spedBallY;
        }
        ballX = ballX + spedBallX;
        ballY = ballY + spedBallY;

        sb.append(ballX).append(':').append(ballY).append(':');
        ball.setBallX(ballX);
        ball.setBallY(ballY);
        ball.setSpedBallX(spedBallX);
        ball.setSpedBallY(spedBallY);
        return sb.toString();
    }

    public String updateRockets() {
        StringBuilder sb = new StringBuilder();
        sb.append(rockets.get(0).getRocketOneY()).append(':')
                .append(rockets.get(0).getRocketTwoY()).append(':')
                .append(rockets.get(1).getRocketOneY()).append(':')
                .append(rockets.get(1).getRocketTwoY()).append(':');
        return sb.toString();
    }

    public void updateClientsGame(String msg) {
        // Обновляем Игру на стороне клиента на основе полученного сообщения от сервера
        String[] split = msg.split(":");
        rockets.get(0).setRocketOneY(Integer.parseInt(split[2]));
        rockets.get(0).setRocketTwoY(Integer.parseInt(split[3]));
        rockets.get(1).setRocketOneY(Integer.parseInt(split[4]));
        rockets.get(1).setRocketTwoY(Integer.parseInt(split[5]));
        ball.setBallX(Integer.parseInt(split[6]));
        ball.setBallY(Integer.parseInt(split[7]));
    }

    public void updateRockets(ChannelHandlerContext ctx, String msg, List<ChannelHandlerContext> clients) {
        //Обновление положения клюшек на основе полученного от клиента сообщения
        int rocketOneYOnePlayer = rockets.get(0).getRocketOneY();
        int rocketTwoYOnePlayer = rockets.get(0).getRocketTwoY();
        int rocketOneYTwoPlayer = rockets.get(1).getRocketOneY();
        int rocketTwoYTwoPlayer = rockets.get(1).getRocketTwoY();
        if (msg.equals("LEFT")) {
            if (clients.get(0).equals(ctx) && rocketOneYOnePlayer != 1) {
                rocketOneYOnePlayer--;
                rocketTwoYOnePlayer--;
            } else if (clients.get(1).equals(ctx) && rocketOneYTwoPlayer != 1){
                rocketOneYTwoPlayer--;
                rocketTwoYTwoPlayer--;
            }
        } else if (msg.equals("RIGHT")) {
            if (clients.get(0).equals(ctx) && rocketTwoYOnePlayer != 8) {
                rocketOneYOnePlayer++;
                rocketTwoYOnePlayer++;
            } else if (clients.get(1).equals(ctx) && rocketTwoYTwoPlayer != 8){
                rocketOneYTwoPlayer++;
                rocketTwoYTwoPlayer++;
            }
        }
        rockets.get(0).setRocketOneY(rocketOneYOnePlayer);
        rockets.get(0).setRocketTwoY(rocketTwoYOnePlayer);
        rockets.get(1).setRocketOneY(rocketOneYTwoPlayer);
        rockets.get(1).setRocketTwoY(rocketTwoYTwoPlayer);
    }

    public String getGameScoreOnePlayer() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player ")
                .append(rockets.get(0).getNamePlayer())
                .append(" - ")
                .append(rockets.get(0).getPoint())
                .append(":");
        return sb.toString();
    }

    public String getGameScoreTwoPlayer() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player ")
                .append(rockets.get(1).getNamePlayer())
                .append(" - ")
                .append(rockets.get(1).getPoint());
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

    public void updateGameAfterExitPlayer() {
        rockets.get(0).setPoint(0);
        rockets.get(1).setPoint(0);
        ball = new Ball();
        rockets.get(0).initializeRocketPlayer();
        rockets.get(1).initializeRocketPlayer();
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
