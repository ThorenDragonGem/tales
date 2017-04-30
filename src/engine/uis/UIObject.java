package engine.uis;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public abstract class UIObject
{
	protected float x, y;
	protected int width, height;
	protected boolean hovering = false;
	protected Rectangle bounds;

	public UIObject(float x, float y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.bounds = new Rectangle((int)x, (int)y, width, height);
	}

	public abstract void update();

	public abstract void render(Graphics graphics);

	public abstract void onClick();

	public void onMouseMoved(MouseEvent e)
	{
		if(bounds.contains(e.getX(), e.getY()))
			hovering = true;
		else
			hovering = false;
	}

	public void onMouseRelease(MouseEvent e)
	{
		if(hovering)
			onClick();
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}
}
