package engine.items;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import engine.entities.Entity;
import engine.physics.Handler;

public class ItemManager
{
	private Handler handler;
	private List<Item> items;

	public ItemManager(Handler handler)
	{
		this.handler = handler;
		this.items = new ArrayList<Item>();
	}

	public void update()
	{
		Iterator<Item> it = items.iterator();
		while(it.hasNext())
		{
			Item i = it.next();
			i.update();
			if(i.isPickedUp())
				it.remove();
		}
	}

	public void render(Graphics graphics)
	{
		for(Item item : items)
			item.render(graphics);
	}

	public void addItem(Item i)
	{
		i.setHandler(handler);
		items.add(i);
	}

	public void addItem(Item type, Entity dropper, int count)
	{
		for(int j = 0; j < count; j++)
			addItem(type.createNew((int)dropper.getBody().getPosition().x, (int)(dropper.getBody().getPosition().y + dropper.getBody().getHeight() / 2)));
	}

	public Handler getHandler()
	{
		return handler;
	}

	public void setHandler(Handler handler)
	{
		this.handler = handler;
	}

	public List<Item> getItems()
	{
		return items;
	}
}
