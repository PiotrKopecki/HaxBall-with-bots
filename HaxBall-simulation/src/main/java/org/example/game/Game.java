package org.example.game;

import lombok.Data;
import org.example.game.objects.ball.Ball;
import org.example.game.objects.players.AlivePlayer;
import org.example.game.objects.players.Bot;
import org.example.game.objects.players.Player;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

import static org.example.Main.*;

@Data
public class Game extends JPanel implements ActionListener,KeyListener {
    private Player playerRed;
    private Player playerBlue;
    private Ball ball;
    private Rectangle goalBoundaries;
    private Rectangle boundaries;
    private String score = "0 : 0";
    private Timer timer;
    private final int BOX_WH = 75;
    private String gameFinished = "no";
    private boolean startNewGame = false;
    private float time = 0;
    private Image backgroundImage;

    public Game(int firstMode, int secondMode){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        String path = "assets/pitch_resized.png";
        backgroundImage = toolkit.getImage(path);
        if(firstMode == 0)
            playerBlue = new AlivePlayer((float)SCREEN_W/4 - 210,(float)SCREEN_H/2 - 90, 'L', Color.RED);
        else
            playerBlue = new Bot((float)SCREEN_W/4 - 210,(float)SCREEN_H/2 - 90, 'L', Color.RED, firstMode);
        if(secondMode == 0)
            playerRed = new AlivePlayer((float)SCREEN_W - 110,(float)SCREEN_H/2 - 90, 'R', Color.BLUE);
        else
            playerRed = new Bot((float)SCREEN_W - 110,(float)SCREEN_H/2 - 90, 'R', Color.BLUE, secondMode);
        ball = new Ball((float)SCREEN_W/2 ,(float)SCREEN_H/2 - 90,Color.WHITE);
        boundaries = new Rectangle(78, 54, 1123, 613);
        goalBoundaries = new Rectangle(78, 275, 1123, 170);
        addKeyListener(this);
        setFocusable(true);
        requestFocus();
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(10, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,SCREEN_W,SCREEN_H);
        g.drawImage(backgroundImage,0,0,null);
        printTime(g);
        printScore(g);
        printObjects(g);
        if(!gameFinished.equals("no")){
            printWinner(g);
        }
        g.dispose();
    }

    void printScore(Graphics g){
        g.setFont(new Font("Verdana", Font.BOLD, 60));
        int stringWidth = g.getFontMetrics().stringWidth(getScore());
        g.setColor(Color.white);
        g.drawString(getScore(),SCREEN_W/2 - stringWidth/2,(int)(720 + (SCREEN_H - 720)/4 + BOX_WH*0.75));
        g.setColor(Color.RED);
        g.fillRect(SCREEN_W/2 - BOX_WH - stringWidth/2 - 10,720 + (SCREEN_H - 720)/4,75,75);
        g.setColor(Color.BLUE);
        g.fillRect(SCREEN_W/2 + stringWidth/2 + 10,720 + (SCREEN_H - 720)/4,75,75);
    }

    void printObjects(Graphics g){
        g.setColor(getPlayerBlue().getColor());
        int x = (int)(getPlayerBlue().getXCoord());
        int y = (int)(getPlayerBlue().getYCoord());
        int r = (int)(getPlayerBlue().getR());
        g.fillOval(x-r,y-r,r*2,r*2);

        g.setColor(getPlayerRed().getColor());
        x = (int)(getPlayerRed().getXCoord());
        y = (int)(getPlayerRed().getYCoord());
        r = (int)(getPlayerRed().getR());
        g.fillOval(x-r,y-r,r*2,r*2);

        g.setColor(getBall().getColor());
        x = (int)(getBall().getXCoord());
        y = (int)(getBall().getYCoord());
        r = (int)(getBall().getR());
        g.fillOval(x-r,y-r,r*2,r*2);
    }

    void printTime(Graphics g){
        int tempTime = (int)time/1000;
        int minutes = 0;
        while(tempTime > 59){
            minutes++;
            tempTime -= 60;
        }
        String temp = "0";
        temp += Integer.toString(minutes);
        temp += ":";
        if(tempTime < 10){
            temp +="0";
        }
        temp += Integer.toString(tempTime);
        g.setFont(new Font("Verdana", Font.BOLD, 50));
        int stringWidth = g.getFontMetrics().stringWidth(temp);
        g.setColor(Color.white);
        g.drawString(temp,SCREEN_W/2 + 2*stringWidth,(int)(720 + (SCREEN_H - 720)/4 + BOX_WH*0.75));
    }

