package engine.inputs;

import java.util.List;

import org.joml.Vector2f;

import engine.physics.Physics;
import engine.physics.geom.Circle;

public class ParticleRepulsor
{
	private Vector2f position, forceFromOrigin;
	private float radius;

	public ParticleRepulsor(Vector2f position, Vector2f forceFromOrigin, float radius)
	{
		this.position = position;
		this.forceFromOrigin = forceFromOrigin;
		this.radius = radius;
	}

	public void update()
	{
		List<Particle> particles = ParticleSystem.getSpherePools(new Circle(position, radius));
		for(Particle particle : particles)
		{
//			float distance = Physics.length(particle.getPosition().x, particle.getPosition().y, position.x, position.y);
			// if(distance > radius)
			// {
			// continue;
			// }
//			Vector2f direction = particle.getPosition().sub(position, new Vector2f());
			particle.applyForce(1, 1);
		}
	}

	public float getRadius()
	{
		return radius;
	}

	public Vector2f getForceFromOrigin()
	{
		return forceFromOrigin;
	}

	public Vector2f getPosition()
	{
		return position;
	}

	public void setPosition(Vector2f position)
	{
		this.position = position;
	}
}
