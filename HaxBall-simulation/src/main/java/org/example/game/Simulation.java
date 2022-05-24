package org.example.game;

import lombok.Data;
import org.example.game.objects.ball.Ball;
import org.example.game.objects.players.AlivePlayer;
import org.example.game.objects.players.Bot;
import org.example.game.objects.players.Player;

import java.awt.*;

import static org.example.Main.SCREEN_H;
import static org.example.Main.SCREEN_W;

@Data
public class Simulation {
    private Bot playerOne;
    private Bot playerTwo;
    private Ball ball;
    private int firstPlayerWins;
    private int secondPlayerWins;
    private int numberOfMatches;
    private Rectangle goalBoundaries;
    private Rectangle boundaries;

    public Simulation(int numberOfMatches, int firstBotMode, int secondBotMode){
        playerOne = new Bot((float)SCREEN_W/4 - 210,(float)SCREEN_H/2 - 90, 'L', Color.RED, firstBotMode);
        playerTwo = new Bot((float)SCREEN_W - 110,(float)SCREEN_H/2 - 90, 'R', Color.BLUE, secondBotMode);
        ball = new Ball((float)SCREEN_W/2 ,(float)SCREEN_H/2 - 90,Color.WHITE);
        boundaries = new Rectangle(78, 54, 1123, 613);
        goalBoundaries = new Rectangle(78, 275, 1123, 170);
        firstPlayerWins = 0;
        secondPlayerWins = 0;
        this.numberOfMatches = numberOfMatches;
    }

    public void simulate(){
        for(int i=0 ; i < numberOfMatches ; i++){
            play();
            newGame();
        }
    }

    public void newRound(){
        playerOne.getReadyToNextRound((float)SCREEN_W/4 - 210, (float)SCREEN_H/2 - 90);
        playerTwo.getReadyToNextRound((float)SCREEN_W - 110, (float)SCREEN_H/2 - 90);
        ball = new Ball((float)SCREEN_W/2 ,(float)SCREEN_H/2 - 90,Color.WHITE);
    }

    public void newGame(){
        newRound();
        playerOne.setPoints(0);
        playerTwo.setPoints(0);
    }
    
    public boolean checkIfGameIsOver(){
        return playerTwo.getPoints() >= 3 && playerOne.getPoints() >= 3;
    }

    public void addGoal(String whichPlayer){
        if(whichPlayer == null){
            return;
        }
        else if(whichPlayer.equals("blue")){
            playerTwo.setPoints(playerTwo.getPoints()+1);
        }
        else if(whichPlayer.equals("red")){
            playerOne.setPoints(playerOne.getPoints()+1);
        }
        newRound();
    }

    public void collisionHandler(){
        //possibilities:
        //player with ball x2 -> ball bounces from players -> exception when ball touches the wall and players at the same time
        //both player with ball -> ball bounces from player -> exception when ball touches the wall and player at the same time
        //player with player -> ridOfCollision() in object class
        ball.checkCollisions(playerTwo, playerOne, boundaries, goalBoundaries);
        ball.ridOfCollision(playerTwo, boundaries, goalBoundaries);
        ball.ridOfCollision(playerOne, boundaries, goalBoundaries);
        playerTwo.ridOfCollision(playerOne, boundaries, goalBoundaries);
    }

    public void play(){
        for(int i=0 ; i < 200000 ; i++) {
            if (checkIfGameIsOver()) {
                break;
            } else {
                playerTwo.move(playerOne, ball, boundaries, goalBoundaries);
                playerOne.move(playerTwo, ball, boundaries, goalBoundaries);
                collisionHandler();
                ball.move(null, null, boundaries, goalBoundaries);
                addGoal(ball.checkIfGoal());
            }
        }
        if(playerOne.getPoints() > playerTwo.getPoints())
            firstPlayerWins++;
        else if(playerOne.getPoints() < playerTwo.getPoints())
            secondPlayerWins++;
    }

    public void printResults() {
        int draws = numberOfMatches - firstPlayerWins - secondPlayerWins;
        System.out.println("Results (" + numberOfMatches + "games):");
        System.out.println("First bot (mode " + playerOne.getMode() + ") wins " + firstPlayerWins + " times");
        System.out.println("Second bot (mode " + playerTwo.getMode() + ") wins " + secondPlayerWins + " times");
        System.out.println("Draws " + draws + " times");
    }
}
