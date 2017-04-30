package engine.physics;

import engine.inputs.KeyManager;
import engine.inputs.MouseManager;
import tests.Game;

public class Handler
{
	private Game instance;
	private World world;

	public Handler(Game instance)
	{
		this.instance = instance;
	}

	public int getWidth()
	{
		return instance.getWidth();
	}

	public int getHeight()
	{
		return instance.getHeight();
	}

	public KeyManager getKey()
	{
		return instance.getKey();
	}

	public MouseManager getMouse()
	{
		return instance.getMouse();
	}

	public Camera getCamera()
	{
		return instance.getCamera();
	}

	public Game getGame()
	{
		return instance;
	}

	public void setGame(Game instance)
	{
		this.instance = instance;
	}

	public World getWorld()
	{
		return world;
	}

	public void setWorld(World world)
	{
		this.world = world;
	}
}
