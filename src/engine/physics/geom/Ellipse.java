package engine.physics.geom;

import org.joml.Vector2f;

public class Ellipse
{
	public float x, y;
	public float width, height;

	public Ellipse(float x, float y, float width, float height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public boolean contains(float x, float y)
	{
		x = x - this.x;
		y = y - this.y;

		return (x * x) / (width * 0.5f * width * 0.5f) + (y * y) / (height * 0.5f * height * 0.5f) <= 1.0f;
	}

	public boolean contains(Vector2f point)
	{
		return contains(point.x, point.y);
	}

	public void set(float x, float y, float width, float height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void set(Ellipse ellipse)
	{
		x = ellipse.x;
		y = ellipse.y;
		width = ellipse.width;
		height = ellipse.height;
	}

	public void set(Vector2f position, Vector2f size)
	{
		this.x = position.x;
		this.y = position.y;
		this.width = size.x;
		this.height = size.y;
	}

	public Ellipse setPosition(Vector2f position)
	{
		this.x = position.x;
		this.y = position.y;

		return this;
	}

	public Ellipse setPosition(float x, float y)
	{
		this.x = x;
		this.y = y;

		return this;
	}

	public Ellipse setSize(float width, float height)
	{
		this.width = width;
		this.height = height;

		return this;
	}

	public float area()
	{
		return (float)(Math.PI * (this.width * this.height) / 4);
	}

	public float circumference()
	{
		float a = this.width / 2;
		float b = this.height / 2;
		if(a * 3 > b || b * 3 > a)
		{
			// If one dimension is three times as long as the other...
			return (float)(Math.PI * ((3 * (a + b)) - Math.sqrt((3 * a + b) * (a + 3 * b))));
		}
		else
		{
			// We can use the simpler approximation, then
			return (float)((Math.PI * 2) * Math.sqrt((a * a + b * b) / 2));
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if(o == null || o.getClass() != this.getClass())
			return false;
		Ellipse e = (Ellipse)o;
		return this.x == e.x && this.y == e.y && this.width == e.width && this.height == e.height;
	}

	@Override
	public String toString()
	{
		return "{" + x + " , " + y + " , " + width + " , " + height + "}";
	}
}