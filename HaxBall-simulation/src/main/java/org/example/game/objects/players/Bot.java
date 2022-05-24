package org.example.game.objects.players;

import lombok.Data;
import org.example.game.objects.ball.Ball;

import java.awt.*;

@Data
public class Bot extends Player{
    private int mode;
    public Bot(float x, float y, char side, Color color, int mode) {
        super(x, y, side, color);
        this.mode = mode;
    }

    @Override
    public void move(Player enemy, Ball ball, Rectangle boundaries, Rectangle goalBoundaries){
        switch (mode) {
            case 1 -> bot1(ball); // bot1
            case 2 -> bot2(ball, enemy, goalBoundaries);
            case 3 -> bot3(ball, enemy, goalBoundaries); // bot2
            case 4 -> bot4(ball, enemy, goalBoundaries); // bot3
        }
        setXCoord(getXCoord() + getXVector());
        setYCoord(getYCoord() + getYVector());
        checkWallHit(boundaries, goalBoundaries);
    }

    public void bot1(Ball ball){//always to ball
        toBall(ball);
    }

    public void bot2(Ball ball, Player enemy, Rectangle goalBoundaries){//always near goal line
        toGoalLine(ball, enemy, goalBoundaries);
    }

    public void bot3(Ball ball, Player enemy, Rectangle goalBoundaries){//check distance and decide to go to ball or stay near goal line
        if(distance(ball) - getR() >enemy.distance(ball))
            toGoalLine(ball, enemy, goalBoundaries);
        else
            toBall(ball);
    }

    public void bot4(Ball ball, Player enemy, Rectangle goalBoundaries){
        if(distance(ball) - getR() >enemy.distance(ball))
            toGoalLine(ball, enemy, goalBoundaries);
        else if(distance(ball) < ball.getR() + 1.5*getR())
            toStrike(ball, goalBoundaries);
        else
            toBall(ball);
    }

    public void toBall(Ball ball){
        float x = getSignOfNumber(ball.getXCoord() - getXCoord());
        float y = getSignOfNumber(ball.getYCoord() - getYCoord());
        setXVector(x*5);
        setYVector(y*5);
    }

    public void toGoalLine(Ball ball, Player enemy, Rectangle goalBoundaries){
        float xS = (getSide() == 'L')? goalBoundaries.x + getR() : goalBoundaries.x+goalBoundaries.width - getR();
        float yS = getYFromLine(ball, enemy, xS, goalBoundaries.y + (float)goalBoundaries.height/2) ;

        if(yS <= goalBoundaries.y || yS >= goalBoundaries.y + goalBoundaries.height){
            yS = (yS <= goalBoundaries.y)? goalBoundaries.y + getR() : goalBoundaries.y + goalBoundaries.height - getR();
        }
        if ((Math.abs(yS - getYCoord()) > 5)) {
            setYVector(5 * getSignOfNumber(yS - getYCoord()));
        } else {
            setYVector(yS - getYCoord());
        }

        if(Math.abs(getXCoord()-xS) > 5)
            setXVector(5*getSignOfNumber(xS - getXCoord()));
        else{
            setXVector(xS - getXCoord());
        }
    }

    public float getYFromLine(Ball ball, Player enemy, float x, float y){//do line from 2 points (ball & enemy) return value of this function for parameter x
        if (ball.getXCoord() == enemy.getXCoord()){
            return y;
        }else{
            float a = (ball.getYCoord() - enemy.getYCoord())/(ball.getXCoord() - enemy.getXCoord());
            float b = ball.getYCoord() - a*ball.getXCoord();
            return a*x+b;
        }
    }

    public void toStrike(Ball ball, Rectangle goalBoundaries) {
        int enemyGoalX = getSide() == 'L' ? goalBoundaries.x + goalBoundaries.width: goalBoundaries.x;

        if (between(getXCoord(), ball.getXCoord(), enemyGoalX))
            makeLineWithBall(ball, goalBoundaries);
        else{
            setXVector(5*getSignOfNumber(ball.getXCoord() - getXCoord()));
            setYVector(0);
        }
    }

    public void makeLineWithBall(Ball ball, Rectangle goalBoundaries){
        //tworzymy prostą w dwóch punktów, piłki i gracza, sprawdzamy czy dla goalX wartość y w tej funkcji znajdzie się pomiędzy bramki
        int enemyGoalX = getSide() == 'L' ? goalBoundaries.x + goalBoundaries.width: goalBoundaries.x;
        float a = (getYCoord()-ball.getYCoord())/(getXCoord() - ball.getXCoord());
        float b = getYCoord() - a*getXCoord();
        float y = a*enemyGoalX + b;
        if(y <= goalBoundaries.y)//strzał by był nad bramkę
            setYVector(-5);
        else if(y >= goalBoundaries.y + goalBoundaries.height)//strzał by był nad bramkę
            setYVector(5);
        else
            toBall(ball);
    }
}
