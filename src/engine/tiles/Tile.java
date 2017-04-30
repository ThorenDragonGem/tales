package engine.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tile
{
	public static Tile[] tiles = new Tile[256];
	public static Tile grass = new GrassTile(0);
	public static Tile rock = new RockTile(1);
	public static Tile dirt = new DirtTile(2);
	
	public static final int TILEWIDTH = 64, TILEHEIGHT = 64;
	protected BufferedImage texture;
	protected final int id;

	public Tile(BufferedImage texture, int id)
	{
		this.texture = texture;
		this.id = id;

		tiles[id] = this;
	}

	public void update()
	{

	}

	public void render(Graphics graphics, int x, int y)
	{
		graphics.drawImage(texture, x, y, TILEWIDTH, TILEWIDTH, null);
	}

	public boolean isSolid()
	{
		return false;
	}

	public int getId()
	{
		return id;
	}
}
