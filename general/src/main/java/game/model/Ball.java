package game.model;

import static game.Constants.SPED_BALL_DEFAULT;
import static game.Constants.START_BALL_NEAR_PLAYER_ONE_X;
import static game.Constants.START_BALL_NEAR_PLAYER_ONE_Y;

public class Ball {
    private int ballX;
    private int ballY;
    private int spedBallX;
    private int spedBallY;

    public Ball() {
        this.ballX = START_BALL_NEAR_PLAYER_ONE_X;
        this.ballY = START_BALL_NEAR_PLAYER_ONE_Y;
        this.spedBallX = SPED_BALL_DEFAULT;
        this.spedBallY = SPED_BALL_DEFAULT;
    }

    public int getBallX() {
        return ballX;
    }

    public void setBallX(int ballX) {
        this.ballX = ballX;
    }

    public int getBallY() {
        return ballY;
    }

    public void setBallY(int ballY) {
        this.ballY = ballY;
    }

    public int getSpedBallX() {
        return spedBallX;
    }

    public void setSpedBallX(int spedBallX) {
        this.spedBallX = spedBallX;
    }

    public int getSpedBallY() {
        return spedBallY;
    }

    public void setSpedBallY(int spedBallY) {
        this.spedBallY = spedBallY;
    }
}
