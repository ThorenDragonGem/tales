package engine.entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import engine.entities.creatures.Player;
import engine.physics.Handler;

public class EntityManager
{
	private Handler handler;
	private Player player;
	private List<Entity> entities;
	private Comparator<Entity> renderSorter = new Comparator<Entity>()
	{
		@Override
		public int compare(Entity a, Entity b)
		{
			if(a.body.getPosition().y + a.body.getHeight() < b.body.getPosition().y + b.body.getHeight())
				return -1;
			return 1;
		};
	};

	public EntityManager(Handler handler, Player player)
	{
		this.handler = handler;
		this.player = player;
		entities = new ArrayList<Entity>();
		add(player);
	}

	public void update()
	{
		Iterator<Entity> it = entities.iterator();
		while(it.hasNext())
		{
			Entity e = it.next();
			if(!handler.getGame().isPaused())
				e.update();
			e.postUpdate();
			if(!e.isActive())
				it.remove();
		}
		entities.sort(renderSorter);
	}

	public void render(Graphics graphics)
	{
		// different 'for' loop => collision detection
		for(Entity e : entities)
		{
			e.render(graphics);
		}
		player.postRender(graphics);
	}

	public void add(Entity e)
	{
		entities.add(e);
	}

	public Handler getHandler()
	{
		return handler;
	}

	public Player getPlayer()
	{
		return player;
	}

	public List<Entity> getEntities()
	{
		return entities;
	}
}
