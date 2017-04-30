package engine.inputs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.joml.Vector2f;

public class ParticlePool
{
	private List<Particle> pool;
	private int aliveCount;
	private int totalCount;
	private Comparator<Particle> particleSorter = new Comparator<Particle>()
	{
		@Override
		public int compare(Particle o1, Particle o2)
		{
			if(o1.isAlive() && !o2.isAlive())
				return -1;
			 if(o2.isAlive() && !o1.isAlive())
				return 1;
			 return 0;
		};
	};

	public ParticlePool(int batchMaxCount)
	{
		this.totalCount = batchMaxCount;
		this.aliveCount = 0;
		this.pool = new ArrayList<Particle>(batchMaxCount);
	}

	public ParticlePool()
	{
		this(1000);
	}

	public void update(List<Vector2f> globalForces)
	{
		if(aliveCount == 0 || totalCount == 0)
			return;
		pool.sort(particleSorter);
		for(int i = 0; i < aliveCount; i++)
		{
			for(Vector2f force : globalForces)
				pool.get(i).applyForce(force);
			pool.get(i).update();
			if(!pool.get(i).isAlive())
				--aliveCount;
		}
	}

	public void addParticle(Particle particle)
	{
		if(isFull())
			return;
		if(!pool.contains(particle))
		{
			pool.add(particle);
			if(particle.isAlive())
				aliveCount++;
		}
	}

	public void removeParticle(Particle particle)
	{
		if(pool.contains(particle))
			pool.remove(particle);
	}

	@Override
	public String toString()
	{
		String string = "[";
		for(Particle particle : pool)
		{
			if(pool == null)
				continue;
			string += "P: " + particle.isAlive() + " , " + particle.getPosition() + " ; ";
		}
		return string.substring(0, string.length() - 3) + "]";
	}

	public List<Particle> getPool()
	{
		return pool;
	}

	public int getTotalCount()
	{
		return totalCount;
	}

	public int getAliveCount()
	{
		return aliveCount;
	}

	public boolean isFull()
	{
		return pool.size() == totalCount;
	}

	public boolean isAllDead()
	{
		for(Particle particle : pool)
			if(particle.isAlive())
				return false;
		return true;
	}
}
