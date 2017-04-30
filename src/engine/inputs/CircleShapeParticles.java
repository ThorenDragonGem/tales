package engine.inputs;

import org.joml.Vector2f;

import engine.physics.Handler;
import engine.utils.Mathf;

public class CircleShapeParticles extends ParticlesShape
{
	private Particle type;
	private float radius;

	public CircleShapeParticles(Handler handler, Vector2f position, float radius, int particleCount, Particle type)
	{
		super(handler, position, particleCount);
		this.radius = radius;
		this.type = type;
		createShape();
	}

	@Override
	public void createShape()
	{
		batch = new ParticlePool(maxCount);
		float angle = -180;
		while(count < maxCount)
		{
			Vector2f partPosition = new Vector2f(position.x + Mathf.sin(Mathf.toRadians(angle)) * radius, position.y + Mathf.cos(Mathf.toRadians(angle)) * radius);
//			batch.addParticle(new Particle(partPosition.x + 5, partPosition.y + 5, 10, 10, 1000, Color.red, 0).applyForce(partPosition.sub(position, new Vector2f()).normalize()).setForce(0));
			batch.addParticle(type.createNew(partPosition.x, partPosition.y).applyForce(partPosition.sub(position.sub(type.getDimension().mul(1 / 2, new Vector2f()), new Vector2f()), new Vector2f()).normalize()).setForce(0));
			angle += 360f / maxCount;
			++count;
		}
	}
}
