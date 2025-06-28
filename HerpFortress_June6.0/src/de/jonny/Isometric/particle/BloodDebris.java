package de.jonny.Isometric.particle;

import de.jonny.Isometric.Bitmap;

public class BloodDebris extends Debris {
	public BloodDebris(double x, double y, double z) {
		super(x, y, z);
		
		drag = 0.96;
		bounce = 0.1;
		icon = 1;
	}
	
	public void renderShadow(Bitmap b) {
		int xp = (int) x;
		int yp = (int) y;
		b.fill(xp, yp, xp, yp, 1);
	}
	
}
