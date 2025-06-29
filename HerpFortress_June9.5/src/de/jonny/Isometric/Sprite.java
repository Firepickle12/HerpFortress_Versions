package de.jonny.Isometric;

import de.jonny.Isometric.level.Level;

public class Sprite {
	public boolean removed;

	public double x, y, z;
	public double xr = 2;
	public double yr = 2;
	public double zh = 5;

	public Level level;

	public void init(Level level) {
		init();
		this.level = level;
	}

	public void init() {

	}

	public void tick() {

	}

	public void remove() {
		removed = true;
	}

	public void render(Bitmap b) {
		int xp = (int) x;
		int yp = (int) (y - z);
		render(b, xp, yp);
	}
	
	public void render(Bitmap b, int xp, int yp) {
	}

	public void renderShadow(Bitmap b) {
		int xp = (int) x;
		int yp = (int) y;
		renderShadow(b, xp, yp);
	}

	public void renderShadow(Bitmap b, int xp, int yp) {
	}

}
