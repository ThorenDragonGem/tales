package engine.inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import engine.uis.UIManager;

public class MouseManager implements MouseListener, MouseMotionListener, MouseWheelListener
{
	private boolean leftPressed, rightPressed;
	private int mouseX, mouseY;
	private UIManager uiManager;
	private int scrollY;
	private boolean[] buttons;

	public MouseManager()
	{
		buttons = new boolean[5];
	}

	public void setUiManager(UIManager uiManager)
	{
		this.uiManager = uiManager;
	}

	public boolean isLeftPressed()
	{
		return leftPressed;
	}

	public boolean isRightPressed()
	{
		return rightPressed;
	}

	public boolean isButtonDown(int button)
	{
		return buttons[button];
	}

	public int getMouseX()
	{
		return mouseX;
	}

	public int getMouseY()
	{
		return mouseY;
	}

	int sy;
	public int getScrollY()
	{
		int scroll = scrollY;
		sy++;
		if(sy % 2 == 0)
		{
			scrollY = 0;
			sy = 0;
		}
		return scroll;
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
		if(uiManager != null)
			uiManager.onMouseMove(e);
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
		if(uiManager != null)
			uiManager.onMouseMove(e);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		buttons[e.getButton()] = true;
		if(e.getButton() == MouseEvent.BUTTON1)
			leftPressed = true;
		else if(e.getButton() == MouseEvent.BUTTON3)
			rightPressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		buttons[e.getButton()] = false;
		if(e.getButton() == MouseEvent.BUTTON1)
			leftPressed = false;
		else if(e.getButton() == MouseEvent.BUTTON3)
			rightPressed = false;
		if(uiManager != null)
			uiManager.onMouseRelease(e);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		scrollY = e.getUnitsToScroll();
	}
}
