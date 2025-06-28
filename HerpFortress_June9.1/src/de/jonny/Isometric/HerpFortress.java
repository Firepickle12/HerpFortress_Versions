package de.jonny.Isometric;

import java.awt.*;
import java.awt.image.*;
import java.util.Random;

import javax.swing.JFrame;

import de.jonny.Isometric.entity.Bullet;
import de.jonny.Isometric.entity.Rocket;
import de.jonny.Isometric.level.Level;
import de.jonny.Isometric.particle.Explosion;
import de.jonny.Isometric.particle.SplatDebris;
import de.jonny.Isometric.unit.Soldier;
import de.jonny.Isometric.unit.Unit;

public class HerpFortress extends Canvas implements Runnable {
	private Random random = new Random();
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 3;
	
	private boolean keepRunning = true;
	private BufferedImage screenImage;
	
	private Bitmap screenBitmap;
	private Bitmap shadows;
	
	private InputHandler inputHandler;
	private Mouse mouse;
	private Level level;
	
	public HerpFortress() {
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
		
		inputHandler = new InputHandler(this);
	}
	
	public void start() {
		new Thread(this, "Game Thread").start();
	}
	
	public void stop() {
		keepRunning = false;
	}
	
	public void init() {
		Art.init();
		screenImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		screenBitmap = new Bitmap(screenImage);
		mouse = inputHandler.updateMouseStatus(SCALE);
		shadows = new Bitmap(WIDTH, HEIGHT);
		
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0,0), "invisible"));
		
		level = new Level(320, 240);
	}
	
	@Override
	public void run() {
		init();
		
		double nsPerFrame = 1000000000.0 / 60.0;
		double unprocessedTime = 0;
		double maxSkipFrames = 10;
		
		long lastTime = System.nanoTime();
		long lastFrameTime = System.currentTimeMillis();
		int frames = 0;
		
		while (keepRunning) {
			long now = System.nanoTime();
			double passedTime = (now - lastTime) / nsPerFrame;
			lastTime = now;
			
			if (passedTime < -maxSkipFrames) passedTime = -maxSkipFrames;
			if (passedTime > maxSkipFrames) passedTime = maxSkipFrames;
			
			unprocessedTime += passedTime;
			
			boolean render = false;
			while (unprocessedTime > 1) {
				unprocessedTime -= 1;
				mouse = inputHandler.updateMouseStatus(SCALE);
				tick();
				render = true;
			}
			render = true;
			if (render) {
				render(screenBitmap);
				frames ++;
			}
			
			if(System.currentTimeMillis() > lastFrameTime + 1000) {
				System.out.println(frames + " fps");
				lastFrameTime += 1000;
				frames = 0;
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			swap();
		}
	}
	
	
	private void swap() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(2);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		int screenW = getWidth();
		int screenH = getHeight();
		int w = WIDTH * SCALE;
		int h = HEIGHT * SCALE;
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenW, screenH);
		g.drawImage(screenImage, (screenW - w) / 2, (screenH - h) / 2, w, h, null);
		g.dispose();
		bs.show();
	}
	
	private void render(Bitmap screen) {
		shadows.clear(0);
		
		level.renderBg(screen);
		level.renderShadows(shadows);
		screen.shade(shadows);
		level.renderSprites(screen);
		
		if(mouse.onScreen) screen.draw(Art.i.cursors[0][0], mouse.x - 1, mouse.y - 1);
	}
	
	private void tick() {
		if (mouse.b0Clicked) {
			Unit unit = Unit.create(random.nextInt(9), 0);
			unit.x = mouse.x;
			unit.y = mouse.y;
			level.add(unit);
		}
		
		if (mouse.b1Clicked) {
			Unit unit = Unit.create(random.nextInt(9), 1);
			unit.x = mouse.x;
			unit.y = mouse.y;
			level.add(unit);
		}
		
		level.tick();
	}

	public static void main(String[] args) {
		HerpFortress gameComponent = new HerpFortress();
		
		JFrame frame = new JFrame("HerpFortress");
		frame.add(gameComponent);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		gameComponent.start();
	}

}
