package engine.entities.creatures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import org.joml.Vector2f;

import engine.assets.Assets;
import engine.entities.Entity;
import engine.entities.stats.MonsterRank;
import engine.graphics.Animation;
import engine.graphics.uis.Hud;
import engine.inputs.CircleShapeParticles;
import engine.inputs.Inventory;
import engine.inputs.Particle;
import engine.physics.Handler;
import engine.utils.Mathf;

public class Player extends Creature
{
	private Animation animDown, animUp, animLeft, animRigth;
	private long lastAttackTimer, attackCoolDown = 100, attackTimer = attackCoolDown;
	private Inventory inventory;
	private Hud hud;
	private int direction = 0;

	public Player(Handler handler, float x, float y)
	{
		super(MonsterRank.BRONZE5, handler, x, y, DEFAULT_CREATURE_WIDTH, DEFAULT_CREATURE_HEIGHT);
		stats.getStat("healthMax").setBaseValue(100000);
		stats.getStat("shieldMax").setBaseValue(10000);
		stats.shield = stats.get("shieldMax");
		stats.getStat("regen").setBaseValue(1000d);
		stats.initAll();
		body.getBounds().x = 16;
		body.getBounds().y = 32;
		body.getBounds().width = 32;
		body.getBounds().height = 32;

		animDown = new Animation(200, Assets.playerDown);
		animUp = new Animation(200, Assets.playerUp);
		animLeft = new Animation(200, Assets.playerLeft);
		animRigth = new Animation(200, Assets.playerRight);

		inventory = new Inventory(handler);
		hud = new Hud(handler);
		stats.setXp(200);
	}

	int index = 0;
	@Override
	public void update()
	{
		super.update();

//		List<GridCell> pathToEnd = handler.getWorld().getFinder().findPath(handler.getWorld().getNavGrid().getCell((int)body.getPosition().x / Tile.TILEWIDTH, (int)(body.getPosition().y / Tile.TILEHEIGHT)), handler.getWorld().getNavGrid().getCell(5, 5), handler.getWorld().getNavGrid());
//		System.out.println(pathToEnd);
		animDown.update();
		animUp.update();
		animLeft.update();
		animRigth.update();

//		body.update();
//		xMove = body.getForce().x;
//		yMove = body.getForce().y;
//		if(pathToEnd == null)
//			return;
//		if(index < pathToEnd.size() - 1 && pathToEnd.get(index) != null)
//		{
//			Vector2f direction = new Vector2f(pathToEnd.get(index).x - body.getPosition().x, pathToEnd.get(index).y - body.getPosition().y).normalize();
//			xMove = body.applyForce(direction.x, 0).x;
//			yMove = body.applyForce(0, direction.y).y;
//		}
//		if(body.getPosition().equals(new Vector2f(pathToEnd.get(index).x, pathToEnd.get(index).y)))
//			index++;
//		if(index > pathToEnd.size())
//			index = 0;

		getInput();

		move();
		handler.getCamera().centerOnEntity(this);

		checkAttacks();
	}

	@Override
	public void postUpdate()
	{
		super.postUpdate();
		inventory.update();
	}

	@Override
	public void render(Graphics graphics)
	{
		super.render(graphics);
		graphics.drawImage(getCurrentAnimationFrame(), (int)(body.getPosition().x - handler.getCamera().getxOffset()), (int)(body.getPosition().y - handler.getCamera().getyOffset()), body.getWidth(), body.getHeight(), null);
		graphics.setColor(Color.red);
		graphics.fillRect((int)(body.getPosBounds().x - handler.getCamera().getxOffset()), (int)(body.getPosBounds().y - handler.getCamera().getyOffset()), (body.getPosBounds().width), (body.getPosBounds().height));
	}

	public void postRender(Graphics graphics)
	{
		// inventory drawing
		inventory.render(graphics);
		// hud drawing;
		hud.render(graphics);
	}

	@Override
	public void die()
	{
		super.die();
		System.out.println("You Loose");
		inventory.dropAll();
	}

	private void checkAttacks()
	{
		attackTimer += System.currentTimeMillis() - lastAttackTimer;
		lastAttackTimer = System.currentTimeMillis();
		if(attackTimer < attackCoolDown)
			return;

		if(inventory.isActive())
			return;

		Rectangle cb = getCollisionBounds(0, 0);
		Rectangle ar = new Rectangle();
		int arSize = 20;
		ar.width = arSize;
		ar.height = arSize;
		if(handler.getKey().aUp)
		{
			ar.x = cb.x + cb.width / 2 - arSize / 2;
			ar.y = cb.y - arSize;
		}
		else if(handler.getKey().aDown)
		{
			ar.x = cb.x + cb.width / 2 - arSize / 2;
			ar.y = cb.y + cb.height;
		}
		else if(handler.getKey().aLeft)
		{
			ar.x = cb.x - arSize;
			ar.y = cb.y + cb.height / 2 - arSize / 2;
		}
		else if(handler.getKey().aRight)
		{
			ar.x = cb.x + cb.width;
			ar.y = cb.y + cb.height / 2 - arSize / 2;
		}
		else
		{
			return;
		}

		attackTimer = 0;

		for(Entity e : handler.getWorld().getEntityManager().getEntities())
		{
			if(e == this)
				continue;
			if(e.getCollisionBounds(0, 0).intersects(ar))
			{
				stats.target = e;
				stats.physicalDamages(10);
				return;
			}
		}
	}

