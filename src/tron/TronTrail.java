package tron;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class TronTrail
{
	private int x, y;
	private Image image;
	private String color;

	public TronTrail(int lx, int ly, String c) 
	{
		String name = c + "Trail.png";
		name = "images/trails/" + name;
		ImageIcon ii = new ImageIcon(this.getClass().getResource(name));
		image = ii.getImage();
		x = lx;
		y = ly;
		color = c;
	}

	public String getColor() 
	{
		return color;
	}

	public int getX() 
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public Rectangle getBounds() 
	{
		return new Rectangle(x, y, 10, 10);
	}

	public Image getImage() 
	{
		return image;
	}
}