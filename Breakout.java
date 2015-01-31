/*
 * File: Breakout.java
 * -------------------
 * Author: SHANKUL JAIN
 * DATE: 11 FEB 2014
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 5;
	

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
    
/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		setup();
		while(!gameOver()){
			moveBall();
			checkCollision();
			pause(3);
		}
		showMessage();
	}
	
	/* setups bricks, paddle and mouselisteners */
	private void setup(){
		addBricks();
		addPaddle();
		n=0;
		bricksRemoved =0;
		addMouseListeners();
		bounceClip = MediaTools.loadAudioClip("bounce.au");
	}
	
	/* adds the brick in the world*/
	private void addBricks(){
		for(int i=0;i<NBRICK_ROWS;i++){
			int y = (i+BRICK_Y_OFFSET/NBRICK_ROWS)*(BRICK_HEIGHT+BRICK_SEP);
			for(int j=0;j<NBRICKS_PER_ROW;j++){
				int x =j*(BRICK_WIDTH+BRICK_SEP);
				brick = new GRect(x,y,BRICK_WIDTH,BRICK_HEIGHT);
				brick.setColor(Color.red);
				brick.setFilled(true);
				add(brick);
			}
		}
	}
	
	/* adds the paddle at its initial position */
	private void addPaddle(){
		paddle = new GRect((getWidth()-PADDLE_WIDTH)/2,HEIGHT-PADDLE_Y_OFFSET,PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
	}
	
	/* checks if mouse is moved if so paddle is moved accordingly */
	public void mouseMoved(MouseEvent e){
		if(e.getX()<=getWidth()-PADDLE_WIDTH/2 && e.getX()>=PADDLE_WIDTH/2){
			paddle.setLocation(e.getX()-PADDLE_WIDTH/2, HEIGHT-PADDLE_Y_OFFSET);
		}
	}
	
	/*check if mouse is clicked and if so a ball is created if it doesn't exist already */
	public void mouseClicked(MouseEvent e){
		if(ball==null && n!= NTURNS){
			message = null;
			ball = new GOval(getWidth()/2-BALL_RADIUS,HEIGHT/2-BALL_RADIUS,2*BALL_RADIUS,2*BALL_RADIUS);
			ball.setFilled(true);
			add(ball);
			setBallSpeed();
		}
	}
	
	private void setBallSpeed(){
		/*dx and dy are initial velocity of ball in x and y direction respectively such that resultant speed is 10*/
		dx = rgen.nextDouble(-.75,.75);
		dy = Math.sqrt(1-Math.pow(dx,2));
	}
	
	/* moves the ball if it exists */
	public void moveBall(){
		if(ball!=null){
			ball.move(dx,dy);
		}
	}
	
	private void checkCollision(){
		checkCollisionWithEdge();
		checkCollisionWithPaddle();
		checkCollisionWithBrick();
		moveOffScreen();
	}
	
	private void checkCollisionWithEdge(){
		if(ball!=null && (ball.getX()<=0 || ball.getX()>=WIDTH-2*BALL_RADIUS)){
			dx = -dx;
		}
		
		else if(ball!=null && ball.getY()<=0){
			dy = -dy;
		}
	}
	
	private void checkCollisionWithPaddle(){
		if(ball!=null){
				GObject coll = getElementAt(ball.getX(),ball.getY()+2*BALL_RADIUS);
			if(coll==paddle){
				bounceClip.play();
				setBallSpeed();
				dy = -dy;
			}
		}
	}
	
	private void checkCollisionWithBrick(){
		if(ball!=null){
			if(dy<0){
			 coll = getElementAt(ball.getX(),ball.getY());	
			}
			else{
			 coll = getElementAt(ball.getX(),ball.getY()+2*BALL_RADIUS);
			}
			if(coll!=paddle && coll!=null){
				bounceClip.play();
				remove(coll);
				bricksRemoved++;
				dy=-dy;
			}
		}
	}
	
	private void moveOffScreen(){
		if(ball!=null&&ball.getY()>=HEIGHT){
			remove(ball);
			ball = null;
			n++;
		}
	}
	
	private boolean gameOver(){
		return n==NTURNS || bricksRemoved==NBRICK_ROWS*NBRICKS_PER_ROW; 
	}
	
	private void showMessage(){
		removeAll();
		if(ball!=null){
			message = new GLabel("You Win!");
			message.setFont("SensSerif-36");
			message.setColor(Color.blue);
			add(message,(APPLICATION_WIDTH-message.getWidth())/2,(APPLICATION_HEIGHT-message.getAscent())/2);
		}
		else{
			message = new GLabel("Game Over");
			message.setFont("SensSerif-36");
			message.setColor(Color.red);
			add(message,(APPLICATION_WIDTH-message.getWidth())/2,(APPLICATION_HEIGHT-message.getAscent())/2);
		}
	}
	
	/* private instance variables */
	private GRect brick;
	private GRect paddle;
	private GOval ball;
	private double dx,dy;
	private int bricksRemoved;
	private int n;
	private GObject coll;
	private GLabel message;
	private AudioClip bounceClip;
	private RandomGenerator rgen = RandomGenerator.getInstance();
}
