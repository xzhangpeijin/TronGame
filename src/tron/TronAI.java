package tron;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TronAI extends TronPlayer
{
	private ArrayList<ArrayList<TronTrail>> trails;
	private ArrayList<TronPlayer> players = new ArrayList<TronPlayer>();
	private Rectangle field = new Rectangle(29, 129, 1545, 745);
	private TronPlayer target;

	public TronAI(int lx, int ly, String c, int number)
	{
		super(lx, ly, c, number);
		setTarget();
	}
	
	public TronAI newPlayer()
	{
		return new TronAI(xloc, yloc, color, num);
	}
	
	public void setTarget()
	{
		int targets = 10;
		for(int x = 0; x < TronBoard.players.size(); x++)
			targets = Math.min(TronBoard.players.get(x).getTargets(), targets);
		for(int x = 0; x < TronBoard.players.size(); x++)
		{
			if(targets == TronBoard.players.get(x).getTargets() && TronBoard.players.get(x).getColor() != color && !(TronBoard.players.get(x) instanceof TronAI))
			{
				target = TronBoard.players.get(x);
				target.Target();
				x = TronBoard.players.size();
			}
		}
	}
	
	public void run()
	{
		trails = TronBoard.trails;
		players.clear();
		for(int x = 0; x < TronBoard.players.size(); x++)
			if(TronBoard.players.get(x).getColor() != color)
				players.add(TronBoard.players.get(x));
		makeDecision();
		Rectangle bounds = new Rectangle(xloc + dx, yloc + dy, 10, 10);
		if(checkIntersect(bounds))
		{
			if(dx > 0)
			{
				keyPressed(up);
				bounds = new Rectangle(xloc + dx, yloc + dy, 10, 10);
				if(checkIntersect(bounds))
				{
					keyPressed(right);
					keyPressed(down);
				}
			}
			else if(dx < 0)
			{
				keyPressed(down);
				bounds = new Rectangle(xloc + dx, yloc + dy, 10, 10);
				if(checkIntersect(bounds))
				{
					keyPressed(left);
					keyPressed(up);
				}
			}
			else if(dy > 0)
			{
				keyPressed(left);
				bounds = new Rectangle(xloc + dx, yloc + dy, 10, 10);
				if(checkIntersect(bounds))
				{
					keyPressed(down);
					keyPressed(right);
				}
			}
			else if(dy < 0)
			{
				keyPressed(right);
				bounds = new Rectangle(xloc + dx, yloc + dy, 10, 10);
				if(checkIntersect(bounds))
				{
					keyPressed(up);
					keyPressed(left);
				}
			}
		}
		super.run();
	}
	
	public void makeDecision()
	{
		if(target == null || !target.visible)
		{
			setTarget();
		}
		else
		{
			TronPlayer a = target;
			int distance = (int) Math.pow((Math.pow(Math.abs(xloc - a.xloc), 2) + Math.pow(Math.abs(yloc - a.yloc), 2)), 0.5);
			if(Math.abs(xloc - a.xloc) > Math.abs(yloc - a.yloc) && distance > 200)
			{
				if(xloc - a.xloc > 0)
					keyPressed(left);
				else if(xloc - a.xloc < 0)
					keyPressed(right);
			}
			else if(distance > 200)
			{
				if(yloc - a.yloc > 0)
					keyPressed(up);
				else if(yloc - a.yloc < 0)
					keyPressed(down);
			}
			else
				keyPressed(boost);
		}
		for(int x = 0; x < players.size(); x++)
		{
			TronPlayer a = players.get(x);
			int distance = (int) Math.pow((Math.pow(Math.abs(xloc - a.xloc), 2) + Math.pow(Math.abs(yloc - a.yloc), 2)), 0.5);
			if(a.speed > speed && distance < 200)
				keyPressed(boost);
		}
	}
	
	public void keyPressed(int key)
	{
		toMove = true;
		if(moves != TronBoard.moves)
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
		}
	}
	
	public boolean checkIntersect(Rectangle bounds)
	{
		if (!field.intersects(bounds)) 
			return true;
					
		for (int a = 0; a < trails.size(); a++) 
		{
			ArrayList<TronTrail> singletrail = trails.get(a);
			for (int z = 0; z < singletrail.size() - 2; z++) 
				if (bounds.intersects(singletrail.get(z).getBounds())) 
					return true;
		}
		
		for (int a = 0; a < players.size(); a++) 
			if (bounds.intersects(players.get(a).getBounds()) && players.get(a).visible) 
				return true;
		
		return false;
	}
}
