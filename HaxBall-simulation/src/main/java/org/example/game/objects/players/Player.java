package org.example.game.objects.players;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.game.objects.Object;
import org.example.game.objects.ball.Ball;

import java.awt.*;

import static org.example.Main.SCREEN_H;
import static org.example.Main.SCREEN_W;

@Data
@NoArgsConstructor
public abstract class Player extends Object {

    private int points = 0;
    private char side;
    public Player(float x, float y, char side, Color color){
        super(x, y, 0.1f, color);
        this.side = side;
    }

    @Override
    public void checkWallHit(Rectangle boundaries, Rectangle goalBoundaries){
        if (getYCoord() <= getR())//up & down
            setYCoord(getR());
        else if(getYCoord() >= boundaries.y + boundaries.height + getR() - 10)
            setYCoord(boundaries.y + boundaries.height + getR() - 10);

        if (getXCoord() <= getR())//up & down
            setXCoord(getR());
        else if(getXCoord() >= boundaries.x + boundaries.width + getR())
            setXCoord(boundaries.x + boundaries.width + getR());

        checkGoalHit(goalBoundaries);
    }

    public void checkGoalHit(Rectangle goalBoundaries){
        if(getXCoord() - getR() < goalBoundaries.x || getXCoord() + getR() > goalBoundaries.x + goalBoundaries.width) {
            if (between(getYCoord(), goalBoundaries.y, getYCoord() + getR()))
                setYCoord(goalBoundaries.y - getR());
            else if (between(getYCoord(), goalBoundaries.y, getYCoord() - getR()))
                setYCoord(goalBoundaries.y + getR());
            else if (between(getYCoord(), goalBoundaries.y + goalBoundaries.height, getYCoord() + getR()))
                setYCoord(goalBoundaries.y + goalBoundaries.height - getR());
            else if (between(getYCoord(), goalBoundaries.y + goalBoundaries.height, getYCoord() - getR()))
                setYCoord(goalBoundaries.y + goalBoundaries.height + getR());
        }
    }

    public void getReadyToNextRound(float x, float y){
        setXCoord(x);
        setYCoord(y);
        setXVector(0);
        setYVector(0);
    }
}
