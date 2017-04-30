package engine.entities.statics;

import java.awt.Graphics;

import engine.assets.Assets;
import engine.items.Item;
import engine.physics.Handler;
import engine.tiles.Tile;

public class Tree extends StaticEntity
{

	public Tree(Handler handler, float x, float y)
	{
		// position on trunk
		super(handler, x, y - Tile.TILEHEIGHT, Tile.TILEWIDTH, Tile.TILEHEIGHT * 2);

		stats.health = 1;
		body.getBounds().x = 10;
		body.getBounds().width = body.getWidth() - 20;
		body.getBounds().height = (int)(body.getWidth() - ((body.getHeight() / 2) / 3f));
		body.getBounds().y = body.getHeight() - body.getBounds().height;
	}

	@Override
	public void update()
	{
		super.update();
	}

	@Override
	public void render(Graphics graphics)
	{
		super.render(graphics);
		graphics.drawImage(Assets.pine, (int)(body.getPosition().x - handler.getCamera().getxOffset()), (int)(body.getPosition().y - handler.getCamera().getyOffset()), body.getWidth(), body.getHeight(), null);
//		graphics.setColor(Color.RED);
//		graphics.fillRect((int)(body.getPosition().x + body.getBounds().x - handler.getCamera().getxOffset()), (int)(body.getPosition().y + body.getBounds().y - handler.getCamera().getyOffset()), body.getBounds().width, body.getBounds().height);
	}

	@Override
	public void die()
	{
		super.die();
		handler.getWorld().getItemManager().addItem(Item.woodItem, this, 100);
	}

}
