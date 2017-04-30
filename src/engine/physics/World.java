package engine.physics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.joml.Vector2f;
import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;

import engine.assets.Assets;
import engine.entities.Entity;
import engine.entities.EntityManager;
import engine.entities.creatures.Aggressive;
import engine.entities.creatures.Player;
import engine.entities.statics.Rock;
import engine.entities.statics.Tree;
import engine.entities.stats.MonsterRank;
import engine.inputs.ParticleSystem;
import engine.items.Item;
import engine.items.ItemManager;
import engine.physics.collisions.AABB;
import engine.physics.geom.Ray;
import engine.tiles.Tile;
import engine.utils.ImageLoader;
import engine.utils.Text;
import engine.utils.Utils;

public class World
{
	private GridCell[][] cells;
	private NavigationGrid<GridCell> navGrid;
	private AStarGridFinder<GridCell> finder;
	private Handler handler;
	private ParticleSystem particleSystem;
	private int width, height;
	private int[][] tiles;
	private AABB[][] tileBounds;
	private int spawnX;
	private int spawnY;

	private EntityManager entityManager;
	private ItemManager itemManager;

	public World(Handler handler, String path)
	{
		this.handler = handler;
		this.particleSystem = new ParticleSystem(handler);
		entityManager = new EntityManager(handler, new Player(handler, 100, 100));
		entityManager.add(new Rock(handler, 3 * 64, 3 * 64));
		entityManager.add(new Aggressive(MonsterRank.BRONZE5, handler, 3 * 64, 2 * 64, 64, 64));
		itemManager = new ItemManager(handler);

		// loadWorldFromTextMap(path);
		loadWorldFromImage("/textures/level");

		entityManager.getPlayer().getBody().getPosition().set(new Vector2f(spawnX, spawnY));
	}

	public World(Handler handler)
	{
		this.handler = handler;
		width = 5;
		height = 5;
		tiles = new int[width][height];
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				tiles[x][y] = 1;
	}

	public void update()
	{
		itemManager.update();
		entityManager.update();
		particleSystem.update();
	}

	public void render(Graphics graphics)
	{
		int xStart = (int)Math.max(0, handler.getCamera().getxOffset() / Tile.TILEWIDTH);
		int xEnd = (int)Math.min(width, (handler.getCamera().getxOffset() + handler.getWidth()) / Tile.TILEWIDTH + 1);
		int yStart = (int)Math.max(0, handler.getCamera().getyOffset() / Tile.TILEHEIGHT);
		int yEnd = (int)Math.min(height, (handler.getCamera().getyOffset() + handler.getHeight()) / Tile.TILEHEIGHT + 1);
		for(int y = yStart; y < yEnd; y++)
			for(int x = xStart; x < xEnd; x++)
				getTile(x, y).render(graphics, (int)(x * Tile.TILEWIDTH - handler.getCamera().getxOffset()), (int)(y * Tile.TILEHEIGHT - handler.getCamera().getyOffset()));
		itemManager.render(graphics);
		entityManager.render(graphics);
		particleSystem.render(graphics);
//		Text.drawString(graphics, handler.getCamera().getPickRay(handler.getMouse().getMouseX(), handler.getMouse().getMouseY()).origin.toString(), 300, 20, true, Color.white, Assets.font28);
		if(getPickingEntity(handler.getMouse().getMouseX(), handler.getMouse().getMouseY()) != null)
			Text.drawString(graphics, getPickingEntity(handler.getMouse().getMouseX(), handler.getMouse().getMouseY()).getClass().getSimpleName(), 300, 20, true, Color.white, Assets.font28);
		Text.drawString(graphics, Integer.toString((int)entityManager.getPlayer().getBody().getPosition().x), 200, 100, true, Color.white, Assets.font28);
		Text.drawString(graphics, Integer.toString((int)entityManager.getPlayer().getBody().getPosition().y), 200, 150, true, Color.white, Assets.font28);
	}

	public Tile getTile(int x, int y)
	{
		if(x < 0 || y < 0 || x >= width || y >= height)
			return Tile.grass;

		Tile tile = Tile.tiles[tiles[x][y]];
		if(tile == null)
			return Tile.dirt;
		return tile;
	}

