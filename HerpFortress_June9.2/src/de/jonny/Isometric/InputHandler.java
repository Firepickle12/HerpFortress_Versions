package de.jonny.Isometric;

import java.awt.Canvas;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandler implements MouseListener, MouseMotionListener {
	private int xm, ym;
	private boolean mb0, mb1, mb2;
	private boolean onScreen;
	private Mouse mouse = new Mouse();
	private Canvas canvas;
	
	
	public InputHandler(Canvas canvas) {
		this.canvas = canvas;
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		xm = me.getX();
		ym = me.getY();
		onScreen = true;
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		xm = me.getX();
		ym = me.getY();
		onScreen = true;
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		xm = me.getX();
		ym = me.getY();
		onScreen = true;
	}

	@Override
	public void mouseEntered(MouseEvent me) {
		xm = me.getX();
		ym = me.getY();
		onScreen = true;
	}

	@Override
	public void mouseExited(MouseEvent me) {
		xm = me.getX();
		ym = me.getY();
		onScreen = false;
	}

	@Override
	public void mousePressed(MouseEvent me) {
		xm = me.getX();
		ym = me.getY();
		onScreen = true;
		if (me.getButton() == MouseEvent.BUTTON1) mb0 = true;
		if (me.getButton() == MouseEvent.BUTTON3) mb1 = true;
		if (me.getButton() == MouseEvent.BUTTON2) mb2 = true;
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		xm = me.getX();
		ym = me.getY();
		onScreen = xm >= 0 && ym >= 0 && xm < canvas.getWidth() && ym < canvas.getHeight();
		if (me.getButton() == MouseEvent.BUTTON1) mb0 = false;
		if (me.getButton() == MouseEvent.BUTTON3) mb1 = false;
		if (me.getButton() == MouseEvent.BUTTON2) mb2 = false;
	}
	
	public synchronized Mouse updateMouseStatus(int scale) {
		mouse.update(xm / scale, ym / scale, mb0, mb1, mb2, onScreen);
		return mouse;
	}
	
}
