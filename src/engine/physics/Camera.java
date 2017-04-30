package engine.physics;

import org.joml.Vector2f;

import engine.entities.Entity;
import engine.physics.geom.Ray;
import engine.tiles.Tile;

public class Camera
{
	private Handler handler;
	private Vector2f offset;

	public Camera(Handler handler, float xOffset, float yOffset)
	{
		this.handler = handler;
		this.offset = new Vector2f(xOffset, yOffset);
	}

	public void checkBlankSpace()
	{
		if(offset.x < 0)
			offset.x = 0;
		else if(offset.x > handler.getWorld().getWidth() * Tile.TILEWIDTH - handler.getWidth())
			offset.x = handler.getWorld().getWidth() * Tile.TILEWIDTH - handler.getWidth();
		if(offset.y < 0)
			offset.y = 0;
		else if(offset.y > handler.getWorld().getHeight() * Tile.TILEHEIGHT - handler.getHeight())
			offset.y = handler.getWorld().getHeight() * Tile.TILEHEIGHT - handler.getHeight();
	}

	public void centerOnEntity(Entity e)
	{
		this.offset.lerp(new Vector2f(e.getBody().getPosition().x - handler.getWidth() / 2 + e.getBody().getWidth() / 2, e.getBody().getPosition().y - handler.getHeight() / 2 + e.getBody().getHeight() / 2), 0.05f);
		checkBlankSpace();
	}

	public void move(float x, float y)
	{
		offset.add(x, y);
		checkBlankSpace();
	}

	public Ray getPickRay(float screenX, float screenY)
	{
		Ray ray = new Ray(new Vector2f(), new Vector2f());
		/** (distance to camera in world: camera.offset.x - world origin: here 0) + (distance to point in camera: screenX - camera.origin.x: here 0) */
		/** same for Y => a simply story of widths and heights */
//		ray.origin.set((offset.x - 0) + (screenX - 0), (offset.y - 0) + (screenY - 0));
		ray.origin.set(offset.x + screenX, offset.y + screenY);
		ray.direction.set(0, 0);
		return ray;
	}

	public float getxOffset()
	{
		return offset.x;
	}

	public void setxOffset(float xOffset)
	{
		this.offset.x = xOffset;
	}

	public float getyOffset()
	{
		return offset.y;
	}

	public Vector2f getOffset()
	{
		return offset;
	}

	public void setyOffset(float yOffset)
	{
		this.offset.y = yOffset;
	}
}