	public void setTile(Tile tile, int x, int y)
	{
		if(tile == null)
		{
			tiles[x][y] = Tile.grass.getId();
			cells[x][y] = new GridCell(!getTile(x, y).isSolid());
			cells[x][y].setX(x);
			cells[x][y].setY(y);
			return;
		}
		tiles[x][y] = tile.getId();

	}

	public AABB getTileBounds(int x, int y)
	{
		try
		{
			return tileBounds[x][y];
		}
		catch(Exception e)
		{
			return null;
		}
	}

	private void loadWorldFromTextMap(String path)
	{
		String file = Utils.loadFileAsString(path);
		String[] tokens = file.split("\\s+");
		width = Utils.parseInt(tokens[0]);
		height = Utils.parseInt(tokens[1]);
		spawnX = Utils.parseInt(tokens[2]);
		spawnY = Utils.parseInt(tokens[3]);

		tiles = new int[width][height];
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				tiles[x][y] = Utils.parseInt(tokens[(x + y * width) + 4]);
	}

	private void loadWorldFromImage(String path)
	{
		BufferedImage world = ImageLoader.loadImage(path + "_world.png");
		BufferedImage entities = ImageLoader.loadImage(path + "_entities.png");
		width = world.getWidth();
		height = world.getHeight();

		int[] mapColors = world.getRGB(0, 0, width, height, null, 0, width);
		int[] entityColors = entities.getRGB(0, 0, width, height, null, 0, width);
		tiles = new int[width][height];
		cells = new GridCell[width][height];
		tileBounds = new AABB[width][height];
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				if(entityColors[i + j * width] == Color.MAGENTA.getRGB())
				{
					spawnX = i * Tile.TILEWIDTH;
					spawnY = j * Tile.TILEHEIGHT;
					continue;
				}
				if(entityColors[i + j * width] < 0)
				{
					// switch
					entityManager.add(new Tree(handler, i * Tile.TILEWIDTH, j * Tile.TILEHEIGHT));
				}
			}
		}

		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				int red = (mapColors[i + j * width] >> 16) & 0xFF;
				tiles[i][j] = red;
				// create cells
				GridCell cell = new GridCell(!getTile(i, j).isSolid());
				cell.setX(i);
				cell.setY(j);
				cells[i][j] = cell;
			}
		}
		navGrid = new NavigationGrid<GridCell>(cells, false);
		finder = new AStarGridFinder<GridCell>(GridCell.class);
		spawnX = 128 * 4;
		spawnY = 128 * 4;
	}

	public Entity getPickingEntity(float screenX, float screenY)
	{
		Ray pickingRay = handler.getCamera().getPickRay(screenX, screenY);
		Rectangle rayBounds = new Rectangle((int)pickingRay.origin.x, (int)pickingRay.origin.y, 1, 1);
		for(Entity e : entityManager.getEntities())
			if(e.getCollisionBounds(0, 0).intersects(rayBounds))
				return e;
		return null;
	}

	// TODO
	public Item getPickingItem(float screenX, float screenY)
	{
		Ray pickingRay = handler.getCamera().getPickRay(screenX, screenY);
		Rectangle rayBounds = new Rectangle((int)pickingRay.origin.x, (int)pickingRay.origin.y, 1, 1);
		for(Item e : itemManager.getItems())
		{
			if(e.getCollisionBounds(0, 0).intersects(rayBounds))
				return e;
		}
		return null;
	}


	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public EntityManager getEntityManager()
	{
		return entityManager;
	}

	public Handler getHandler()
	{
		return handler;
	}

	public void setHandler(Handler handler)
	{
		this.handler = handler;
	}

	public ItemManager getItemManager()
	{
		return itemManager;
	}

	public void setItemManager(ItemManager itemManager)
	{
		this.itemManager = itemManager;
	}

	public AStarGridFinder<GridCell> getFinder()
	{
		return finder;
	}

	public GridCell[][] getCells()
	{
		return cells;
	}

	public NavigationGrid<GridCell> getNavGrid()
	{
		return navGrid;
	}

	public ParticleSystem getParticleSystem()
	{
		return particleSystem;
	}
}
