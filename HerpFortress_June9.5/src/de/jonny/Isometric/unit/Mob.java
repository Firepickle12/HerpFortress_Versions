package de.jonny.Isometric.unit;

import java.util.List;

import de.jonny.Isometric.Art;
import de.jonny.Isometric.Bitmap;
import de.jonny.Isometric.Player;
import de.jonny.Isometric.Team;
import de.jonny.Isometric.entity.Entity;
import de.jonny.Isometric.particle.FlameDebris;
import de.jonny.Isometric.particle.MeatDebris;
import de.jonny.Isometric.unit.order.IdleOrder;
import de.jonny.Isometric.unit.order.Order;
import de.jonny.Isometric.weapon.Revolver;
import de.jonny.Isometric.weapon.Weapon;

public class Mob extends Unit {
	public static final int UNIT_SCOUT = 0;
	public static final int UNIT_SOLDIER = 1;
	public static final int UNIT_PYRO = 2;
	public static final int UNIT_DEMOMAN = 3;
	public static final int UNIT_HEAVY = 4;
	public static final int UNIT_ENGINEER = 5;
	public static final int UNIT_MEDIC = 6;
	public static final int UNIT_SNIPER = 7;
	public static final int UNIT_SPY = 8;
	
	public int unitClass;
	public double walkStep = 0;
	public Weapon weapon = new Revolver(this);
	
	public double speed = 100;
	public Order order = new IdleOrder();
	
	
	public Mob(int unitClass, Player player) {
		super(player);
		this.unitClass = unitClass;
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
			
		if (isOnGround()) {
			xa *= 0.5;
			ya *= 0.5;
		} else {
			xa *= 0.99;
			ya *= 0.99;
		}
		za -= 0.08;
		
		order.tick();
		if (order.finished()) {
			setOrder(getNextOrder());
		}
		attemptMove();
		
		if (burnTime > 0) {
			FlameDebris fd = new FlameDebris(x + (random.nextDouble() - 0.5) * 4, y + (random.nextDouble() - 0.5) * 4, z + random.nextInt(12));
			fd.xa *= 0.1;
			fd.ya *= 0.1;
			fd.za *= 0.1;
			fd.life /= 2;
			level.add(fd);
		}
		
		double r = 3;
		for (Entity e : level.getEntities(x - r, y - r, z - r, x + r, y + r, z + r)) {
			if (e instanceof Mob && e != this) {
				Mob u = (Mob) e;
				if (u.team == team && u.isAlive()) {
					double xd = u.x - x;
					double yd = u.y - y;
					if (xd * xd + yd * yd < 0.01) {
						xd = 0.01;
						yd = 0;
					}
					double dd = Math.sqrt(xd * xd + yd * yd);
					if (dd < r * r) {
						xd = xd / dd / dd * 0.5;
						yd = yd / dd / dd * 0.5;
						this.knockBack(-xd, -yd, 0);
						u.knockBack(xd, yd, 0);
					}
				}
			}
		}
		
	}
	
	public boolean isAlive() {
		return health > 0;
	}

	public Order getNextOrder() {
		return new IdleOrder();
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
	public void render(Bitmap b, int xp, int yp) {
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
			b.blendDraw(sheet[frame][unitClass], xp - 8, yp - 15, 0xffffffff);
		} else {
			b.draw(sheet[frame][unitClass], xp - 8, yp - 15);
		}
		b.xFlip = false;
	}
	
	public void renderSelected(Bitmap screen) {
		int xp = (int) x;
		int yp = (int) (y - z - 5);
		
		int r = 8;
		screen.box(xp - r, yp - r - 1, xp + r - 1, yp + r, 0xff40ff80);
		
		int dmg = (maxHealth - health) * 11 / maxHealth;
		screen.fill(xp - 5, yp - 10, xp + 5, yp - 10, 0xffff0000);
		screen.fill(xp - 5, yp - 10, xp + 5 - dmg, yp - 10, 0xff00ff00);
		int ammo = (weapon.maxAmmoLoaded - weapon.ammoLoaded) * 11 / weapon.maxAmmoLoaded;
		screen.fill(xp - 5, yp - 11, xp + 5, yp - 11, 0xff202020);
		screen.fill(xp - 5, yp - 11, xp + 5 - ammo, yp - 11, 0xffb0b0b0);
	}
	
	public void renderShadow(Bitmap b, int xp, int yp) {
		if (deadTime > 0) return;
		super.renderShadow(b, xp, yp);
		
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
		b.draw(sheet[frame][unitClass], xp - 8, yp - 15);
		b.xFlip = false;
	}
	
	public void setOrder(Order order) {
		this.order = order;
		order.init(this);
	}
	
	public void MoveForwards() {
		double moveSpeed = 0.2 * speed / 100;
		if (!isOnGround()) {
			moveSpeed *= 0.1;
		}
		xa += Math.cos(dir) * moveSpeed;
		ya += Math.sin(dir) * moveSpeed;
		
		walkStep += speed / 100.0;
	}
	
	public void jump() {
		za = 1.5;
	}
	
	public static Unit create(int unitClass, Player player) {
		Unit unit = null;
		
		if (unitClass == UNIT_SCOUT) unit = new Scout(player);
		if (unitClass == UNIT_SOLDIER) unit = new Soldier(player);
		if (unitClass == UNIT_PYRO) unit = new Pyro(player);
		if (unitClass == UNIT_DEMOMAN) unit = new Demoman(player);
		if (unitClass == UNIT_HEAVY) unit = new Heavy(player);
		if (unitClass == UNIT_ENGINEER) unit = new Engineer(player);
		if (unitClass == UNIT_MEDIC) unit = new Medic(player);
		if (unitClass == UNIT_SNIPER) unit = new Sniper(player);
		if (unitClass == UNIT_SPY) unit = new Spy(player);
		
		return unit;
	}
}
