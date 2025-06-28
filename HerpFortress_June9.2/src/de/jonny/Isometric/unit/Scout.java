package de.jonny.Isometric.unit;

import de.jonny.Isometric.weapon.Scattergun;

public class Scout extends Unit {
	public Scout() {
		super(0);
		maxHealth = health = 125;
		speed = 133;
		
		weapon = new Scattergun(this);
	}
	
}
