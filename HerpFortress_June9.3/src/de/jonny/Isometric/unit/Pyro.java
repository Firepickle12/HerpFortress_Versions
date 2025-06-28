package de.jonny.Isometric.unit;

import de.jonny.Isometric.weapon.Flamethrower;

public class Pyro extends Unit {
	public Pyro() {
		super(2);
		maxHealth = health = 175;
		speed = 100;
		
		weapon = new Flamethrower(this);
	}
	
	public void tick() {
		if (burnTime > 0) burnTime = 0;
		super.tick();
	}
}
