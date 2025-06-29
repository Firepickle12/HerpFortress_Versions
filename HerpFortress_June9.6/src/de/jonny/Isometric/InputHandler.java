package de.jonny.Isometric;

import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandler implements MouseListener, MouseMotionListener, KeyListener {
	private int xm, ym;
	private boolean mb0, mb1, mb2;
	private boolean onScreen;
	private Input input = new Input();
	private Canvas canvas;
	private boolean[] keysDown = new boolean[655536];
	private String typed;
	
	
	public InputHandler(Canvas canvas) {
		this.canvas = canvas;
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addKeyListener(this);
	}

	@Override
	public synchronized void mouseDragged(MouseEvent me) {
		xm = me.getX();
		ym = me.getY();
		onScreen = true;
	}

	@Override
	public synchronized void mouseMoved(MouseEvent me) {
		xm = me.getX();
		ym = me.getY();
		onScreen = true;
	}

	@Override
	public synchronized void mouseClicked(MouseEvent me) {
		xm = me.getX();
		ym = me.getY();
		onScreen = true;
	}

	@Override
	public synchronized void mouseEntered(MouseEvent me) {
		xm = me.getX();
		ym = me.getY();
		onScreen = true;
	}

	@Override
	public synchronized void mouseExited(MouseEvent me) {
		xm = me.getX();
		ym = me.getY();
		onScreen = false;
	}

	@Override
	public synchronized void mousePressed(MouseEvent me) {
		xm = me.getX();
		ym = me.getY();
		onScreen = true;
		if (me.getButton() == MouseEvent.BUTTON1) mb0 = true;
		if (me.getButton() == MouseEvent.BUTTON3) mb1 = true;
		if (me.getButton() == MouseEvent.BUTTON2) mb2 = true;
	}

	@Override
	public synchronized void mouseReleased(MouseEvent me) {
		xm = me.getX();
		ym = me.getY();
		onScreen = xm >= 0 && ym >= 0 && xm < canvas.getWidth() && ym < canvas.getHeight();
		if (me.getButton() == MouseEvent.BUTTON1) mb0 = false;
		if (me.getButton() == MouseEvent.BUTTON3) mb1 = false;
		if (me.getButton() == MouseEvent.BUTTON2) mb2 = false;
	}
	
	public synchronized Input updateMouseStatus(int scale) {
		input.update(xm / scale, ym / scale, mb0, mb1, mb2, onScreen, keysDown, typed);
		return input;
	}

	@Override
	public synchronized void keyPressed(KeyEvent ke) {
		if (ke.getKeyCode() > 0 && ke.getKeyCode() < keysDown.length) keysDown[ke.getKeyCode()] = true;
	}

	@Override
	public synchronized void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() > 0 && ke.getKeyCode() < keysDown.length) keysDown[ke.getKeyCode()] = false;
	}

	@Override
	public synchronized void keyTyped(KeyEvent ke) {
		typed += ke.getKeyChar();
	}
	
}
