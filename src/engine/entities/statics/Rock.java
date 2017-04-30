package engine.entities.statics;

import java.awt.Graphics;

import engine.assets.Assets;
import engine.items.Item;
import engine.physics.Handler;
import engine.tiles.Tile;

public class Rock extends StaticEntity
{

	public Rock(Handler handler, float x, float y)
	{
		super(handler, x, y, Tile.TILEWIDTH, Tile.TILEHEIGHT);
		body.getBounds().x = 0;
		body.getBounds().y = 0;
		body.getBounds().width = 32;
		body.getBounds().height = 32;
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
		graphics.drawImage(Assets.rock, (int)(body.getPosition().x - body.getBounds().x - handler.getCamera().getxOffset()), (int)(body.getPosition().y - body.getBounds().y - handler.getCamera().getyOffset()), body.getWidth(), body.getHeight(), null);
//		graphics.setColor(Color.RED);
//		graphics.fillRect((int)(body.getPosition().x + body.getBounds().x - handler.getCamera().getxOffset()), (int)(body.getPosition().y + body.getBounds().y - handler.getCamera().getyOffset()), body.getBounds().width, body.getBounds().height);
	}

	@Override
	public void die()
	{
		super.die();
		handler.getWorld().getItemManager().addItem(Item.rockItem.createNew((int)body.getPosition().x, (int)body.getPosition().y));
	}
}
