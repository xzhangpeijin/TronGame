package tron;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class GameMenu extends TronScreen
{
	private int selected;
	private String[] imageFile = new String[4];
	
	private ArrayList<ArrayList<TronTrail>> trails;
	private ArrayList<TronPlayer> players;

	public GameMenu(ArrayList<ArrayList<TronTrail>> Ttrails, ArrayList<TronPlayer> Tplayers) 
	{
		players = Tplayers;
		trails = Ttrails;
		
		addKeyListener(new TAdapter());
		setFocusable(true);
		setDoubleBuffered(true);
		selected = 1;
		imageNames();
	}

	public void imageNames() 
	{
		imageFile[0] = "images/ResumeGame.png";
		imageFile[1] = "images/Options1.png";
		imageFile[2] = "images/Controls1.png";
		imageFile[3] = "images/ExitToMenu.png";
	}
	

	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		
		ImageIcon opacity = new ImageIcon(this.getClass().getResource("images/TransparentBlack.png"));
		ImageIcon outline = new ImageIcon(this.getClass().getResource("images/GameMenu.png"));

		drawGame(g2d);
		
		g2d.drawImage(opacity.getImage(), 0, 0, this);
		g2d.drawImage(outline.getImage(), 0, 0, this);
		
		drawMenu(g2d);

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public void drawMenu(Graphics2D g2d) 
	{
		ImageIcon Tron = new ImageIcon(this.getClass().getResource("images/GamePaused.png"));

		switch (selected) 
		{
			case 1:		imageFile[0] = "images/ResumeGame1.png"; break;
			case 2:		imageFile[1] = "images/Options.png"; break;
			case 3:		imageFile[2] = "images/Controls.png"; break;
			case 4: 	imageFile[3] = "images/ExitToMenu1.png"; break;
		}

		ImageIcon[] icons = new ImageIcon[4];

		for (int x = 0; x < 4; x++)
			icons[x] = new ImageIcon(this.getClass().getResource(imageFile[x]));

		g2d.drawImage(Tron.getImage(), 400, 180, this);
		g2d.drawImage(icons[0].getImage(), 560, 440, this);
		g2d.drawImage(icons[1].getImage(), 630, 520, this);
		g2d.drawImage(icons[2].getImage(), 630, 600, this);
		g2d.drawImage(icons[3].getImage(), 560, 680, this);
	}
	
	public void drawGame(Graphics2D g2d)
	{
		ImageIcon background = new ImageIcon(this.getClass().getResource("images/background.png"));
		g2d.drawImage(background.getImage(), 0, 0, this);
		
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
		
		for (int x = 0; x < Math.min(players.size(), 4); x++) 
		{
			TronPlayer templayer = players.get(x);
			if (templayer.visible) 
			{
				int cooldown = templayer.getCooldown();
				TronTrail temptrail = new TronTrail(0, 0, players.get(x)
						.getColor());
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
		@SuppressWarnings("deprecation")
		public void keyPressed(KeyEvent e) 
		{
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_DOWN)
				selected = Math.min(selected + 1, 4);
			if (key == KeyEvent.VK_UP)
				selected = Math.max(selected - 1, 1);
			if (key == KeyEvent.VK_ESCAPE)
			{
				TronFrame.popScreen();
				TronBoard.animator.resume();
			}
			if (key == KeyEvent.VK_ENTER) 
			{
				if (selected == 1) 
				{
					TronFrame.popScreen();
					TronBoard.animator.resume();
				} 
				else if (selected == 2) 
				{
					TronFrame.pushScreen(new OptionsMenu(true));
				} 
				else if (selected == 3) 
				{
					TronFrame.pushScreen(new ControlsMenu());
				} 
				else if (selected == 4) 
				{
					TronFrame.popAll();
				}
			} 
			imageNames();
			repaint();
		}
	}
}
