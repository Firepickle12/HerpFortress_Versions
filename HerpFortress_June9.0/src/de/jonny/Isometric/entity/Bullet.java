package de.jonny.Isometric.entity;

import de.jonny.Isometric.Bitmap;
import de.jonny.Isometric.unit.Unit;
import de.jonny.Isometric.weapon.Weapon;

public class Bullet extends Entity {
	public Unit owner;
	public Weapon weapon;
	public double xo, yo, zo;
	public double xStart, yStart, zStart;
	public int dmg;
	
	public Bullet(Unit owner, Weapon weapon, double xa, double ya, double za, int dmg) {
		this(owner, weapon, xa, ya, za, dmg, 4);
	}
	
	public Bullet(Unit owner, Weapon weapon, double xa, double ya, double za, int dmg, double speed) {
		this.owner = owner;
		this.weapon = weapon;
		this.xo = this.x = owner.x;
		this.yo = this.y = owner.y;
		this.zo = this.z = owner.z + 5;
		xStart = x;
		yStart = y;
		zStart = z;
		xr = yr = zh = 1;
		this.xa = xa * speed;
		this.ya = ya * speed;
		this.za = za * speed;
		this.dmg = dmg;
	}
	
	public boolean blocks(Entity e) {
		if (e == owner) return false;
		if (e instanceof Bullet) return false;
		if (e instanceof Unit && ((Unit) e).team == owner.team) return false;
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
			if (Math.random() * steps < i) continue;
			double zz = z + zd * i / steps;
			int br = 200 - i * 200 / steps;
			
			int col = 0;
			if (owner.team == 0) {
				col = 0xff0000ff | (0x010100 * br);
			} else {
				col = 0xffff0000 | (0x010100 * br);
			}
			b.setPixel((int) (x + xd * i / steps), (int) (y + yd * i / steps - zz), col);
		}
	}
	
	public void collide(Entity e, double xxa, double yya, double zza) {
		if (e != null) {
			e.hitBy(this);
		}
		remove();
	}
	
	public void applyHitEffect(Unit unit) {
	}
	
	public int getDamage(Unit unit) {
		return getDamage(unit.x, unit.y, unit.z);
	}
	
	public int getDamage(double xx, double yy, double zz) {
		double xd = xStart - xx;
		double yd = yStart - yy;
		double zd = zStart - zz;
		double distanceTravelled = Math.sqrt(xd * xd + yd * yd + zd * zd) * 5;
		double dmg = this.dmg;
		
		int d = (int) (dmg + Weapon.random.nextDouble());
		
		return d;
	}
}
