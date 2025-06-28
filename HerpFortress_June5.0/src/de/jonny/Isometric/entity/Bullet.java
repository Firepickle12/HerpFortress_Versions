package de.jonny.Isometric.entity;

import de.jonny.Isometric.Bitmap;
import de.jonny.Isometric.unit.Unit;

public class Bullet extends Entity {
	public Unit owner;
	public double xo, yo, zo;
	
	public Bullet(Unit owner, double x, double y, double z, double xa, double ya, double za) {
		this.owner = owner;
		this.xo = this.x = x;
		this.yo = this.y = y;
		this.zo = this.z = z;
		this.xa = xa * 8;
		this.ya = ya * 8;
		this.za = za * 8;
	}
	
	public boolean blocks(Entity e) {
		if (e == owner) return false;
		return true;
	}
	
	public void tick() {
		xo = x;
		yo = y;
		zo = z;
		super.tick();
		attemptMove();
	}
	
	public void renderShadow(Bitmap b) {
		double xd = xo - x;
		double yd = yo - y;
		double zd = zo - z;
		int steps = (int) (Math.sqrt(xd * xd + yd * yd + zd * zd) + 1);
		for (int i = 0; i < steps; i++) {
			double zz = 0;
			b.setPixel((int) (x + xd * i / steps),(int) (y + yd * i / steps - zz), 1);
		}
	}
	
	public void render(Bitmap b) {
		double xd = xo - x;
		double yd = yo - y;
		double zd = zo - z;
		int steps = (int) (Math.sqrt(xd * xd + yd * yd + zd * zd) + 1);
		for (int i = 0; i < steps; i++) {
			double zz = z + zd * i / steps;
			b.setPixel((int) (x + xd * i / steps),(int) (y + yd * i / steps - zz), 0xffffffff);
		}
	}
	
	public void collide(Entity e, double xxa, double yya, double zza) {
		remove();
	}
}
