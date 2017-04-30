package engine.inputs;

import org.joml.Vector2f;

import engine.physics.Handler;
import engine.utils.Mathf;

public class ConeParticules extends ParticlesShape
{
	private Vector2f direction;
	private float gap, radius;
	private Particle type;

	public ConeParticules(Handler handler, Vector2f position, int maxCount, Vector2f direction, float gap, float radius, Particle type)
	{
		super(handler, position, maxCount);
		this.direction = direction;
		this.gap = gap;
		this.radius = radius;
		this.type = type;
		System.out.println(direction);
		createShape();
	}

	@Override
	public void createShape()
	{
		if(direction.x == 0 && direction.y == 0)
			return;
		batch = new ParticlePool(maxCount);
		float a = new Vector2f(direction.x - position.x, 0).normalize().length();
		float angle = Mathf.acos(a / radius);
		while(count < maxCount)
		{
			Vector2f p = direction.sub(position, new Vector2f());
			Vector2f partPosition = new Vector2f(p.x + Mathf.sin(Mathf.toRadians(angle)) * radius, p.y + Mathf.cos(Mathf.toRadians(angle)) * radius);
			System.out.println(partPosition);
//			batch.addParticle(new Particle(partPosition.x + 5, partPosition.y + 5, 10, 10, 1000, Color.red, 0).applyForce(partPosition.sub(position, new Vector2f()).normalize()).setForce(0));
			batch.addParticle(type.createNew(partPosition.x, partPosition.y).applyForce(partPosition.sub(position.sub(type.getDimension().mul(1 / 2, new Vector2f()), new Vector2f()), new Vector2f()).normalize()).setForce(0));
			System.out.println((angle));
			angle += gap / maxCount;
			++count;
		}
	}
}
