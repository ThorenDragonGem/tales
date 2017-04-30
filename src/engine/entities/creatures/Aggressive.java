package engine.entities.creatures;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.util.List;

import org.joml.Vector2f;

import engine.assets.Assets;
import engine.entities.Entity;
import engine.entities.stats.MonsterRank;
import engine.physics.Handler;
import engine.physics.Physics;
import engine.physics.geom.Circle;

public class Aggressive extends Creature
{
	private Ellipse2D ar, sr;

	public Aggressive(MonsterRank rank, Handler handler, float x, float y, int width, int height)
	{
		super(rank, handler, x, y, width, height);
		body.getBounds().setBounds(16, 16, 32, 32);
		stats.getStat("atr").setBaseValue(100f);
		stats.initAll();
	}

	@Override
	public void update()
	{
		super.update();
//		ar = new Ellipse2D.Float((float)(body.getPosBounds().x + body.getPosBounds().width / 2 - handler.getCamera().getxOffset() - stats.get("atr") / 2), (float)(body.getPosBounds().y + body.getPosBounds().height / 2 - handler.getCamera().getyOffset() - stats.get("atr") / 2), (float)stats.get("atr"), (float)stats.get("atr"));
//		sr = new Ellipse2D.Float((float)(body.getPosBounds().x + body.getPosBounds().width / 2 - handler.getCamera().getxOffset() - stats.get("sgr") / 2), (float)(body.getPosBounds().y + body.getPosBounds().height / 2 - handler.getCamera().getyOffset() - stats.get("sgr") / 2), (float)stats.get("sgr"), (float)stats.get("sgr"));
		setAttacking(false);
		setDefending(false);
		if(stats.target == null)
		{
			// TODO ?
			// aiManager.get("look").updateAI();
			look();
			if(cd.isOver())
				cd.stop();
		}
		else
		{
			stats.target.setAttacked(false);
			if(!cd.isActive())
				cd.go();
				
			if(Physics.length(body.getPosition().x + body.getWidth() / 2, body.getPosition().y + body.getHeight() / 2, stats.target.getBody().getPosition().x + stats.target.getBody().getWidth() / 2, stats.target.getBody().getPosition().y + stats.target.getBody().getHeight() / 2) <= stats.get("atr"))
			// if(ar.contains(stats.target.getBody().getPosBounds()))
			{
				if(cd.isOver())
					attack();
				// attack(stats.target);
			}
			else if(Physics.length(body.getPosition().x + body.getWidth() / 2, body.getPosition().y + body.getHeight() / 2, stats.target.getBody().getPosition().x + stats.target.getBody().getWidth() / 2, stats.target.getBody().getPosition().y + stats.target.getBody().getHeight() / 2) <= stats.get("sgr"))
			// else if(sr.contains(stats.target.getBody().getPosBounds()))
			{
				chase();
				// move(ray.direction.x, ray.direction.y);
			}
			else
			{
				stats.target = null;
			}
			if(!handler.getWorld().getEntityManager().getEntities().contains(stats.target))
				stats.target = null;
		}
	}

	@Override
	public void render(Graphics graphics)
	{
		super.render(graphics);
		graphics.drawImage(Assets.mouse, (int)(body.getPosition().x - handler.getCamera().getxOffset()), (int)(body.getPosition().y - handler.getCamera().getyOffset()), body.getWidth(), body.getHeight(), null);
//		graphics.setColor(Color.red);
//		graphics.drawOval((int)ar.getX(), (int)ar.getY(), (int)ar.getWidth(), (int)ar.getHeight());
//		graphics.setColor(Color.blue);
//		graphics.drawOval((int)(sr.getX()), (int)(sr.getY()), (int)sr.getWidth(), (int)sr.getHeight());
//		graphics.setColor(Color.red);
//		graphics.fillRect((int)(body.getPosBounds().x - handler.getCamera().getxOffset()), (int)(body.getPosBounds().y - handler.getCamera().getyOffset()), (body.getPosBounds().width), (body.getPosBounds().height));
	}

	@Override
	public void die()
	{
		super.die();
	}

	protected void look()
	{
		List<Entity> entities = Physics.sphereCollideEntities(handler, new Circle(body.getPosition().x + body.getWidth() / 2, body.getPosition().y + body.getHeight() / 2, (float)stats.get("sgr")));
		for(Entity e : entities)
		{
			if(e instanceof Player && e != this)
				stats.target = e;
			// make the nearest the target
		}
	}

	int index = 0;
	protected void chase()
	{
//		List<GridCell> pathToEnd = handler.getWorld().getFinder().findPath(handler.getWorld().getNavGrid().getCell((int)(body.getPosition().x / Tile.TILEWIDTH), (int)(body.getPosition().y / Tile.TILEHEIGHT)), handler.getWorld().getNavGrid().getCell((int)(target.getBody().getPosition().x / Tile.TILEWIDTH), (int)(target.getBody().getPosition().y / Tile.TILEHEIGHT)), handler.getWorld().getNavGrid());
//		System.out.println(pathToEnd);
		body.update();
		xMove = body.getForce().x;
		yMove = body.getForce().y;
		Vector2f direction = new Vector2f(stats.target.getBody().getPosition().x - body.getPosition().x, stats.target.getBody().getPosBounds().y - body.getPosition().y).normalize();
//		if(pathToEnd.get(index) != null)
//		{
//			Vector2f direction = new Vector2f(pathToEnd.get(index).x - body.getPosition().x, pathToEnd.get(index).y - body.getPosition().y).normalize();
		xMove = body.applyForce((float)(direction.x * stats.get("velocity")), 0).x;
		yMove = body.applyForce(0, (float)(direction.y * stats.get("velocity"))).y;
//		}
//		if(body.getPosition().equals(new Vector2f(pathToEnd.get(index).x, pathToEnd.get(index).y)))
//			index++;
//		if(index >= pathToEnd.size())
//			index = pathToEnd.size() - 1;
		move();
	}

	protected void attack()
	{
		cd.restart();
		setAttacking(true);
		stats.target.setAttacked(true);
		stats.target.getStats().target = this;
		stats.physicalDamages(1);
	}

	protected void defend()
	{
		setDefending(true);
	}
}
