package engine.inputs;

import java.awt.Graphics;

import engine.physics.Handler;

public class ParticleRenderer
{
	private Handler handler;

	public ParticleRenderer(Handler handler)
	{
		this.handler = handler;
	}

	public void updateBatch(ParticlePool batch)
	{
		for(int i = 0; i < batch.getAliveCount(); i++)
		{
			if(batch.getPool().get(i).getPosition().x + batch.getPool().get(i).getDimension().x > handler.getCamera().getxOffset() && batch.getPool().get(i).getPosition().x - batch.getPool().get(i).getDimension().x < handler.getCamera().getxOffset() + handler.getWidth())
			{
				if(batch.getPool().get(i).getPosition().y + batch.getPool().get(i).getDimension().y > handler.getCamera().getyOffset() && batch.getPool().get(i).getPosition().y - batch.getPool().get(i).getDimension().y < handler.getCamera().getyOffset() + handler.getHeight())
				{
					if(batch.getPool().get(i).animation != null)
						batch.getPool().get(i).animation.update();
				}
			}
		}
	}

	public void renderBatch(Graphics graphics, ParticlePool batch)
	{
		for(int i = 0; i < batch.getAliveCount(); i++)
		{
			if(batch.getPool().get(i).getPosition().x + batch.getPool().get(i).getDimension().x > handler.getCamera().getxOffset() && batch.getPool().get(i).getPosition().x - batch.getPool().get(i).getDimension().x < handler.getCamera().getxOffset() + handler.getWidth())
			{
				if(batch.getPool().get(i).getPosition().y + batch.getPool().get(i).getDimension().y > handler.getCamera().getyOffset() && batch.getPool().get(i).getPosition().y - batch.getPool().get(i).getDimension().y < handler.getCamera().getyOffset() + handler.getHeight())
				{
					// FOR(INT J = 0; J < BATCH.GETALIVECOUNT(); J++)
					// {
					// IF(BATCH.GETPOOL().GET(I).GETBOUNDS().INTERSECTS(BATCH.GETPOOL().GET(J).GETBOUNDS())
					// && I != J)
					// {
					// BATCH.GETPOOL().GET(I).SETCOLOR(NEW COLOR(0, 255, 0));
					// }
						renderParticle(graphics, batch.getPool().get(i));
						ParticleSystem.h++;
					// }
				}
			}
		}
	}

	public void renderParticle(Graphics graphics, Particle particle)
	{
		if(particle.hasAnimation())
		{
			graphics.drawImage(particle.getAnimation().getCurrentFrame(), (int)(particle.getPosition().x - particle.getDimension().x / 2 - handler.getCamera().getxOffset()), (int)(particle.getPosition().y - particle.getDimension().y / 2 - handler.getCamera().getyOffset()), (int)(particle.getDimension().x), (int)(particle.getDimension().y), null);
		}
		else if(particle.hasTexture())
		{
			graphics.drawImage(particle.getTexture(), (int)(particle.getPosition().x - particle.getDimension().x / 2 - handler.getCamera().getxOffset()), (int)(particle.getPosition().y - particle.getDimension().y / 2 - handler.getCamera().getyOffset()), (int)(particle.getDimension().x), (int)(particle.getDimension().y), null);
		}
		else if(particle.getColor() != null)
		{
			graphics.setColor(particle.getColor());
			switch(particle.getShape())
			{
				case 0:
					graphics.fillRect((int)(particle.getPosition().x - particle.getDimension().x / 2 - handler.getCamera().getxOffset()), (int)(particle.getPosition().y - particle.getDimension().y / 2 - handler.getCamera().getyOffset()), (int)particle.getDimension().x, (int)particle.getDimension().y);
					return;
				case 1:
					graphics.fillOval((int)(particle.getPosition().x - particle.getDimension().x / 2 - handler.getCamera().getxOffset()), (int)(particle.getPosition().y - particle.getDimension().y / 2 - handler.getCamera().getyOffset()), (int)particle.getDimension().x, (int)particle.getDimension().y);
					return;
				default:
					graphics.fillRect((int)(particle.getPosition().x - particle.getDimension().x / 2 - handler.getCamera().getxOffset()), (int)(particle.getPosition().y - particle.getDimension().y / 2 - handler.getCamera().getyOffset()), (int)particle.getDimension().x, (int)particle.getDimension().y);
					return;
			}
		}
	}
}
