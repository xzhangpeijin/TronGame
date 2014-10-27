package tron;

import java.io.*;
import javazoom.jl.player.Player;


public class TronSound 
{
    private String filename;
    private Player player; 

    public TronSound(String filename, boolean loop) 
    {
        this.filename = filename;
        play();
    }

    public void close() 
    { 
    	if (player != null) 
    		player.close(); 
    }

    public void play() 
    {
        try 
        {
            FileInputStream fis = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
        }
        catch (Exception e) 
        {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }

        new Thread() 
        {
            public void run() 
            {
                try 
                { 
                	player.play(); 
                }
                catch (Exception e) 
                { 
                	System.out.println(e); 
                }
            }
        }.start();
    }
}
