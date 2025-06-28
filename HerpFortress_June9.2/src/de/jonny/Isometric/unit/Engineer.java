package de.jonny.Isometric.unit;

import de.jonny.Isometric.weapon.Shotgun;

public class Engineer extends Unit {
	public Engineer() {
		super(5);
		maxHealth = health = 125;
		speed = 100;
		
		weapon = new Shotgun(this);
	}
}
