package de.jonny.Isometric.entity;

import de.jonny.Isometric.Art;
import de.jonny.Isometric.Bitmap;
import de.jonny.Isometric.particle.Debris;
import de.jonny.Isometric.particle.FlameDebris;
import de.jonny.Isometric.particle.SmokeDebris;
import de.jonny.Isometric.unit.Unit;
import de.jonny.Isometric.weapon.Weapon;

public class Rocket extends Bullet {
	public Unit owner;
	public double xo, yo, zo;
	
	public Rocket(Unit owner, Weapon weapon, double xa, double ya, double za, int dmg) {
		super(owner, weapon, xa, ya, za, dmg);
		this.owner = owner;
		this.xa = xa * 1;
		this.ya = ya * 1;
		this.za = za * 1;
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
	
	public void renderShadow(Bitmap b, int xp, int yp) {
		b.fill(xp - 1, yp, xp, yp, 1);
	}
	
	public void render(Bitmap b, int xp, int yp) {
		int frame = (int) (Math.floor(-Math.atan2(ya, xa) * 16 / (Math.PI * 2)) + 4.5) & 7;
		b.draw(Art.i.missles[frame][0], xp - 4, yp - 4);
	}
	
	public void collide(Entity e, double xxa, double yya, double zza) {
		level.explode(this, x, y, z, 125, 25);
		remove();
	}
}
