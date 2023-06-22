package game.model;

public class Rocket {
    private String namePlayer;
    private int point;
    private int rocketOneY;
    private int rocketTwoY;
    private int rocketX;

    public Rocket(int rocketX) {
        this.rocketX = rocketX;
        this.point = 0;
        initializeRocketPlayer();
    }

    public void initializeRocketPlayer() {
        rocketOneY = 4;
        rocketTwoY = 5;
    }

    public int getRocketX() {
        return rocketX;
    }

    public void setRocketX(int rocketX) {
        this.rocketX = rocketX;
    }

    public int getRocketOneY() {
        return rocketOneY;
    }

    public void setRocketOneY(int rocketOneY) {
        this.rocketOneY = rocketOneY;
    }

    public int getRocketTwoY() {
        return rocketTwoY;
    }

    public void setRocketTwoY(int rocketTwoY) {
        this.rocketTwoY = rocketTwoY;
    }

    public String getNamePlayer() {
        return namePlayer;
    }

    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
