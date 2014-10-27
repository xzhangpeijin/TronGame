package tron;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class TronPlayer 
{
	public int dx, dy;
	public int xloc, yloc;
	public Image image;
	public int speed;
	public boolean toMove;
	public int cooldown;
	public String color;
	public int moves;

	public boolean visible = true;

	public int up, down, left, right, boost, num;
	
	private boolean update = true;
	private int targets = 0;

	public TronPlayer(int lx, int ly, String c, int number) 
	{
		String name = c + "Player.png";
		name = "images/players/" + name;
		ImageIcon ii = new ImageIcon(this.getClass().getResource(name));
		image = ii.getImage();
		xloc = lx;
		yloc = ly;
		speed = 5;
		dy = -1 * speed;
		toMove = true;
		cooldown = 0;
		color = c;
		num = number;
		setDirections(number);
	}
	
	public TronPlayer newPlayer()
	{
		return new TronPlayer(xloc, yloc, color, num);
	}
	
	public void Target()
	{
		targets++;
	}
	
	public int getTargets()
	{
		return targets;
	}

	private void setDirections(int number) 
	{
		if (number == 1) 
		{
			left = KeyEvent.VK_A;
			right = KeyEvent.VK_D;
			up = KeyEvent.VK_W;
			down = KeyEvent.VK_S;
			boost = KeyEvent.VK_E;
		}
		else if (number == 2) 
		{
			left = KeyEvent.VK_J;
			right = KeyEvent.VK_L;
			up = KeyEvent.VK_I;
			down = KeyEvent.VK_K;
			boost = KeyEvent.VK_O;
		} 
		else if (number == 3) 
		{
			left = KeyEvent.VK_LEFT;
			right = KeyEvent.VK_RIGHT;
			up = KeyEvent.VK_UP;
			down = KeyEvent.VK_DOWN;
			boost = KeyEvent.VK_NUMPAD0;
		} 
		else if (number == 4) 
		{
			left = KeyEvent.VK_NUMPAD4;
			right = KeyEvent.VK_NUMPAD6;
			up = KeyEvent.VK_NUMPAD8;
			down = KeyEvent.VK_NUMPAD5;
			boost = KeyEvent.VK_NUMPAD7;
		}
		else
		{
			left = -1;
			right = -2;
			up = -3;
			down = -4;
			boost = -5;
		}
	}

	public void run() 
	{
		if (toMove)
			move();
		else
			toMove = true;
		moves = TronBoard.moves;
		update = true;
	}

	public void move() 
	{
		xloc += dx;
		yloc += dy;
		if (dx != 0 && Math.abs(dx) != speed)
			dx = dx / Math.abs(dx) * speed;
		if (dy != 0 && Math.abs(dy) != speed)
			dy = dy / Math.abs(dy) * speed;
		if (cooldown == 900)
			speed = speed / 2;
		if (cooldown != 0)
			cooldown--;
	}

	public TronPlayer clone() 
	{
		return new TronPlayer(xloc, yloc, color, num);
	}

	public int getX() 
	{
		return xloc;
	}

	public int getY() 
	{
		return yloc;
	}

	public int getCooldown() 
	{
		return cooldown;
	}

	public String getColor()
	{
		return color;
	}

	public int getNumber()
	{
		return num;
	}

	public Image getImage() 
	{
		return image;
	}

	public void setVisible(boolean a)
	{
		visible = a;
	}

	public Rectangle getBounds()
	{
		return new Rectangle(xloc, yloc, 10, 10);
	}

	public ArrayList<TronTrail> getTrails()
	{
		ArrayList<TronTrail> trails = new ArrayList<TronTrail>();
		if (speed >= 5)
			trails.add(new TronTrail(xloc, yloc, color));
		if (speed >= 10 && toMove)
			trails.add(new TronTrail(xloc + dx, yloc + dy, color));
		return trails;
	}

	public void keyPressed(int key)
	{
		toMove = true;
		if(moves != TronBoard.moves && update)
		{
			if (key == left)
			{
				if (yloc % 10 != 0)
				{
					yloc += 5 * dy / Math.abs(dy);
					toMove = false;
				}	
				if (dx != speed) 
				{
					dy = 0;
					dx = -1 * speed;
				}
			} 
			else if (key == right)
			{
				if (yloc % 10 != 0) 
				{
					yloc += 5 * dy / Math.abs(dy);
					toMove = false;
				}	
				if (dx != -1 * speed)
				{
					dy = 0;
					dx = speed;
				}	
			}	 	
			else if (key == up) 
			{
				if (xloc % 10 != 0)
				{
					xloc += 5 * dx / Math.abs(dx);
					toMove = false;
				}	
				if (dy != speed) 
				{
					dy = -1 * speed;
					dx = 0;
				}	
			}	
			else if (key == down) 
			{
				if (xloc % 10 != 0)
				{
					xloc += 5 * dx / Math.abs(dx);
					toMove = false;
				}	
				if (dy != -1 * speed)
				{
					dy = speed;
					dx = 0;
				}	
			}	
			if (key == boost && cooldown == 0)
			{
				speed = 2 * speed;
				cooldown = 990;
			}
			update = false;
		}
	}
}