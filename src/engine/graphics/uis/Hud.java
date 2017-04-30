package engine.graphics.uis;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import engine.assets.Assets;
import engine.entities.stats.Stats;
import engine.physics.Handler;
import engine.utils.ColorFading;
import engine.utils.Mathf;
import engine.utils.Text;

public class Hud
{
	private ColorFading.FadeColor[] healthFadeColors = new ColorFading.FadeColor[]
	{ new ColorFading.FadeColor(0.0f, new java.awt.Color(200, 25, 28)), new ColorFading.FadeColor(1.0f, new java.awt.Color(28, 200, 25)), new ColorFading.FadeColor(0.5f, new java.awt.Color(255, 165, 0)) };
	private ColorFading.FadeColor[] manaFadeColors = new ColorFading.FadeColor[]
	{ new ColorFading.FadeColor(0.0f, new java.awt.Color(25, 28, 200)), new ColorFading.FadeColor(1.0f, new java.awt.Color(30, 144, 255)) };
	private Handler handler;
	private boolean active = true;
	private List<Stats> statsToShow;
	private int x, y, width, height;

	public Hud(Handler handler)
	{
		this.handler = handler;
		this.x = 0;
		this.y = 0;
	}


	public void render(Graphics graphics)
	{
		graphics.drawImage(Assets.healthHud, 200, 650, null);
		handler.getWorld().getEntityManager().getPlayer().getStats().health = Mathf.minimize(handler.getWorld().getEntityManager().getPlayer().getStats().health, 0);
		handler.getWorld().getEntityManager().getPlayer().getStats().mana = Mathf.minimize(handler.getWorld().getEntityManager().getPlayer().getStats().mana, 0);
		graphics.setColor(ColorFading.blendColors(healthFadeColors, handler.getWorld().getEntityManager().getPlayer().getStats().health / handler.getWorld().getEntityManager().getPlayer().getStats().get("healthMax")));
		graphics.fillRoundRect(304, 661, (int)(749 * handler.getWorld().getEntityManager().getPlayer().getStats().health / handler.getWorld().getEntityManager().getPlayer().getStats().get("healthMax")), 19, 10, 10);
		graphics.setColor(new Color(0.753f, 0.753f, 0.753f, 0.9f));
		graphics.fillRoundRect(304, 661, (int)(749 * handler.getWorld().getEntityManager().getPlayer().getStats().shield / handler.getWorld().getEntityManager().getPlayer().getStats().get("shieldMax")), 19, 10, 10);
		// graphics.drawImage(Assets.healtHud2, 262, 660, null);
		graphics.setColor(ColorFading.blendColors(manaFadeColors, handler.getWorld().getEntityManager().getPlayer().getStats().mana / handler.getWorld().getEntityManager().getPlayer().getStats().get("manaMax")));
		graphics.fillRoundRect(304, 689, (int)(749 * handler.getWorld().getEntityManager().getPlayer().getStats().mana / handler.getWorld().getEntityManager().getPlayer().getStats().get("manaMax")), 19, 10, 10);
		Text.drawStringNegate(graphics, "+" + Text.getThousandsStringd(handler.getWorld().getEntityManager().getPlayer().getStats().get("regen")), 1043, 675, Color.white, Assets.font18);
		Text.drawStringNegate(graphics, "+" + Text.getThousandsStringd(handler.getWorld().getEntityManager().getPlayer().getStats().get("manaRegen")), 1043, 703, Color.white, Assets.font18);
		Text.drawString(graphics, Integer.toString(handler.getWorld().getEntityManager().getPlayer().getStats().getLevel()), 273, 684, true, Color.white, Assets.font18);
		graphics.setColor(new Color(0xc00000));
		int width = (int)(handler.getWorld().getEntityManager().getPlayer().getStats().getXp() / handler.getWorld().getEntityManager().getPlayer().getStats().calculateNextLevelXp() * 54);
		graphics.fillRoundRect(216, 712 - width, 24, width, 10, 10);
		// graphics.drawImage(Assets.healtHud2, 262, 660 + 28, null);
	}
}
