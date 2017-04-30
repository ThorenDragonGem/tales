package engine.entities.creatures;

import java.awt.Graphics;
import java.awt.Rectangle;

import org.joml.Vector2f;

import engine.entities.Entity;
import engine.entities.stats.MonsterRank;
import engine.physics.Handler;
import engine.physics.collisions.Collision;
import engine.tiles.Tile;

public abstract class Creature extends Entity
{
	public static final int DEFAULT_CREATURE_WIDTH = 64;
	public static final int DEFAULT_CREATURE_HEIGHT = 64;
	public static final float DEFAULT_SPEED = 1.0f;

	protected float speed;
	protected float xMove, yMove;

	public Creature(MonsterRank rank, Handler handler, float x, float y, int width, int height)
	{
		super(rank, handler, x, y, width, height);
		this.speed = DEFAULT_SPEED;
		xMove = 0;
		yMove = 0;
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
	}

	public void move()
	{
		if(!checkEntitytCollisions(xMove, 0))
			moveX();
		if(!checkEntitytCollisions(0, yMove))
			moveY();
	}

	public void moveX()
	{
		if(xMove > 0)
		{
			int tx = (int)(body.getPosition().x + xMove + body.getBounds().x + body.getBounds().width) / Tile.TILEWIDTH;

			if(!collisionWithTile(tx, (int)(body.getPosition().y + body.getBounds().y) / Tile.TILEHEIGHT) && !collisionWithTile(tx, (int)(body.getPosition().y + body.getBounds().y + body.getBounds().height) / Tile.TILEHEIGHT))
			{
				body.getPosition().x += xMove;
			}
			else
			{
				body.getPosition().x = tx * Tile.TILEWIDTH - body.getBounds().x - body.getBounds().width - 1;
			}
		}
		else if(xMove < 0)
		{
			int tx = (int)(body.getPosition().x + xMove + body.getBounds().x) / Tile.TILEWIDTH;

			if(!collisionWithTile(tx, (int)(body.getPosition().y + body.getBounds().y) / Tile.TILEHEIGHT) && !collisionWithTile(tx, (int)(body.getPosition().y + body.getBounds().y + body.getBounds().height) / Tile.TILEHEIGHT))
			{
				body.getPosition().x += xMove;
			}
			else
			{
				body.getPosition().x = tx * Tile.TILEWIDTH + Tile.TILEWIDTH - body.getBounds().x;
			}
		}
	}

	public void moveY()
	{
		if(yMove > 0)
		{
			int ty = (int)(body.getPosition().y + yMove + body.getBounds().y + body.getBounds().height) / Tile.TILEHEIGHT;

			if(!collisionWithTile((int)(body.getPosition().x + body.getBounds().x) / Tile.TILEWIDTH, ty) && !collisionWithTile((int)(body.getPosition().x + body.getBounds().x + body.getBounds().width) / Tile.TILEWIDTH, ty))
			{
				body.getPosition().y += yMove;
			}
			else
			{
				body.getPosition().y = ty * Tile.TILEHEIGHT - body.getBounds().y - body.getBounds().height - 1;
			}
		}
		else if(yMove < 0)
		{
			int ty = (int)(body.getPosition().y + yMove + body.getBounds().y) / Tile.TILEHEIGHT;

			if(!collisionWithTile((int)(body.getPosition().x + body.getBounds().x) / Tile.TILEWIDTH, ty) && !collisionWithTile((int)(body.getPosition().x + body.getBounds().x + body.getBounds().width) / Tile.TILEWIDTH, ty))
			{
				body.getPosition().y += yMove;
			}

			else
			{
				body.getPosition().y = ty * Tile.TILEHEIGHT + Tile.TILEHEIGHT - body.getBounds().y;
			}
		}
	}

	public Collision getCollision(Rectangle bounds, Rectangle box)
	{
		Vector2f boxCenter = new Vector2f((float)box.getCenterX(), (float)box.getCenterY());
		Vector2f boundsCenter = new Vector2f((float)bounds.getCenterX(), (float)bounds.getCenterY());
		Vector2f distance = boxCenter.sub(boundsCenter, new Vector2f());
		distance.x = Math.abs(distance.x);
		distance.y = Math.abs(distance.y);
		distance.sub(new Vector2f(bounds.width / 2, bounds.height / 2).add(new Vector2f(box.width / 2, box.height / 2)), new Vector2f());

		return new Collision(distance, distance.x < 0 && distance.y < 0);
	}

	protected boolean collisionWithTile(int x, int y)
	{
		return handler.getWorld().getTile(x, y).isSolid();
	}

	public float getSpeed()
	{
		return speed;
	}

	public float getxMove()
	{
		return xMove;
	}

	public float getyMove()
	{
		return yMove;
	}
}
