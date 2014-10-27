package tron;

import java.util.Stack;

import javax.swing.JFrame;

public class TronFrame extends JFrame implements Runnable 
{
	public static Stack<TronScreen> screens = new Stack<TronScreen>();
	public Thread counter;
	public static boolean push, update;
	public static TronScreen top;
	public static int numpop = 0;

	public TronFrame() 
	{
		push = false;
		MainMenu menu = new MainMenu();
		add(menu);
		screens.push(menu);
		top = menu;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1600, 920);
		setLocationRelativeTo(null);
		setTitle("TRON");
		setResizable(false);
		setVisible(true);
		counter = new Thread(this);
		counter.start();
	}

	public void Update() 
	{
		if (update) 
		{
			if (push) 
			{
				add(screens.peek());
				top = screens.peek();
				setVisible(true);
			} 
			else 
			{
				remove(screens.pop());
				top = screens.peek();
				top.repaint();
			}
			top.requestFocus();
			if(numpop == 0)
				update = false;
			else
				numpop--;
		}
	}

	static void pushScreen(TronScreen screen) 
	{
		screens.push(screen);
		push = true;
		update = true;
	}

	static void popScreen() 
	{
		push = false;
		update = true;
	}

	static void popAll() 
	{
		if(screens.size() > 2)
			numpop = screens.size() - 2;
		popScreen();
	}

	public void run() 
	{
		long beforeTime, timeDiff, sleep;

		while (true) 
		{
			beforeTime = System.currentTimeMillis();

			Update();

			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = 200 - timeDiff;

			if (sleep < 0)
				sleep = 1;
			try 
			{
				Thread.sleep(sleep);
			} 
			catch (InterruptedException e) 
			{
				System.out.println("interrupted");
			}
		}
	}

	public static void main(String[] args) 
	{
		new TronFrame();
	}
}