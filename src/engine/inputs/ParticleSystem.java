package engine.inputs;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import engine.physics.Handler;
import engine.physics.Physics;
import engine.physics.geom.Circle;
import engine.utils.Mathf;

public class ParticleSystem
{
	// TODO: add global physic equations
	private Handler handler;
	private static List<ParticlePool> pools;
	private List<ParticlePool> toRemove;
	private List<Vector2f> globalForces;
	private ParticleRenderer renderer;

	public ParticleSystem(Handler handler)
	{
		this.handler = handler;
		this.pools = new ArrayList<ParticlePool>();
		this.toRemove = new ArrayList<ParticlePool>();
		this.globalForces = new ArrayList<Vector2f>();
		this.renderer = new ParticleRenderer(handler);
		// gravity
		// addGlobalForce(new Vector2f(0, 0.98f));
	}

	public void update()
	{
		for(ParticlePool pool : toRemove)
			if(pools.contains(pool))
				pools.remove(pool);
		toRemove.clear();
		for(ParticlePool pool : pools)
		{
			if(pool.isAllDead())
			{
				remove(pool);
				continue;
			}
			pool.update(globalForces);
			renderer.updateBatch(pool);
		}
	}

	public static int h = 0;
	public void render(Graphics graphics)
	{
		h = 0;
		for(ParticlePool pool : pools)
			renderer.renderBatch(graphics, pool);
		// System.out.println(h);
	}

	public void addGlobalForce(Vector2f force)
	{
		globalForces.add(force);
	}

	public void addAttractionForce(Vector2f position, Vector2f force, float attractionRadius)
	{
		
	}

	public void addRepulsionForce(Vector2f position, Vector2f force, float repulsionRadius)
	{

	}

	public void addPool(ParticlePool pool)
	{
		if(!pools.contains(pool))
			pools.add(pool);
	}

	public void addParticle(Particle particle)
	{
		if(pools.isEmpty())
		{
			ParticlePool pool = new ParticlePool();
			pool.addParticle(particle);
			addPool(pool);
			return;
		}
		else
		{
			for(ParticlePool pool : pools)
			{
				if(pool.isFull())
				{
					continue;
				}
				else
				{
					pool.addParticle(particle);
					return;
				}
			}
			ParticlePool pool2 = new ParticlePool();
			pool2.addParticle(particle);
			addPool(pool2);
		}
	}

	public void addParticle(Particle particle, int count, float dx, float dy)
	{
		for(int i = 0; i < count; i++)
			addParticle(particle.createNew(particle.getPosition().x + Mathf.random(-dx, dx), particle.getPosition().y + Mathf.random(-dx, dy)));
	}

	public void addParticleShape(ParticlesShape shape)
	{
		addPool(shape.batch);
	}

	public void remove(ParticlePool pool)
	{
		if(pools.contains(pool))
			toRemove.add(pool);
	}

	public List<ParticlePool> getPools()
	{
		return pools;
	}

	public static List<Particle> getSpherePools(Circle circle)
	{
		List<Particle> res = new ArrayList<Particle>();
		for(ParticlePool pool : pools)
			for(int i = 0; i < pool.getAliveCount(); i++)
				if(Physics.overlaps(circle, pool.getPool().get(i).getBounds()))
					res.add(pool.getPool().get(i));
		return res;
	}
}