	Animation partAnim = new Animation(150, Assets.fire);

	private void getInput()
	{
		boolean moveMouse = false, moveKeys = false;
		body.update();
		xMove = body.getForce().x;
		yMove = body.getForce().y;

		if(inventory.isActive())
			return;

		if(handler.getKey().isKeyDown(KeyEvent.VK_U))
//			handler.getWorld().getParticleSystem().addParticleShape(new ConeParticules(handler, body.getPosition(), 20, new Vector2f(handler.getMouse().getMouseX(), handler.getMouse().getMouseY()), 90, 100, new Particle(0, 1, 0, 1, 64, 10, 64, 10, 200, 20, partAnim, 1)).applyRepulsion(20, 20));
			handler.getWorld().getParticleSystem().addParticleShape(new CircleShapeParticles(handler, new Vector2f(body.getPosition().x + body.getWidth() / 2, body.getPosition().y + body.getHeight() / 2), 20, 30, new Particle(0, 1, 0, 1, 64, 10, 64, 10, 60 * 20, 20, partAnim, 1)).applyRepulsion(20, 20));
//			handler.getWorld().getParticleSystem().addParticle(new Particle(500, 300, 100, 100, 200, partAnim, 0).applyForce(new Vector2f(Mathf.random(-1, 1) * 100f, Mathf.random(-1, 1) * 100f)), 1, 100, 100);
		
		if(handler.getMouse().isButtonDown(MouseEvent.BUTTON3))
		{
			moveMouse = true;
			float pathX = handler.getCamera().getPickRay(handler.getMouse().getMouseX(), handler.getMouse().getMouseY()).origin.x - (body.getPosition().x + body.getWidth() / 2);
			float pathY = handler.getCamera().getPickRay(handler.getMouse().getMouseX(), handler.getMouse().getMouseY()).origin.y - (body.getPosition().y + body.getHeight() / 2);
			float distance = Mathf.sqrt(pathX * pathX + pathY * pathY);
			distance = Mathf.minimize(distance, 10f);
			float dirX = pathX / distance / 2;
			float dirY = pathY / distance / 2;
			if(dirY > -0.25f && dirY < 0.25f)
			{
				if(dirX < 0)
					direction = 2;
				else
					direction = 3;
			}
			else if(dirX > -0.5f && dirX < 0.5f)
			{
				if(dirY > 0)
					direction = 0;
				else
					direction = 1;
			}
			xMove = body.applyForce(dirX, 0).x;
			yMove = body.applyForce(0, dirY).y;
			move();
		}
		
		if(moveMouse)
			return;

		float pathX, pathY, distance, dirX, dirY;
		pathX = 0;
		pathY = 0;
		if(handler.getKey().up)
		{
			// yMove = (float)(-speed * 300 * handler.getGame().getDelta());
			pathY += (float)-stats.get("velocity");
//			yMove = body.applyForce(0, (float)-stats.get("velocity")).y;
			direction = 1;
		}
		if(handler.getKey().down)
		{
			// yMove = (float)(speed * 300 * handler.getGame().getDelta());
			pathY += (float)stats.get("velocity");
			// yMove = body.applyForce(0, (float)stats.get("velocity")).y;
			direction = 0;
		}
		if(handler.getKey().left)
		{
			// xMove = (float)(-speed * 300 * handler.getGame().getDelta());
			pathX += (float)-stats.get("velocity");
			// xMove = body.applyForce((float)-stats.get("velocity"), 0).x;
			direction = 2;
		}
		if(handler.getKey().right)
		{
			// xMove = (float)(speed * 300 * handler.getGame().getDelta());
			pathX += (float)stats.get("velocity");
			// xMove = body.applyForce((float)stats.get("velocity"), 0).x;
			direction = 3;
		}
		distance = Mathf.sqrt(pathX * pathX + pathY * pathY);
		distance = Mathf.minimize(distance, 1f);
		dirX = pathX / distance / 2;
		dirY = pathY / distance / 2;

		xMove = body.applyForce(dirX, 0).x;
		yMove = body.applyForce(0, dirY).y;
		move();

		// if(instance.getInput().getKey(KeyEvent.VK_Z))
		// yMove = (float)(-speed * 300 * instance.getDelta());
		// if(instance.getInput().getKey(KeyEvent.VK_S))
		// yMove = (float)(speed * 300 * instance.getDelta());
		// if(instance.getInput().getKey(KeyEvent.VK_Q))
		// xMove = (float)(-speed * 300 * instance.getDelta());
		// if(instance.getInput().getKey(KeyEvent.VK_D))
		// xMove = (float)(speed * 300 * instance.getDelta());
	}

	private BufferedImage getCurrentAnimationFrame()
	{
		switch(direction)
		{
			case 0:
				return animDown.getCurrentFrame();
			case 1:
				return animUp.getCurrentFrame();
			case 2:
				return animLeft.getCurrentFrame();
			case 3:
				return animRigth.getCurrentFrame();
			default:
				return animDown.getCurrentFrame();
		}

	}

	public Inventory getInventory()
	{
		return inventory;
	}
}
