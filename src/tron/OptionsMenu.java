package tron;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class OptionsMenu extends TronScreen
{
	private boolean drawGame;
	private int row;
	private int collumn;
	
	private ArrayList<ArrayList<TronTrail>> trails;
	private ArrayList<TronPlayer> players;
	
	public OptionsMenu(boolean game)
	{
		drawGame = game;
		addKeyListener(new TAdapter());
		setFocusable(true);
		setDoubleBuffered(true);
		
		trails = TronBoard.trails;
		players = TronBoard.players;
	}
	
	public void imageNames()
	{
		
	}

	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		
		ImageIcon opacity = new ImageIcon(this.getClass().getResource("images/TransparentBlack.png"));
		ImageIcon background = new ImageIcon(this.getClass().getResource("images/background.png"));
		g2d.drawImage(background.getImage(), 0, 0, this);

		if(drawGame)
		{
			drawGame(g2d);
			g2d.drawImage(opacity.getImage(), 0, 0, this);
		}
		
		drawMenu(g2d);

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public void drawMenu(Graphics2D g2d) 
	{
		
	}
	
	public void drawGame(Graphics2D g2d)
	{
		for (int x = 0; x < trails.size(); x++) 
		{
			ArrayList<TronTrail> singletrail = trails.get(x);
			for (int y = 0; y < singletrail.size(); y++) 
			{
				TronTrail temptrail = singletrail.get(y);
				g2d.drawImage(temptrail.getImage(), temptrail.getX(), temptrail.getY(), this);
			}
		}

		for (int x = 0; x < players.size(); x++) 
		{
			TronPlayer temp = players.get(x);
			if (players.get(x).visible)
				g2d.drawImage(temp.getImage(), temp.getX(), temp.getY(), this);
		}
		
		for (int x = 0; x < players.size(); x++) 
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
	}
	
	private class TAdapter extends KeyAdapter 
	{
		public void keyPressed(KeyEvent e) 
		{
			int key = e.getKeyCode();
			
			if(key == KeyEvent.VK_ESCAPE)
			{
				TronFrame.popScreen();
			}

		}
	}
}
