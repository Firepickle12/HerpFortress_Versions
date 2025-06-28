package de.jonny.Isometric.unit;

import java.util.Random;

import de.jonny.Isometric.Art;
import de.jonny.Isometric.Bitmap;
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
	
	// Rendert die Figuren
	public void render(Bitmap b) {
		int xp = (int) x;
		int yp = (int) y;
		
		int frame = 0;
		
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
