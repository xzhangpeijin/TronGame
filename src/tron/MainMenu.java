package tron;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class MainMenu extends TronScreen 
{
	private int selected;
	private String[] imageFile = new String[4];

	public MainMenu() 
	{
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		selected = 1;
		imageNames();
	}

	public void imageNames() 
	{
		imageFile[0] = "images/NewGame1.png";
		imageFile[1] = "images/Options1.png";
		imageFile[2] = "images/Controls1.png";
		imageFile[3] = "images/Exit1.png";
	}

	public void paint(Graphics g) 
	{
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;

		drawMenu(g2d);

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public void drawMenu(Graphics2D g2d) 
	{
		ImageIcon Tron = new ImageIcon(this.getClass().getResource("images/Tron.png"));
		ImageIcon Background = new ImageIcon(this.getClass().getResource("images/Background.png"));

		switch (selected) 
		{
			case 1:		imageFile[0] = "images/NewGame.png"; break;
			case 2:		imageFile[1] = "images/Options.png"; break;
			case 3:		imageFile[2] = "images/Controls.png"; break;
			case 4: 	imageFile[3] = "images/Exit.png"; break;
		}

		ImageIcon[] icons = new ImageIcon[4];

		for (int x = 0; x < 4; x++)
			icons[x] = new ImageIcon(this.getClass().getResource(imageFile[x]));

		g2d.drawImage(Background.getImage(), 0, 0, this);
		g2d.drawImage(Tron.getImage(), 500, 180, this);
		for (int x = 0; x < 4; x++)
			g2d.drawImage(icons[x].getImage(), 630, 440 + 80 * x, this);
	}

	private class TAdapter extends KeyAdapter 
	{
		public void keyPressed(KeyEvent e) 
		{
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_DOWN)
				selected = Math.min(selected + 1, 4);
			if (key == KeyEvent.VK_UP)
				selected = Math.max(selected - 1, 1);
			if (key == KeyEvent.VK_ENTER) 
			{
				if (selected == 1) 
				{
					TronFrame.pushScreen(new GameStart());
				} 
				else if (selected == 2) 
				{
					TronFrame.pushScreen(new OptionsMenu(false));
				} 
				else if (selected == 3) 
				{
					TronFrame.pushScreen(new ControlsMenu());
				} 
				else if (selected == 4) 
				{
					System.exit(1);
				}
			} 
			if(key == KeyEvent.VK_ESCAPE)
			{
				System.exit(1);
			}
			else 
			{
				imageNames();
				repaint();
			}
		}
	}
}
