package de.jonny.Isometric;

import java.awt.*;
import java.awt.image.*;

import javax.swing.JFrame;

import de.jonny.Isometric.level.Level;
import de.jonny.Isometric.unit.Unit;

public class HerpFortress extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 3;
	
	private boolean keepRunning = true;
	private BufferedImage screenImage;
	
	private Bitmap screenBitmap;
	private Level level;
	
	public HerpFortress() {
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
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
		
		level = new Level(320, 240);
		
		// Hier werden verschiednene unit instanzen mit eingestellten Koordinaten erzeugt und in die Array Lists in Level.java gesteckt
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < 9; i++) {
				Unit unit = Unit.create(i, j);
				unit.x = i * 16 + 160 - 9 * 8;
				unit.y = 70 + j * 20;
				level.add(unit);
			}
		}
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
		// Color Screen
		screen.clear(0x505050);
		
		// Render Level
		level.render(screen);
	}
	
	public int step = 0;
	
	private void tick() {
		step ++;
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
