package snake_game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 1300;
	static final int SCREEN_HEIGHT = 750;
	static final int UNIT_SIZE = 30;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
	static final int DELAY = 100
			;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	public GamePanel() {
		
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		
	}
	
	public void startGame() {
		
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		if(running) {
			//Just to visualize the unit size in the game panel.
//			for(int i=0;i<SCREEN_HEIGHT;i++) {
//				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
//				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
//			}
			
			//apple
			g.setColor(Color.RED);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			//snake body part
			for(int i=0;i<bodyParts;i++) {
				if(i==0) {
					g.setColor(Color.BLUE);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else if(i%2==0){
					g.setColor(new Color(255,165,0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else if(i%2!=0) {
					g.setColor(new Color(255,234,0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.WHITE);
			g.setFont(new Font("Calbiri", Font.BOLD,35));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+ applesEaten, 
					(SCREEN_WIDTH - metrics.stringWidth("Score: "+ applesEaten))/2,
					g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}
	
	public void newApple() {
		
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	
	public void move() {
		
		for(int i=bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
			case 'U':
				y[0] = y[0] - UNIT_SIZE;
				break;
				
			case 'D':
				y[0] = y[0] + UNIT_SIZE;
				break;
				
			case 'L':
				x[0] = x[0] - UNIT_SIZE;
				break;
				
			case 'R':
				x[0] = x[0] + UNIT_SIZE;
				break;
			
		}
	}
	
	public void checkAppleEaten() {
		
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	
	public void checkCollisions() {
		
		//checks if snake touches it's own body
		for(int i=bodyParts;i>0;i--) {
			if((x[0] == x[i]) && y[0] == y[i]) {
				running=false;
			}
		}
		
		//check if snake's head touches the left border
		if(x[0] < 0)
			running=false;
		
		//check if snake's head touches the right border
		if(x[0] > SCREEN_WIDTH)
			running=false;
		
		//check if snake's head touches top border
		if(y[0] < 0)
			running=false;
		
		//check if snake's head touches bottom border
		if(y[0] > SCREEN_HEIGHT)
			running=false;
		
		if(!running)
			timer.stop();
		
		
	}
	
	public void gameOver(Graphics g) {
		
		//Game Over text
		g.setColor(Color.RED);
		g.setFont(new Font("Calbiri", Font.BOLD,95));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		//printing the msg in the middle:
		g.drawString("Game OVER", 
				(SCREEN_WIDTH - metrics1.stringWidth("GAME OVER"))/2,
				SCREEN_HEIGHT/2);
		
		
		//Printing the score
		g.setColor(Color.RED);
		g.setFont(new Font("Calbiri", Font.BOLD,35));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: "+ applesEaten, 
				(SCREEN_WIDTH - metrics2.stringWidth("Score: "+ applesEaten))/2,
				g.getFont().getSize());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if(running) {
			move();
			checkAppleEaten();
			checkCollisions();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT:
				if(direction != 'R')
					direction ='L';
				
				break;
				
			case KeyEvent.VK_RIGHT:
				if(direction != 'L')
					direction ='R';
				
				break;
				
			case KeyEvent.VK_UP:
				if(direction != 'D')
					direction ='U';
				
				break;
				
			case KeyEvent.VK_DOWN:
				if(direction != 'U')
					direction ='D';
				
				break;
				
			}
		}
	}

}
