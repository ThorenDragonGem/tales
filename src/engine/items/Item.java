package engine.items;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import engine.assets.Assets;
import engine.physics.Handler;

public class Item
{
	public static Item[] items = new Item[256];
	public static Item woodItem = new Item(Assets.stick, "Stick", 0);
	public static Item rockItem = new Item(Assets.rock, "Rock", 1);

	public static final int ITEMWIDTH = 32, ITEMHEIGHT = 32;

	protected Handler handler;
	protected BufferedImage texture;
	protected String name;
	protected final int id;

	protected Rectangle bounds;

	protected int x, y, count, maxCount;
	protected boolean pickedUp = false;

	public Item(BufferedImage texture, String name, int id)
	{
		this.texture = texture;
		this.name = name;
		this.id = id;
		maxCount = 64;
		count = 1;

		bounds = new Rectangle(x, y, ITEMWIDTH, ITEMHEIGHT);

		items[id] = this;
	}

	public void update()
	{
		if(handler.getWorld().getEntityManager().getPlayer().getCollisionBounds(0, 0).intersects(bounds))
		{
			pickedUp = true;
			handler.getWorld().getEntityManager().getPlayer().getInventory().add(this);
		}
	}

	public void render(Graphics graphics)
	{
		if(handler == null)
			return;
		render(graphics, (int)(x - handler.getCamera().getxOffset()), (int)(y - handler.getCamera().getyOffset()));
	}

	public void render(Graphics graphics, int x, int y)
	{
		graphics.drawImage(texture, x, y, ITEMWIDTH, ITEMHEIGHT, null);
	}

	public Item setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
		bounds.x = x;
		bounds.y = y;
		return this;
	}

	public Item createNew(int x, int y)
	{
		Item item = new Item(texture, name, id);
		item.setPosition(x, y);
		return item;
	}

	public Item createNew(int count)
	{
		Item item = new Item(texture, name, id);
		item.setPickedUp(true);
		item.setCount(count);
		return item;
	}

	public Handler getHandler()
	{
		return handler;
	}

	public void setHandler(Handler handler)
	{
		this.handler = handler;
	}

	public BufferedImage getTexture()
	{
		return texture;
	}

	public void setTexture(BufferedImage texture)
	{
		this.texture = texture;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
		bounds.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
		bounds.y = y;
	}

	public int getCount()
	{
		return count;
	}

	public Item setCount(int count)
	{
		this.count = count;
		return this;
	}

	public int getMaxCount()
	{
		return maxCount;
	}

	public void setMaxCount(int maxCount)
	{
		this.maxCount = maxCount;
	}

	public boolean hasFreeSlots()
	{
		return count < maxCount;
	}

	public int getId()
	{
		return id;
	}

	public boolean isPickedUp()
	{
		return pickedUp;
	}

	public void setPickedUp(boolean pickedUp)
	{
		this.pickedUp = pickedUp;
	}

	public Rectangle getCollisionBounds(float xOffset, float yOffset)
	{
		return new Rectangle((int)(x + bounds.x + xOffset), (int)(y + bounds.y + yOffset), bounds.width, bounds.height);
//		return new Rectangle((int)(body.getPosition().x + body.getBounds().x + xOffset), (int)(body.getPosition().y + body.getBounds().y + yOffset), body.getBounds().width, body.getBounds().height);
	}
}
