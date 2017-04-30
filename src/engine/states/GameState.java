package engine.states;

import java.awt.Graphics;

import engine.physics.Handler;
import engine.physics.World;
import engine.tiles.Tile;

public class GameState extends State
{
	private World world;

	public GameState(Handler handler)
	{
		super(handler);
		world = new World(handler, "./res/worlds/world.txt");
		handler.setWorld(world);
		world.setTile(Tile.dirt, 1, 1);
	}

	@Override
	public void update()
	{
		world.update();
	}

	@Override
	public void render(Graphics graphics)
	{
		world.render(graphics);
	}

}
