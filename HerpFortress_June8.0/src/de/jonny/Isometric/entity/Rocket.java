package de.jonny.Isometric.entity;

import de.jonny.Isometric.Art;
import de.jonny.Isometric.Bitmap;
import de.jonny.Isometric.particle.Debris;
import de.jonny.Isometric.particle.FlameDebris;
import de.jonny.Isometric.particle.SmokeDebris;
import de.jonny.Isometric.unit.Unit;

public class Rocket extends Entity {
	public Unit owner;
	public double xo, yo, zo;
	
	public Rocket(Unit owner, double x, double y, double z, double xa, double ya, double za) {
		this.owner = owner;
		this.xo = this.x = x;
		this.yo = this.y = y;
		this.zo = this.z = z;
		this.xa = xa * 2;
		this.ya = ya * 2;
		this.za = za * 2;
	}
	
	public boolean blocks(Entity e) {
		if (e == owner) return false;
		return true;
	}
	
	public void tick() {
		xo = x;
		yo = y;
		zo = z;
		
		FlameDebris flame = new FlameDebris(x - xa * 2, y - ya * 2, z - za * 2);
		flame.xa *= 0.1;
		flame.ya *= 0.1;
		flame.za *= 0.1;
		flame.xa += xa * 1;
		flame.ya += ya * 1;
		flame.za += za * 1;
		flame.life = flame.maxLife / 2;
		level.add(flame);
		
		super.tick();
		attemptMove();
	}
	
	public void renderShadow(Bitmap b) {
		int xp = (int) x;
		int yp = (int) y;
		b.fill(xp - 1, yp, xp, yp, 1);
	}
	
	public void render(Bitmap b) {
		int xp = (int) x;
		int yp = (int) (y - z);
		
		int frame = (int) (Math.floor(-Math.atan2(ya, xa) * 16 / (Math.PI * 2)) + 4.5) & 7;
		
		b.draw(Art.i.missles[frame][0], xp - 4, yp - 4);
	}
	
	public void collide(Entity e, double xxa, double yya, double zza) {
		level.explode(this, x, y, z);
		remove();
	}
}
