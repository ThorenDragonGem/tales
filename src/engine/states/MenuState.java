package engine.states;

import java.awt.Graphics;

import engine.assets.Assets;
import engine.physics.Handler;
import engine.uis.ClickListener;
import engine.uis.UIImageButton;
import engine.uis.UIManager;

public class MenuState extends State
{
	private UIManager manager;

	public MenuState(Handler handler)
	{
		super(handler);
		manager = new UIManager(handler);
		handler.getMouse().setUiManager(manager);
		manager.addObject(new UIImageButton(200, 200, 128, 64, Assets.menuStart, new ClickListener()
		{
			@Override
			public void onClick()
			{
				MenuState.this.handler.getMouse().setUiManager(null);
				State.setState(MenuState.this.handler.getGame().gameState);
			}
		}));
	}

	@Override
	public void update()
	{
		manager.update();
	}

	@Override
	public void render(Graphics graphics)
	{
		manager.render(graphics);
	}
}
