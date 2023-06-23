package game.model;

import static game.Constants.FIRST_VALUE_Y_START_ROCKET;
import static game.Constants.SECOND_VALUE_Y_START_ROCKET;

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
        rocketOneY = FIRST_VALUE_Y_START_ROCKET;
        rocketTwoY = SECOND_VALUE_Y_START_ROCKET;
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
