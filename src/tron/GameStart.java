package tron;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;


public class GameStart extends TronScreen
{
	
	public int[] selected;
	public GameStart()
	{
		addKeyListener(new TAdapter());
		setFocusable(true);
		setDoubleBuffered(true);
		
	}
	
	public void imageNames()
	{
		
	}

	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		
		ImageIcon background = new ImageIcon(this.getClass().getResource("images/background.png"));
		g2d.drawImage(background.getImage(), 0, 0, this);
		
		drawMenu(g2d);

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public void drawMenu(Graphics2D g2d) 
	{
		
	}
	
	private class TAdapter extends KeyAdapter 
	{
		public void keyPressed(KeyEvent e) 
		{
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_ENTER)
			{
				ArrayList<TronPlayer> temp = new ArrayList<TronPlayer>();
				temp.add(new TronPlayer(750, 550, "Blue", 1));
				temp.add(new TronPlayer(850, 550, "Red", 3));
				temp.add(new TronAI(650, 600, "Gray3", 7));
				temp.add(new TronAI(950, 600, "Gray6", 10));
				temp.add(new TronAI(550, 650, "Gray2", 6));
				temp.add(new TronAI(1050, 650, "Gray7", 11));
				temp.add(new TronAI(450, 700, "Gray1", 5));
				temp.add(new TronAI(1150, 700, "Gray8", 12));
				TronBoard board = new TronBoard(temp);
				TronFrame.pushScreen(board);
			}
			if(key == KeyEvent.VK_LEFT)
			{
				
			}
			if(key == KeyEvent.VK_RIGHT)
			{
				
			}
			if(key == KeyEvent.VK_ESCAPE)
			{
				TronFrame.popScreen();
			}

		}
	}
}
