package de.jonny.Isometric.unit;

import de.jonny.Isometric.weapon.Minigun;

public class Heavy extends Unit {
	public Heavy() {
		super(4);
		maxHealth = health = 300;
		speed = 77;
		
		weapon = new Minigun(this);
	}
}
