package engine.inputs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import engine.assets.Assets;
import engine.items.Item;
import engine.physics.Handler;
import engine.utils.Mathf;
import engine.utils.Text;

public class Inventory
{
	private Handler handler;
	private boolean active = false;
	private List<Item> inventoryItems;
	private List<Item> toRemove;
	private int invX;
	private int invY;
	private int invWidth;
	private int invHeight;
	private int invListCenterX;
	private int invListCenterY;
	private int invListSpacing;
	private int invImageX, invImageY, invImageWidth, invImageHeight;
	private int invCountX, invCountY;

	private int selectedItem = 0;

	public Inventory(Handler handler)
	{
		this.handler = handler;
		inventoryItems = new ArrayList<Item>();
		toRemove = new ArrayList<Item>();
		invWidth = Assets.inventory.getWidth();
		invHeight = Assets.inventory.getHeight();
		invX = handler.getGame().getWidth() / 2 - invWidth / 2;
		invY = handler.getGame().getHeight() / 2 - invHeight / 2;
		invListCenterX = invX + 171;
		invListCenterY = invY + invHeight / 2 + 5;
		invListSpacing = 30;
		invImageX = invX + 392;
		invImageWidth = 64;
		invImageHeight = 64;
		invImageY = invY + invImageHeight / 2;
		invCountX = invX + 421;
		invCountY = invY + 124;
	}

	public void update()
	{
		for(Item item : toRemove)
			if(inventoryItems.contains(item))
				inventoryItems.remove(item);
		toRemove.clear();
		if(handler.getKey().isKeyPressed(KeyEvent.VK_I))
		{
			active = !active;
			if(handler.getGame().isPaused())
				handler.getGame().resume();
			else
				handler.getGame().pause();
		}
		if(!active)
			return;
		
		// bugged
		if(handler.getKey().isKeyPressed(KeyEvent.VK_ENTER) && selectedItem < inventoryItems.size())
			drop(inventoryItems.get(selectedItem));
		if(handler.getMouse().getScrollY() < 0 || handler.getKey().isKeyPressed(KeyEvent.VK_UP))// .getKey().isKeyPressed(KeyEvent.VK_U))
			selectedItem--;
		if(handler.getMouse().getScrollY() > 0 || handler.getKey().isKeyPressed(KeyEvent.VK_DOWN))// .getKey().isKeyPressed(KeyEvent.VK_J))
			selectedItem++;

		if(selectedItem < 0)
			selectedItem = inventoryItems.size() - 1;
		else if(selectedItem >= inventoryItems.size())
			selectedItem = 0;
	}

	public void render(Graphics graphics)
	{
		if(!active)
			return;
		graphics.drawImage(Assets.inventory, invX, invY, invWidth, invHeight, null);

		int len = inventoryItems.size();
		if(len == 0)
			return;
		for(int i = -5; i < 6; i++)
		{
			if(selectedItem + i < 0 || selectedItem + i >= len)
				continue;
			if(i == 0)
			{
				Text.drawString(graphics, "> " + inventoryItems.get(selectedItem + i).getName() + " <", invListCenterX, invListCenterY + i * invListSpacing, true, Color.YELLOW, Assets.font28);
			}
			else
			{
				Text.drawString(graphics, inventoryItems.get(selectedItem + i).getName(), invListCenterX, invListCenterY + i * invListSpacing, true, Color.white, Assets.font28);
			}
		}
		Item item = inventoryItems.get(selectedItem);
		graphics.drawImage(item.getTexture(), invImageX, invImageY, invImageWidth, invImageHeight, null);
		Text.drawString(graphics, Text.getThousandsStringd(item.getCount()), invCountX, invCountY, true, Color.white, Assets.font18);
	}

	public void add(Item item)
	{
		for(Item i : inventoryItems)
		{
			// TODO
			// no max count
			if(i.getId() == item.getId())// && i.hasFreeSlots())
			{
				i.setCount(i.getCount() + item.getCount());
				return;
			}
		}
		// if(item.getCount() > item.getMaxCount())
		// {
		// while(item.getCount() > item.getMaxCount())
		// {
		// inventoryItems.add(item.createNew(item.getMaxCount()));
		// item.setCount(item.getCount() - item.getMaxCount());
		// }
		// }
		// else
			inventoryItems.add(item);
	}

	public void remove(Item item)
	{
		toRemove.add(item);
	}

	public List<Item> getItems()
	{
		return inventoryItems;
	}

	public Handler getHandler()
	{
		return handler;
	}

	public void setHandler(Handler handler)
	{
		this.handler = handler;
	}

	public boolean isActive()
	{
		return active;
	}

	public void drop(Item item)
	{
		handler.getWorld().getItemManager().addItem(item.createNew((int)handler.getWorld().getEntityManager().getPlayer().getBody().getPosition().x + (int)Mathf.random(-100, 100), (int)(handler.getWorld().getEntityManager().getPlayer().getBody().getPosition().y + (int)Mathf.random(-100, 100))).setCount(item.getCount()));
		remove(item);
	}

	public void dropAll()
	{
		for(Item item : inventoryItems)
		{
			drop(item);
		}
	}
}
