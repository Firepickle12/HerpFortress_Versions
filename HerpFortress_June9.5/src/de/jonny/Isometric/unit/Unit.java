package de.jonny.Isometric.unit;

import java.util.List;
import java.util.Random;

import de.jonny.Isometric.Art;
import de.jonny.Isometric.Bitmap;
import de.jonny.Isometric.Player;
import de.jonny.Isometric.Team;
import de.jonny.Isometric.entity.Bullet;
import de.jonny.Isometric.entity.Entity;
import de.jonny.Isometric.entity.Rocket;
import de.jonny.Isometric.level.Level;
import de.jonny.Isometric.particle.FlameDebris;
import de.jonny.Isometric.particle.MeatDebris;
import de.jonny.Isometric.particle.SplatDebris;
import de.jonny.Isometric.unit.order.IdleOrder;
import de.jonny.Isometric.unit.order.MoveOrder;
import de.jonny.Isometric.unit.order.Order;
import de.jonny.Isometric.weapon.Revolver;
import de.jonny.Isometric.weapon.Weapon;

public class Unit extends Entity {
	public static Random random = new Random();
	public Team team;
	public double dir = 0;
	public int shootTime = 0;
	public int maxHealth = 125;
	public double walkStep = 0;
	public int shootDelay = 0;
	public double speed = 100;
	public int health = 125;
	public int hurtTime = 0;
	public int burnTime;
	public int burnInterval;
	public Player player;
	public double aimDir;
	public int deadTime;
	
	public Unit(Player player) {
		this.player = player;
		team = player.team;
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
	
	protected boolean isOnGround() {
		return z <= 1;
	}
	
	public boolean isLegalTarget(Mob u) {
		return u.team != team;
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

	public double distanceTo(double x, double y) {
		double xd = x - this.x;
		double yd = y - this.y;
		
		return Math.sqrt(xd * xd + yd * yd);
	}
	
	public double angleTo(double x, double y) {
		return Math.atan2(y - this.y, x - this.x);
	}
	
	public boolean turnTowards(double angle) {
		while (dir < -Math.PI)
			dir += Math.PI * 2;
		while (dir >= Math.PI)
			dir -= Math.PI * 2;
		while (angle < -Math.PI)
			angle += Math.PI * 2;
		while (angle >= Math.PI)
			angle -= Math.PI * 2;
		
		double angleDiff = angle - dir;
		while (angleDiff < -Math.PI)
			angleDiff += Math.PI * 2;
		while (angleDiff >= Math.PI)
			angleDiff -= Math.PI * 2;
		
		double turnSpeed = 0.1;
		double near = 1.0;
		boolean wasAimed = angleDiff * angleDiff < near * near;
		if (angleDiff < -turnSpeed) angleDiff = -turnSpeed;
		if (angleDiff > +turnSpeed) angleDiff = +turnSpeed;
		dir += angleDiff;
		return wasAimed;
	}
	
	public void heal(int toHeal) {
		int maxHeal = maxHealth - health;
		if (maxHeal <= 0) return;
		if (toHeal > maxHeal) toHeal = maxHeal;
		health += toHeal;
	}
}
