package engine.tiles;

import engine.assets.Assets;

public class RockTile extends Tile
{

	public RockTile(int id)
	{
		super(Assets.rockTile, id);
	}

	@Override
	public boolean isSolid()
	{
		return true;
	}
}
