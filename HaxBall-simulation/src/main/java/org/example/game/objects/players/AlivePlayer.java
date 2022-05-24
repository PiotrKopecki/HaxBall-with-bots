package org.example.game.objects.players;

import org.example.game.objects.ball.Ball;

import java.awt.*;

public class AlivePlayer extends Player{
    public AlivePlayer(float x, float y, char side, Color color) {
        super(x, y, side, color);
    }

    @Override
    public void move(Player enemy, Ball ball, Rectangle boundaries, Rectangle goalBoundaries) {
        setXCoord(getXCoord() + getXVector());
        setYCoord(getYCoord() + getYVector());
        checkWallHit(boundaries, goalBoundaries);
    }
}
