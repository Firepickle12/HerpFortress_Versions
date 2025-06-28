package de.jonny.Isometric.particle;

import de.jonny.Isometric.Bitmap;

public class SmokeDebris extends Debris {
	public SmokeDebris(double x, double y, double z) {
		super(x, y, z);
		
		life /= 2;
		
		drag = 0.92;
		icon = random.nextInt(3) + 8;
		gravity = -0.02;
	}
	
	public void renderShadow(Bitmap b) {
	}
}
