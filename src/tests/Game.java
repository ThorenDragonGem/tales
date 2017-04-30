package tests;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import engine.assets.Assets;
import engine.display.Display;
import engine.inputs.KeyManager;
import engine.inputs.MouseManager;
import engine.physics.Camera;
import engine.physics.Handler;
import engine.states.GameState;
import engine.states.MenuState;
import engine.states.State;
import engine.utils.Timer;

public class Game implements Runnable
{
	private Thread thread;
	private Display display;
	public String title;
	private int width, height;
	private boolean running;
	private boolean paused = false;

	private BufferStrategy bs;
	private Graphics graphics;
	private double tickTime;
	private double renderTime;
	private double delta;
	private double alpha;

	public State gameState;
	public State menuState;

	private KeyManager keyManager;
	private MouseManager mouseManager;

	private Camera camera;

	private static Handler handler;

	public Game(String title, int width, int height)
	{
		this.title = title;
		this.width = width;
		this.height = height;
		keyManager = new KeyManager();
		mouseManager = new MouseManager();
	}

	private void init()
	{
		display = new Display(title, width, height);
		display.getFrame().addKeyListener(keyManager);
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getFrame().addMouseWheelListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseWheelListener(mouseManager);
		Assets.init();

		handler = new Handler(this);
		camera = new Camera(handler, 0, 0);

		gameState = new GameState(handler);
		menuState = new MenuState(handler);
		// State.setState(menuState);
		State.setState(gameState);
	}

	private void update()
	{
		keyManager.update();
		if(keyManager.exit)
		{
			display.getFrame().dispose();
			stop();
		}
		if(State.getState() != null)
			State.getState().update();
	}

	private void render()
	{
		bs = display.getCanvas().getBufferStrategy();
		if(bs == null)
		{
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		graphics = bs.getDrawGraphics();
		graphics.clearRect(0, 0, width, height);

		if(State.getState() != null)
			State.getState().render(graphics);

		bs.show();
		graphics.dispose();
	}

	int tps = 60;
	int fps = -1;

	@Override
	public void run()
	{
		tickTime = 1000000000.0 / tps;
		renderTime = 1000000000.0 / fps;
		double updatedTime = 0.0;
		double renderedTime = 0.0;
		delta = 0;
		alpha = 0;

		int secondTime = 0;
		boolean second;
		int frames = 0;
		int ticks = 0;
		Timer timer = new Timer();
		init();
		while(running)
		{
			second = false;
			delta = timer.getElapsed() - updatedTime;
			alpha = timer.getElapsed() - renderedTime;

			if(delta >= tickTime)
			{
				update();
				ticks++;

				secondTime++;
				if(secondTime % tps == 0)
				{
					second = true;
					secondTime = 0;
				}
				updatedTime += tickTime;
			}
			else if(alpha >= renderTime)
			{
				render();
				frames++;
				renderedTime += renderTime;
			}
			else
			{
				try
				{
					Thread.sleep(1);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			if(second)
			{
				System.out.println(ticks + " tps, " + frames + " fps");
				frames = 0;
				ticks = 0;
			}
		}
		stop();
	}

	public synchronized void start()
	{
		if(running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop()
	{
		if(!running)
			return;
		running = false;
		try
		{
			System.exit(0);
			thread.join();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public KeyManager getKey()
	{
		return keyManager;
	}

	public MouseManager getMouse()
	{
		return mouseManager;
	}

	public Camera getCamera()
	{
		return camera;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public double getDelta()
	{
		return delta / 1000000000;
	}

	public int getTps()
	{
		return tps;
	}

	public boolean isPaused()
	{
		return paused;
	}

	public void pause()
	{
		this.paused = true;
	}

	public void resume()
	{
		this.paused = false;
	}

	public static Handler getHandler()
	{
		return handler;
	}
}
