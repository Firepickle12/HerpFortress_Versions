package de.jonny.Isometric.level;

import de.jonny.Isometric.Bitmap;
import de.jonny.Isometric.Sprite;

public class MapSprite extends Sprite {
	public Bitmap bitmap;
	
	public MapSprite(Bitmap bitmap, double x, double y) {
		this.bitmap = bitmap;
		this.x = x + 8;
		this.y = y + 8;
	}
	
	public void render(Bitmap b, int xp, int yp) {
		b.draw(bitmap, xp - 12, yp - 18);
	}
}
