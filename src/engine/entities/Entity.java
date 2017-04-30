package engine.entities;

import java.awt.Graphics;
import java.awt.Rectangle;

import org.joml.Vector2f;

import engine.entities.creatures.Creature;
import engine.entities.creatures.Player;
import engine.entities.stats.EntityStats;
import engine.entities.stats.MonsterRank;
import engine.physics.Handler;
import engine.physics.collisions.RigidBody;
import engine.utils.CoolDown;

public class Entity
{
	public static Entity[] ids = new Entity[256 * 256 * 256];
	public static int pointer;
	public static final int DEFAULT_HEALTH = 10;
	protected MonsterRank rank;
	protected EntityStats stats;
	protected Handler handler;
	protected RigidBody body;
	protected boolean active = true, defending = false, attacking = false, attacked = false;
	protected int attackCD;
	protected CoolDown cd;
	protected final int id;

	public Entity(MonsterRank rank, Handler handler, float x, float y, int width, int height)
	{
		this.rank = rank;
		this.handler = handler;
		this.body = new RigidBody(new Vector2f(x, y), width, height);
		this.stats = new EntityStats(this);
		attackCD = (int)(60d / stats.get("as"));
		this.cd = new CoolDown(attackCD);
		this.id = pointer;
		pointer++;
		ids[id] = this;
	}

	private int halfSecond = 0;

	public void update()
	{
		if(!stats.isAlive())
			die();

		attackCD = (int)(60d / stats.get("as"));
		cd.setTime((int)(1 - stats.get("cdr")) * attackCD);
		if(halfSecond % (handler.getGame().getTps() / 2) == 0)
		{
			halfSecondUpdate();
			halfSecond = 0;
		}
		halfSecond++;

		if(this instanceof Creature && !(this instanceof Player))
			stats.applyStatsForLevel();

		cd.update();
	}

	public void halfSecondUpdate()
	{
		stats.heal(stats.get("regen") / 2d);
		stats.mana(stats.get("manaRegen") / 2d);
	}

	public void postUpdate()
	{

	}

	public void die()
	{
		active = false;
	}

	public void render(Graphics graphics)
	{

	}

	public boolean checkEntitytCollisions(float xOffset, float yOffset)
	{
		for(Entity e : handler.getWorld().getEntityManager().getEntities())
		{
			if(e == this)
				continue;
			if(e.getCollisionBounds(0, 0).intersects(getCollisionBounds(xOffset, yOffset)))
				return true;
		}
		return false;
	}

	public Rectangle getCollisionBounds(float xOffset, float yOffset)
	{
		return new Rectangle((int)(body.getPosition().x + body.getBounds().x + xOffset), (int)(body.getPosition().y + body.getBounds().y + yOffset), body.getBounds().width, body.getBounds().height);
	}

	public RigidBody getBody()
	{
		return body;
	}

	public EntityStats getStats()
	{
		return stats;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public int getId()
	{
		return id;
	}

	public MonsterRank getRank()
	{
		return rank;
	}

	public boolean isDefending()
	{
		return defending;
	}

	public void setDefending(boolean defending)
	{
		this.defending = defending;
	}

	public boolean isAttacked()
	{
		return attacked;
	}

	public void setAttacked(boolean attacked)
	{
		this.attacked = attacked;
	}

	public boolean isAttacking()
	{
		return attacking;
	}

	public void setAttacking(boolean attacking)
	{
		this.attacking = attacking;
	}
}
