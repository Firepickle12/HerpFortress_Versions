package de.jonny.Isometric.unit;

import java.util.Random;

import de.jonny.Isometric.Art;
import de.jonny.Isometric.Bitmap;
import de.jonny.Isometric.entity.Bullet;
import de.jonny.Isometric.entity.Mob;

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
	public int team;
	
	public double dir = 0;
	public double dira = 0;
	public double walkStep = 0;
	
	public int shootDelay = 0;
	public int shootTime = 0;
	
	public double speed = 100;
	public int maxHealth = 125;
	public int Health = 125;
	
	public Unit(int ySpriteIndex) {
		this.ySpriteIndex = ySpriteIndex;
	}
	
	public void init(int team) {
		this.team = team;
	}
	
	public void tick() {
		super.tick();
		
		if (shootDelay == 0) {
			shootDelay = 2 + random.nextInt(100);
		} else {
			shootDelay --;
			if (shootDelay == 0) {
				level.add(new Bullet(this, x, y, z + 5, Math.cos(dir), Math.sin(dir), 0));
				shootTime = 20;
			}
		}
		
		if (shootTime > 0) {
			shootTime --;
		}
		
		xa *= 0.5;
		ya *= 0.5;
		dira *= 0.9;
		dira += ((random.nextDouble() - random.nextDouble()) * random.nextDouble()) * 0.1;
		dir += dira;
		
		// Movement
		double moveSpeed = 0.2 * speed / 100;
		xa += Math.cos(dir) * moveSpeed;
		ya += Math.sin(dir) * moveSpeed;
		attemptMove();
		
		if(xa == 0 || ya == 0) {
			dira += ((random.nextDouble() - random.nextDouble()) * random.nextDouble()) * 0.4;
		}
		
		walkStep += speed / 100.0;
	}
	
	public void renderShadow(Bitmap b) {
		int xp = (int) x;
		int yp = (int) y;
		b.fill(xp - 3, yp, xp + 2, yp, 1);
	}
	
	// Rendert die Figuren
	public void render(Bitmap b) {
		int xp = (int) x;
		int yp = (int) y;
		
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
			int dirFrame = (int) (-Math.floor(dir * 8 / (Math.PI * 2) - 1.5)) & 7;
			frame = dirFrame + 9;
			if (dirFrame > 4) {
				frame = 9 + 3 - (dirFrame - 5);
				b.xFlip = true;
			}
		}
		
		Bitmap[][] sheet = team == 0 ? Art.i.blu : Art.i.red;
		b.draw(sheet[frame][ySpriteIndex], xp - 8, yp - 15);
		b.xFlip = false;
	}
	
	// Instanziiert Unit immer Unterschiedlich jenachdem was man für werte einsetzt
	public static Unit create(int unitClass, int team) {
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
}
