package tron;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class ControlsMenu extends TronScreen 
{
	public ControlsMenu() 
	{
		addKeyListener(new TAdapter());
		setFocusable(true);
		setDoubleBuffered(true);
	}

	public void paint(Graphics g) 
	{
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;

		ImageIcon screen = new ImageIcon(this.getClass().getResource("images/ControlScreen.png"));
		g2d.drawImage(screen.getImage(), 0, 0, this);

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	private class TAdapter extends KeyAdapter 
	{
		public void keyPressed(KeyEvent e) 
		{
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_ESCAPE) 
			{
				TronFrame.popScreen();
			}
		}
	}
}
