package engine.uis;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import engine.physics.Handler;

public class UIManager
{
	private Handler handler;
	private List<UIObject> objects;

	public UIManager(Handler handler)
	{
		this.handler = handler;
		objects = new CopyOnWriteArrayList<UIObject>();
	}

	public void update()
	{
		for(UIObject o : objects)
			o.update();
	}

	public void render(Graphics graphics)
	{
		for(UIObject o : objects)
			o.render(graphics);
	}

	public void onMouseMove(MouseEvent e)
	{
		for(UIObject o : objects)
			o.onMouseMoved(e);
	}

	public void onMouseRelease(MouseEvent e)
	{
		for(UIObject o : objects)
			o.onMouseRelease(e);
	}

	public void addObject(UIObject o)
	{
		objects.add(o);
	}

	public void remove(UIObject o)
	{
		if(objects.contains(o))
			objects.remove(o);
	}

	public Handler getHandler()
	{
		return handler;
	}

	public List<UIObject> getObjects()
	{
		return objects;
	}
}
