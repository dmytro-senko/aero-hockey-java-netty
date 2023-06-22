package game.model;

public class Ball {
    private int ballX;
    private int ballY;
    private int spedBallX;
    private int spedBallY;

    public Ball() {
        this.ballX = 2;
        this.ballY = 4;
        this.spedBallX = 1;
        this.spedBallY = 1;
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
