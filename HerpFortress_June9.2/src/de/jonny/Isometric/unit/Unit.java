package de.jonny.Isometric.unit;

import java.util.List;
import java.util.Random;

import de.jonny.Isometric.Art;
import de.jonny.Isometric.Bitmap;
import de.jonny.Isometric.Team;
import de.jonny.Isometric.entity.Bullet;
import de.jonny.Isometric.entity.Entity;
import de.jonny.Isometric.entity.Mob;
import de.jonny.Isometric.entity.Rocket;
import de.jonny.Isometric.level.Level;
import de.jonny.Isometric.particle.FlameDebris;
import de.jonny.Isometric.particle.MeatDebris;
import de.jonny.Isometric.particle.SplatDebris;
import de.jonny.Isometric.weapon.Revolver;
import de.jonny.Isometric.weapon.Weapon;

public class Unit extends Mob {
	public static Random random = new Random();
	
	public static final int UNIT_SCOUT = 0;
	public static final int UNIT_SOLDIER = 1;
	public static final int UNIT_PYRO = 2;
	public static final int UNIT_DEMOMAN = 3;
	public static final int UNIT_HEAVY = 4;
	public static final int UNIT_ENGINEER = 5;
	public static final int UNIT_MEDIC = 6;
	public static final int UNIT_SNIPER = 7;
	public static final int UNIT_SPY = 8;
	
	private int ySpriteIndex;
	public Team team;
	
	public double dir = 0;
	public double dira = 0;
	public double walkStep = 0;
	
	public int shootDelay = 0;
	public int shootTime = 0;
	
	public double speed = 100;
	public int maxHealth = 125;
	public int health = 125;
	public int hurtTime = 0;
	
	public Weapon weapon = new Revolver(this);
	public int burnTime, burnInterval;
	
	public double aimDir;
	
	public Unit(int ySpriteIndex) {
		this.ySpriteIndex = ySpriteIndex;
	}
	
	public void init(Level level) {
		super.init(level);
		level.units.add(this);	
	}
	
	public void onRemoved() {
		level.units.remove(this);	
	}
	
	public void hitBy(Bullet bullet) {
		if (bullet.owner.team == team) return;
		
		bullet.applyHitEffect(this);
		
		hurt(bullet.getDamage(this));
		knockBack(bullet.xa * 0.25, bullet.ya * 0.25, bullet.za * 0.25);
		SplatDebris sd = new SplatDebris(x, y, z + 5);
		sd.xa -= bullet.xa * 0.1;
		sd.ya -= bullet.ya * 0.1;
		sd.za -= bullet.za * 0.1;
		level.add(sd);
	}
	
	public void knockBack(double xxa, double yya, double zza) {
		xa += (xxa - xa) * 0.4;
		ya += (yya - ya) * 0.4;
		za += (zza - za) * 0.4;
	}
	
	public void init(Team team) {
		this.team = team;
	}
	
	public void tick() {
		super.tick();
		
		if (burnTime > 0) {
			if (++burnInterval >= 30) {
				burnInterval = 0;
				hurt(3);
			}
			burnTime --;
		}
		
		if (hurtTime > 0) hurtTime --;
		
		
		weapon.tick();
		if(weapon.canUse()) {
			updateWeapon();
		}
		
		if (shootTime > 0) {
			shootTime --;
		}
		
		if (health <= 0) {
			die();
			return;
		}
		
		boolean onGround = z <= 1;
		if (onGround) {
			xa *= 0.5;
			ya *= 0.5;
		
	/*		dira *= 0.9;
			dira += ((random.nextDouble() - random.nextDouble()) * random.nextDouble()) * 0.1;
			dir += dira;
			
			double moveSpeed = 0.2 * speed / 100;
			xa += Math.cos(dir) * moveSpeed;
			ya += Math.sin(dir) * moveSpeed;
			if (random.nextInt(100) == 0) {
				za = 1.5;
			}
			walkStep += speed / 100.0;
	*/
		} else {
			xa *= 0.99;
			ya *= 0.99;
		}
		za -= 0.08;
		attemptMove();
		
		
		if(xa == 0 || ya == 0) {
			dira += ((random.nextDouble() - random.nextDouble()) * random.nextDouble()) * 0.4;
		}
		
		if (burnTime > 0) {
			FlameDebris fd = new FlameDebris(x + (random.nextDouble() - 0.5) * 4, y + (random.nextDouble() - 0.5) * 4, z + random.nextInt(12));
			fd.xa *= 0.1;
			fd.ya *= 0.1;
			fd.za *= 0.1;
			fd.life /= 2;
			level.add(fd);
		}
	}
	
	private void die() {
		for (int i = 0; i < 8; i++) {
				level.add(new MeatDebris(x, y, z + i));
			weapon.playerDied();
			remove();
		}
	}
	
	public void updateWeapon() {
		Entity target = findTarget();
		if (weapon.ammoLoaded > 0 && target != null) {
			shootAt(target);
		} else {
			weapon.reload();
		}
	}

	private void shootAt(Entity target) {
		double lead = Math.sqrt(target.distanceToSqr(this)) * weapon.aimLead / 5;
		
		double xd = (target.x + target.xa * lead) - x;
		double yd = (target.y + target.ya * lead) - y;
		double zd = (target.z + target.za * lead) - z;
		if (weapon.aimAtGround) {
			zd = (0) - (z + 5);
		}
		
		double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
		xd /= dd;
		yd /= dd;
		zd /= dd;
		weapon.shoot(xd, yd, zd);
		aimDir = Math.atan2(yd, xd);
		shootTime = 20;
	}

