package engine.entities.statics;

import engine.entities.Entity;
import engine.entities.stats.MonsterRank;
import engine.physics.Handler;

public abstract class StaticEntity extends Entity
{
	public StaticEntity(Handler handler, float x, float y, int width, int height)
	{
		super(MonsterRank.BRONZE1, handler, x, y, width, height);
	}
}