    public void printWinner(Graphics g){
        String temp = gameFinished;
        g.setFont(new Font("Verdana", Font.BOLD, 70));
        int stringWidth = g.getFontMetrics().stringWidth(temp);
        if(temp.equals("Its a draw")){
            g.setColor(Color.BLACK);
        }
        else if(temp.equals("Blue wins")){
            g.setColor(Color.BLUE);
        }
        else{
            g.setColor(Color.RED);
        }
        g.drawString(temp,SCREEN_W/2 - stringWidth/2,SCREEN_H/2 - stringWidth/2);
    }

    public void newRound(){
        playerBlue.getReadyToNextRound((float)SCREEN_W/4 - 210, (float)SCREEN_H/2 - 90);
        playerRed.getReadyToNextRound((float)SCREEN_W - 110, (float)SCREEN_H/2 - 90);
        ball = new Ball((float)SCREEN_W/2 ,(float)SCREEN_H/2 - 90,Color.WHITE);
    }

    public void newGame(){
        newRound();
        playerBlue.setPoints(0);
        playerRed.setPoints(0);
        timer.restart();
        score = "0 : 0";
        gameFinished = "no";
        startNewGame = false;
        time = 0;
    }

    public boolean checkIfGameIsOver(){
        if(playerBlue.getPoints() >= 3){
            gameFinished = "Blue wins";
            return true;
        }
        else if(playerRed.getPoints() >= 3){
            gameFinished = "Red wins";
            return true;
        }
        else if(time >= 1000*60*3){
            if(playerBlue.getPoints() > playerRed.getPoints()){
                gameFinished = "Blue wins";
            }
            else if(playerRed.getPoints() > playerBlue.getPoints()){
                gameFinished = "Red wins";
            }
            else{
                gameFinished = "Its a draw";
            }
            return true;
        }
        else{
            gameFinished = "no";
            return false;
        }
    }

    public void addGoal(String whichPlayer){
        if(whichPlayer == null){
            return;
        }
        else if(whichPlayer.equals("blue")){
            playerBlue.setPoints(playerBlue.getPoints()+1);
        }
        else if(whichPlayer.equals("red")){
            playerRed.setPoints(playerRed.getPoints()+1);
        }
        newRound();
    }

    public void changeScore(){
        String s = Integer.toString(playerRed.getPoints());
        score = s;
        score += " : ";
        s = Integer.toString(playerBlue.getPoints());
        score += s;
    }

    public void collisionHandler(){
        //possibilities:
        //player with ball x2 -> ball bounces from players -> exception when ball touches the wall and players at the same time
        //both player with ball -> ball bounces from player -> exception when ball touches the wall and player at the same time
        //player with player -> ridOfCollision() in object class
        ball.checkCollisions(playerBlue, playerRed, boundaries, goalBoundaries);
        ball.ridOfCollision(playerBlue, boundaries, goalBoundaries);
        ball.ridOfCollision(playerRed, boundaries, goalBoundaries);
        playerBlue.ridOfCollision(playerRed, boundaries, goalBoundaries);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int button = e.getKeyCode();
        Player alive = (playerBlue instanceof AlivePlayer)? playerBlue : playerRed;
        if(button == KeyEvent.VK_DOWN){
            alive.setYVector(5);
        }
        if(button == KeyEvent.VK_UP){
            alive.setYVector(-5 );
        }
        if(button == KeyEvent.VK_RIGHT){
            alive.setXVector(5);
        }
        if(button == KeyEvent.VK_LEFT){
            alive.setXVector(-5);
        }
        if(button == KeyEvent.VK_N && !(gameFinished.equals("no"))){
            startNewGame = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int button = e.getKeyCode();
        Player alive = (playerBlue instanceof AlivePlayer)? playerBlue : playerRed;
        if(button == KeyEvent.VK_DOWN){
            alive.setYVector(0);
        }
        if(button == KeyEvent.VK_UP){
            alive.setYVector(0);
        }
        if(button == KeyEvent.VK_RIGHT){
            alive.setXVector(0);
        }
        if(button == KeyEvent.VK_LEFT){
            alive.setXVector(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(checkIfGameIsOver() && !(gameFinished.equals("no"))){
            repaint();
            if(startNewGame){
                newGame();
            }
        }
        else{
            playerBlue.move(playerRed, ball, boundaries, goalBoundaries);
            playerRed.move(playerBlue, ball, boundaries, goalBoundaries);
            collisionHandler();
            ball.move(null, null, boundaries, goalBoundaries);
            addGoal(ball.checkIfGoal());
            changeScore();
            time += timer.getDelay();
            repaint();
        }
    }
}