	private Entity findTarget() {
		double r = weapon.maxRange;
		List<Entity> es = level.getEntities(x - r, y - r, z - r, x + r, y + r, z + r);
		Entity closest = null;
		for (int i = 0; i < es.size(); i++) {
			Entity e = es.get(i);
			if (e instanceof Unit && e != this) {
				Unit u = (Unit) e;
				if (u.team != team && u.distanceToSqr(this) < r * r) {
					if (closest == null) {
						closest = e;
					} else if (e.distanceToSqr(this) < closest.distanceToSqr(this)) {
						closest = e;
					}
				}
			}
		}
		
		return closest;
	}

	// Rendert die Figuren
	public void render(Bitmap b) {
		int xp = (int) x;
		int yp = (int) (y - z);
		
		int frame = 0;
		
		if (shootTime == 0) {
			int dirFrame = (int) (Math.floor(-dir * 4 / (Math.PI * 2) - 2.5)) & 3;
			if (dirFrame == 0) frame = 0;
			if (dirFrame == 1) frame = 3;
			if (dirFrame == 2) frame = 6;
			if (dirFrame == 3) {
				frame = 3;
				b.xFlip = true;
			}
			
			int walkFrame = ((int) walkStep / 4) & 3;
			if (frame == 3) {
				if (walkFrame == 1) frame += 1;
				if (walkFrame == 2) frame += 2;
				if (walkFrame == 3) frame += 1;
			} else {
				if (walkFrame == 1) frame += 1;
				if (walkFrame == 2) frame += 2;
			}
		} else {
			int dirFrame = (int) (-Math.floor(aimDir * 8 / (Math.PI * 2) - 1.5)) & 7;
			frame = dirFrame + 9;
			if (dirFrame > 4) {
				frame = 9 + 3 - (dirFrame - 5);
				b.xFlip = true;
			}
		}
		
		Bitmap[][] sheet = team == Team.blu ? Art.i.blu : Art.i.red;
		if (hurtTime > 0 && hurtTime / 2 % 2 == 1) {
			b.blendDraw(sheet[frame][ySpriteIndex], xp - 8, yp - 15, 0xffffffff);
		} else {
			b.draw(sheet[frame][ySpriteIndex], xp - 8, yp - 15);
		}
		b.xFlip = false;
	}
	
	// Instanziiert Unit immer Unterschiedlich jenachdem was man für werte einsetzt
	public static Unit create(int unitClass, Team team) {
		Unit unit = null;
		
		if (unitClass == UNIT_SCOUT) unit = new Scout();
		if (unitClass == UNIT_SOLDIER) unit = new Soldier();
		if (unitClass == UNIT_PYRO) unit = new Pyro();
		if (unitClass == UNIT_DEMOMAN) unit = new Demoman();
		if (unitClass == UNIT_HEAVY) unit = new Heavy();
		if (unitClass == UNIT_ENGINEER) unit = new Engineer();
		if (unitClass == UNIT_MEDIC) unit = new Medic();
		if (unitClass == UNIT_SNIPER) unit = new Sniper();
		if (unitClass == UNIT_SPY) unit = new Spy();
		
		unit.init(team);
		
		return unit;
	}
	
	public void hurt(int dmg) {
		health -= dmg;
		hurtTime = 20;
	}
	
	public void handleExplosion(Bullet source, int dmg, double xd, double yd, double zd) {
		if (this == source.owner) {
			dmg /= 2;
		} else if (team == source.owner.team) {
			return;
		}
		hurt(dmg);
		knockBack(xd * 2, yd * 2, zd * 2);
	}
	
	public void renderSelected(Bitmap screen) {
		int xp = (int) x;
		int yp = (int) (y - z - 5);
		
		int r = 8;
		screen.box(xp - r, yp - r, xp + r, yp + r, 0xff00ff00);
		
		int dmg = (maxHealth - health) * 11 / maxHealth;
		screen.fill(xp - 5, yp - 10, xp + 5, yp - 10, 0xffff0000);
		screen.fill(xp - 5, yp - 10, xp + 5 - dmg, yp - 10, 0xff00ff00);
		int ammo = (weapon.maxAmmoLoaded - weapon.ammoLoaded) * 11 / weapon.maxAmmoLoaded;
		screen.fill(xp - 5, yp - 11, xp + 5, yp - 11, 0xff202020);
		screen.fill(xp - 5, yp - 11, xp + 5 - ammo, yp - 11, 0xffb0b0b0);
	}
	
	public double distanceToScreenSpaceSqr(double x0, double y0) {
		double xx = x;
		double yy = y - z - 6;
		
		double xd = xx - x0;
		double yd = yy - y0;
		
		return xd * xd + yd * yd;
	}

	public boolean intersectsScreenSpace(double x0, double y0, double x1, double y1) {
		double xx = x;
		double yy = y - z - 6;
		int ww = 4;
		int hh = 12;
		if (x1 <= xx - ww || x0 > xx + ww || y1 <= yy - hh || y0 > yy) return false;
		return true;
	}
}
