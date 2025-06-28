package de.jonny.Isometric.particle;

import de.jonny.Isometric.Bitmap;

public class SplatDebris extends Debris {
	public SplatDebris(double x, double y, double z) {
		super(x, y, z);
		drag = 0.98;
		life /= 4;
	}

	public void tick() {
		super.tick();
		if (random.nextInt(2) != 0) return;
		Debris blood = new BloodDebris(x, y, z);
		blood.xa *= 0.05;
		blood.ya *= 0.05;
		blood.za *= 0.05;
		blood.xa += xa * 0.5;
		blood.ya += ya * 0.5;
		blood.za += za * 0.5;
		level.add(blood);
	}

	public void render(Bitmap b) {
	}

	public void renderShadow(Bitmap b) {
	}
}
