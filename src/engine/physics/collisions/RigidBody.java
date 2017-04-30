package engine.physics.collisions;

import java.awt.Rectangle;

import org.joml.Vector2f;

public class RigidBody
{
	private Vector2f force;
	private Vector2f position;
	private int width, height;
	private Rectangle bounds;

	public RigidBody(Vector2f position, int width, int height)
	{
		this.position = position;
		this.force = new Vector2f();
		this.width = width;
		this.height = height;
		this.bounds = new Rectangle(0, 0, width, height);
	}

	public void update()
	{
		// this.position.add(force);
		this.force.mul(0.9f);
	}

	public Vector2f applyForce(float forceX, float forceY)
	{
		this.force.add(forceX, forceY);
		return this.force;
	}

	public Vector2f applyForce(Vector2f force)
	{
		this.force.add(force);
		return this.force;
	}

	public Vector2f getPosition()
	{
		return position;
	}

	public void setX(float x)
	{
		this.position.x = x;
	}

	public void setY(float y)
	{
		this.position.y = y;
	}

	public Vector2f getForce()
	{
		return force;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public Rectangle getBounds()
	{
		return bounds;
	}

	public Rectangle getPosBounds()
	{
		return new Rectangle((int)(position.x + bounds.x), (int)(position.y + bounds.y), bounds.width, bounds.height);
	}
}
