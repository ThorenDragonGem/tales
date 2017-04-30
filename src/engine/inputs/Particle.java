package engine.inputs;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.joml.Vector2f;

import engine.graphics.Animation;
import engine.utils.Mathf;

/**
 * Creates a new particle instance. The particle is only represented by a moving point.
 * A ParticleRenderer is used to apply graphics on this point/
 */
public class Particle
{
	private Vector2f position, dimension, force;
	public Animation animation;
	private BufferedImage texture;
	private Color color;
	private int shape;
	private int maxLife, life;
	private float lifeVar, colorVar, xVar, yVar, widthVar, heightVar;
	
	public Particle(float centerX, float xVar, float centerY, float yVar, float width, float widthVar, float height, float heightVar, int maxLife, float lifeVar, Color color, float colorVar, int shape)
	{
		this.lifeVar = lifeVar;
		this.colorVar = colorVar;
		this.xVar = xVar;
		this.yVar = yVar;
		this.widthVar = widthVar;
		this.heightVar = heightVar;
		this.position = new Vector2f(centerX + Mathf.random(-xVar, xVar), centerY + Mathf.random(-yVar, yVar));
		this.dimension = new Vector2f(width + Mathf.random(-widthVar, widthVar), height + Mathf.random(-heightVar, heightVar));
		this.force = new Vector2f();
		this.maxLife = (int)Mathf.abs((maxLife + Mathf.random(-lifeVar, lifeVar)));
		this.life = this.maxLife;
		if(colorVar != 0)
		{
			int r = (int)Mathf.minAndMax(color.getRed() + Mathf.random(-colorVar, colorVar), 0, 255);
			int g = (int)Mathf.minAndMax(color.getGreen() + Mathf.random(-colorVar, colorVar), 0, 255);
			int b = (int)Mathf.minAndMax(color.getBlue() + Mathf.random(-colorVar, colorVar), 0, 255);
			color = new Color(r, g, b);
		}
		this.color = color;
		this.shape = shape;
	}

	public Particle(float centerX, float centerY, float width, float height, int maxLife, Color color, int shape)
	{
		this(centerX, 0, centerY, 0, width, 0, height, 0, maxLife, 0, color, 0, shape);
	}

	public Particle(float centerX, float xVar, float centerY, float yVar, float width, float widthVar, float height, float heightVar, int maxLife, float lifeVar, BufferedImage image, int shape)
	{
		this.lifeVar = lifeVar;
		this.xVar = xVar;
		this.yVar = yVar;
		this.widthVar = widthVar;
		this.heightVar = heightVar;
		this.position = new Vector2f(centerX + Mathf.random(-xVar, xVar), centerY + Mathf.random(-yVar, yVar));
		this.dimension = new Vector2f(width + Mathf.random(-widthVar, widthVar), height + Mathf.random(-heightVar, heightVar));
		this.force = new Vector2f();
		this.maxLife = (int)Mathf.abs((maxLife + Mathf.random(-lifeVar, lifeVar)));
		this.life = this.maxLife;
		this.texture = image;
		this.shape = shape;
	}

	public Particle(float centerX, float centerY, float width, float height, int maxLife, BufferedImage image, int shape)
	{
		this(centerX, 0, centerY, 0, width, 0, height, 0, maxLife, 0, image, shape);
	}

	public Particle(float centerX, float xVar, float centerY, float yVar, float width, float widthVar, float height, float heightVar, int maxLife, float lifeVar, Animation animation, int shape)
	{
		this.lifeVar = lifeVar;
		this.xVar = xVar;
		this.yVar = yVar;
		this.widthVar = widthVar;
		this.heightVar = heightVar;
		this.position = new Vector2f(centerX + Mathf.random(-xVar, xVar), centerY + Mathf.random(-yVar, yVar));
		this.dimension = new Vector2f(width + Mathf.random(-widthVar, widthVar), height + Mathf.random(-heightVar, heightVar));
		this.force = new Vector2f();
		this.maxLife = (int)Mathf.abs((maxLife + Mathf.random(-lifeVar, lifeVar)));
		this.life = this.maxLife;
		this.animation = animation;
		this.shape = shape;
	}

	public Particle(float centerX, float centerY, float width, float height, int maxLife, Animation animation, int shape)
	{
		this(centerX, 0, centerY, 0, width, 0, height, 0, maxLife, 0, animation, shape);
	}

	public Particle createNew(float x, float y)
	{
		Particle particle;
		if(hasAnimation())
			particle = new Particle(x, y, dimension.x, dimension.y, maxLife, animation, shape);
		else if(hasTexture())
			particle = new Particle(x, y, dimension.x, dimension.y, maxLife, texture, shape);
		else
			particle = new Particle(x, y, dimension.x, dimension.y, maxLife, color, shape);
		particle.applyForce(force);
		// .mul(Mathf.random(0.5f, 1.5f), Mathf.random(0.5f, 1.5f)));
		return particle;
	}

	public void update()
	{
//		float distance = new Vector2f(ParticleSystem.holePosition.x - position.x, ParticleSystem.holePosition.y - position.y).length();
		// this.applyForce(ParticleSystem.holePosition.mul(1 / distance));
		// this.applyForce(new Vector2f(0, 9.8f));
		this.position.add(force);
		// 1 - friction
		this.force.mul(0.9f);
		--this.life;
	}

	public Particle applyRepulsion(Vector2f origin, Vector2f force)
	{
		Vector2f direction = origin.sub(position, new Vector2f()).normalize().negate();
		applyForce(direction.mul(force));
		return this;
	}

	public Particle applyAttraction(Vector2f origin, Vector2f force)
	{
		Vector2f direction = origin.sub(position, new Vector2f()).normalize();
		applyForce(direction.mul(force));
		return this;
	}

	public Particle applyForce(Vector2f force)
	{
		this.force.add(force);
		return this;
	}

	public Particle applyForce(float forceX, float forceY)
	{
		this.force.add(forceX, forceY);
		return this;
	}

	public Particle applyForce(float force)
	{
		this.force.add(force, force);
		return this;
	}

	public void stop()
	{
		this.force.set(0, 0);
	}

	public BufferedImage getTexture()
	{
		return texture;
	}

	public Animation getAnimation()
	{
		return animation;
	}

	public boolean hasAnimation()
	{
		return animation != null;
	}

	public boolean hasTexture()
	{
		return texture != null;
	}

	public int getRemainingLife()
	{
		return life;
	}

	public boolean isAlive()
	{
		return life > 0;
	}

	public Vector2f getPosition()
	{
		return position;
	}

	public Vector2f getDimension()
	{
		return dimension;
	}

	public Vector2f getForce()
	{
		return force;
	}

	public int getLife()
	{
		return life;
	}

	public int getMaxLife()
	{
		return maxLife;
	}

	public int getShape()
	{
		return shape;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public Particle setForce(float force)
	{
		this.force = new Vector2f(0, 0);
		return this;
	}

	public Rectangle getBounds()
	{
		return new Rectangle((int)(position.x + dimension.x / 2), (int)(position.y + dimension.y / 2), (int)dimension.x, (int)dimension.y);
	}
}
