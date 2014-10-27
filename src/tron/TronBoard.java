package tron;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.ImageIcon;

import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.SourceDataLine;

public class TronBoard extends TronScreen implements Runnable 
{
	public static int gamespeed = 30;

	public static Thread animator;

	private boolean gameover;

	private Rectangle field = new Rectangle(29, 129, 1545, 745);

	public static ArrayList<ArrayList<TronTrail>> trails = new ArrayList<ArrayList<TronTrail>>();
	public static ArrayList<TronPlayer> players = new ArrayList<TronPlayer>();
	private ArrayList<TronPlayer> playersarchive = new ArrayList<TronPlayer>();
	private ArrayList<String> dead = new ArrayList<String>();
	private LinkedList<Integer> keylist = new LinkedList<Integer>();

	public static int moves = 0;

	public TronBoard(ArrayList<TronPlayer> player) 
	{
		players.clear();
		trails.clear();
		
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);

		players = player;
		for (int x = 0; x < players.size(); x++)
		{
			playersarchive.add(players.get(x).newPlayer());
		}
		gameover = false;
		moves = 0;

		animator = new Thread(this);
		animator.start();

		for (int x = 0; x < players.size(); x++) 
			trails.add(new ArrayList<TronTrail>());
	}

	public void run() 
	{
		long beforeTime, timeDiff, sleep;
      
		while (true) 
		{
			beforeTime = System.currentTimeMillis();

			action();
			repaint();

			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = gamespeed - timeDiff;

			if (sleep < 0)
				sleep = 0;
			try 
			{
				Thread.sleep(sleep);
			} 
			catch (InterruptedException e) 
			{
				System.out.println("interrupted");
			}
			while(keylist.size() > 0)
			{
				int key = keylist.remove();
				if (!gameover) 
					for (int x = 0; x < players.size(); x++)
						players.get(x).keyPressed(key);
			}
		}
	}

	public void paint(Graphics g) 
	{
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;

		ImageIcon Background = new ImageIcon(this.getClass().getResource("images/Background.png"));
		g2d.drawImage(Background.getImage(), 0, 0, this);

		drawGame(g2d);

		if (gameover) 
		{
			ImageIcon gameovericon = new ImageIcon(this.getClass().getResource("images/Gameover.png"));
			g2d.drawImage(gameovericon.getImage(), 440, 400, this);
		}

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public void drawGame(Graphics2D g2d) 
	{
		for (int x = 0; x < trails.size(); x++) 
		{
			ArrayList<TronTrail> singletrail = trails.get(x);
			for (int y = 0; y < singletrail.size(); y++) 
			{
				TronTrail temptrail = singletrail.get(y);
				g2d.drawImage(temptrail.getImage(), temptrail.getX(),
						temptrail.getY(), this);
			}
		}

		for (int x = 0; x < players.size(); x++) 
		{
			TronPlayer temp = players.get(x);
			if (players.get(x).visible)
				g2d.drawImage(temp.getImage(), temp.getX(), temp.getY(), this);
		}
		
		if (moves < 2000 / gamespeed) 
		{
			ImageIcon[] start = new ImageIcon[4];
			for (int x = 0; x < 4; x++) 
			{
				String filename = "images/" + Integer.toString(3 - x) + ".png";
				start[x] = new ImageIcon(this.getClass().getResource(filename));
			}
			if (moves < 600 / gamespeed)
				g2d.drawImage(start[0].getImage(), 700, 360, this);
			else if (moves < 1200 / gamespeed)
				g2d.drawImage(start[1].getImage(), 700, 360, this);
			else if (moves < 1800 / gamespeed)
				g2d.drawImage(start[2].getImage(), 700, 360, this);
			else
				g2d.drawImage(start[3].getImage(), 700, 360, this);
		}

		for (int x = 0; x < Math.min(players.size(), 4); x++) 
		{
			TronPlayer templayer = players.get(x);
			if (templayer.visible) 
			{
				int cooldown = templayer.getCooldown();
				TronTrail temptrail = new TronTrail(0, 0, players.get(x).getColor());
				if (cooldown > 900) 
				{
					for (int y = 0; y < cooldown - 900; y++)
						g2d.drawImage(temptrail.getImage(), 190 + 2 * y, 20 + 22 * x, this);
				} 
				else 
				{
					int temp = (900 - cooldown) / 5;
					for (int y = 0; y < temp; y++)
						g2d.drawImage(temptrail.getImage(), 190 + y, 20 + 22 * x, this);
				}
			}
		}
		
		ImageIcon black = new ImageIcon(this.getClass().getResource("images/Black.png"));
		ImageIcon boost = new ImageIcon(this.getClass().getResource("images/Boost.png"));
		
		for (int x = 0; x < 4; x++) 
		{
			g2d.drawImage(black.getImage(), 190, 20 + 22 * x, this);
			g2d.drawImage(boost.getImage(), 200, 20 + 22 * x, this);
		}
		
		moves++;
	}

	public void action() 
	{
		int alive = 0;
		int AI = 0;
		if (moves > 2000 / gamespeed) 
		{
			checkCollisions();
			for (int x = 0; x < players.size(); x++) 
			{
				TronPlayer temp = players.get(x);
				if (temp.visible && !gameover) 
				{
					trails.get(x).addAll(temp.getTrails());
					temp.run();
					alive++;
					if(players.get(x) instanceof TronAI)
						AI++;
				}
			}
			if (alive <= 1)
				gameover = true;
			if (alive == AI)
				gameover = true;
			if (dead.size() > 0) 
			{
				for (int x = 0; x < dead.size(); x++) 
				{
					String death = dead.get(x);
					for (int y = 0; y < trails.size(); y++) 
					{
						if (trails.get(y).size() > 0) 
						{
							if (trails.get(y).get(0).getColor().equals(death)) 
							{
								trails.get(y).remove(0);
								if (trails.get(y).size() > 0)
									trails.get(y).remove(0);
								if (trails.get(y).size() > 0)
									trails.get(y).remove(0);
								if (trails.get(y).size() > 0)
									trails.get(y).remove(0);
								if (trails.get(y).size() > 0)
									trails.get(y).remove(0);
							}
						}
					}
				}
			}
		}
	}

	public void restartGame() 
	{
		trails.clear();
		players.clear();
		dead.clear();

		gameover = false;
		moves = 0;

		for (int x = 0; x < playersarchive.size(); x++)
			players.add(playersarchive.get(x).newPlayer());
		for (int x = 0; x < players.size(); x++) 
		{
			trails.add(new ArrayList<TronTrail>());
			players.get(x).setVisible(true);
		}
	}

	private void checkCollisions() 
	{
		for (int x = 0; x < players.size(); x++) 
		{
			TronPlayer temp = players.get(x);
			if (temp.visible) 
			{
				if (!field.intersects(temp.getBounds())) 
				{
					players.get(x).setVisible(false);
					dead.add(temp.getColor());
				}
				for (int y = 0; y < trails.size(); y++) 
				{
					ArrayList<TronTrail> singletrail = trails.get(y);
					for (int z = 0; z < singletrail.size() - 2; z++) 
					{
						if (temp.getBounds().intersects(singletrail.get(z).getBounds())) 
						{
							players.get(x).setVisible(false);
							dead.add(temp.getColor());
							z = singletrail.size();
						}
					}
				}
				for (int y = 0; y < x; y++) 
				{
					if (temp.getBounds().intersects(players.get(y).getBounds()) && players.get(y).visible) 
					{
						dead.add(temp.getColor());
						dead.add(players.get(y).getColor());
						players.get(x).setVisible(false);
						players.get(y).setVisible(false);
					}
				}
			}
		}
	}

	private class TAdapter extends KeyAdapter 
	{
		@SuppressWarnings("deprecation")
		public void keyPressed(KeyEvent e) 
		{
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_ENTER && gameover)
				restartGame();
			if (key == KeyEvent.VK_ESCAPE && gameover)
			{
				TronFrame.popAll();
			}
			else if(key == KeyEvent.VK_ESCAPE)
			{
				animator.suspend();
				TronFrame.pushScreen(new GameMenu(trails, players));
			}
			else
				keylist.add(key);
		}
	}
}