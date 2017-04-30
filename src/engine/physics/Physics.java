package engine.physics;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import engine.entities.Entity;
import engine.physics.geom.Circle;
import engine.utils.Mathf;

public class Physics
{
	public static boolean overlaps(Circle c, Rectangle r)
	{
		float closestX = c.x;
		float closestY = c.y;

		if(c.x < r.x)
		{
			closestX = r.x;
		}
		else if(c.x > r.x + r.width)
		{
			closestX = r.x + r.width;
		}

		if(c.y < r.y)
		{
			closestY = r.y;
		}
		else if(c.y > r.y + r.height)
		{
			closestY = r.y + r.height;
		}

		closestX = closestX - c.x;
		closestX *= closestX;
		closestY = closestY - c.y;
		closestY *= closestY;

		return closestX + closestY < c.radius * c.radius;
	}

	public static List<Entity> sphereCollideEntities(Handler handler, Circle circle)
	{
		List<Entity> res = new ArrayList<Entity>();
		for(Entity e : handler.getWorld().getEntityManager().getEntities())
			if(overlaps(circle, e.getBody().getPosBounds()))
				res.add(e);
		return res;
	}

	public static <T extends Entity> List<Entity> sphereCollide(Handler handler, Circle circle, Class<T> clazz)
	{
		List<Entity> res = new ArrayList<Entity>();
		for(Entity e : handler.getWorld().getEntityManager().getEntities())
			if(e.getClass().getName() == clazz.getName())
				if(overlaps(circle, e.getBody().getPosBounds()))
					res.add(e);
		return res;
	}

	public static <T extends Entity> List<Entity> sphereCollide(Handler handler, Circle circle)
	{
		return sphereCollide(handler, circle, Entity.class);
	}

	public static float length(float x1, float y1, float x2, float y2)
	{
		float x = x2 - x1;
		float y = y2 - y1;
		return Mathf.sqrt(x * x + y * y);
	}

}
