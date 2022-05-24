package org.example.game.objects;

import lombok.Data;
import org.example.game.objects.ball.Ball;
import org.example.game.objects.players.Player;

import java.awt.*;

@Data
public abstract class Object {

    private float xCoord = 0;
    private float yCoord = 0;
    private float xVector = 0;
    private float yVector = 0;
    private float r = 30;
    private float deltaT = 0.1f;
    private Color color = Color.BLACK;

    public Object(){}

    public abstract void move(Player enemy, Ball ball, Rectangle boundaries, Rectangle goalBoundaries);
    public abstract void checkWallHit(Rectangle boundaries, Rectangle goalBoundaries);

    public Object(float x, float y, float deltaT, Color color) {
        xCoord = x;
        yCoord = y;
        xVector = 0;
        yVector = 0;
        this.deltaT = deltaT;
        this.color = color;
    }

    public boolean checkHit(Object object){
        return distance(object) < r+object.getR();
    }

    public double distance(Object object){
        return Math.sqrt(Math.pow(xCoord- object.xCoord, 2)+Math.pow(yCoord- object.yCoord, 2));
    }

    public double distanceFromPoint(float x, float y){
        return Math.sqrt(Math.pow(xCoord-x, 2)+Math.pow(yCoord-y, 2));
    }

    public float getSignOfNumber(float number){
        return (number != 0)? number/Math.abs(number) : 0;
    }

    public boolean between(float left, float middle, float right){
        return ((left < middle && middle < right) || (left > middle && middle > right));
    }

    public void ridOfCollision(Player enemy, Rectangle boundaries, Rectangle goalBoundaries){
        int vectorY = Float.compare(enemy.getYCoord(), getYCoord());
        int vectorX = Float.compare(enemy.getXCoord(), getXCoord());

        while(checkHit(enemy)){
            setYCoord(getYCoord()-vectorY);
            setXCoord(getXCoord()-vectorX);
            enemy.setYCoord(enemy.getYCoord()+vectorY);
            enemy.setXCoord(enemy.getXCoord()+vectorX);
            checkWallHit(boundaries, goalBoundaries);
            enemy.checkWallHit(boundaries, goalBoundaries);
        }

    }
}
