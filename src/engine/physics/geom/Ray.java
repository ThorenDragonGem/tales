package engine.physics.geom;

import org.joml.Vector2f;

public class Ray
{
	public Vector2f origin = new Vector2f(), direction = new Vector2f();

	public Ray(Vector2f origin, Vector2f direction)
	{
		this.origin.set(origin);
		this.direction.set(direction).normalize();
	}

	public Ray copy()
	{
		return new Ray(origin, direction);
	}

	public Vector2f getEndPoint(final Vector2f out, final float distance)
	{
		return out.set(direction).mul(distance).add(origin);
	}

	public Ray set(Vector2f origin, Vector2f direction)
	{
		this.origin.set(origin);
		this.direction.set(direction);
		return this;
	}

	public Ray set(float x, float y, float dx, float dy)
	{
		this.origin.set(x, y);
		this.direction.set(dx, dy);
		return this;
	}

	public Ray set(Ray ray)
	{
		this.origin.set(ray.origin);
		this.direction.set(ray.direction);
		return this;
	}

	@Override
	public String toString()
	{
		return "[" + origin.toString() + " ; " + direction.toString() + "]";
	}
}
