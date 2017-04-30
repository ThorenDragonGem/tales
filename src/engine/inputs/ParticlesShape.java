package engine.inputs;

import org.joml.Vector2f;

import engine.physics.Handler;

public abstract class ParticlesShape
{
	protected Handler handler;
	protected ParticlePool batch;
	protected int maxCount, count;
	protected Vector2f position;

	public ParticlesShape(Handler handler, Vector2f position, int maxCount)
	{
		this.handler = handler;
		this.batch = new ParticlePool(maxCount);
		this.position = position;
		this.maxCount = maxCount;
		this.count = 0;
	}

	public abstract void createShape();

	public ParticlesShape applyRepulsion(float forceX, float forceY)
	{
		for(int i = 0; i < batch.getAliveCount(); i++)
			batch.getPool().get(i).applyRepulsion(position, new Vector2f(forceX, forceY));
		return this;
	}

	public ParticlesShape applyAttraction(float forceX, float forceY)
	{
		for(int i = 0; i < batch.getAliveCount(); i++)
			batch.getPool().get(i).applyAttraction(position, new Vector2f(forceX, forceY));
		return this;
	}

	public ParticlesShape applyForce(float forceX, float forceY)
	{
		for(Particle particle : batch.getPool())
			if(particle.isAlive())
				particle.applyForce(forceX, forceY);
		return this;
	}

	public ParticlesShape applyForce(float force)
	{
		for(Particle particle : batch.getPool())
			if(particle.isAlive())
				particle.applyForce(force);
		return this;
	}
}
