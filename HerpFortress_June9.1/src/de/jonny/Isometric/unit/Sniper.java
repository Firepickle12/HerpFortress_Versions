package de.jonny.Isometric.unit;

import de.jonny.Isometric.weapon.SniperRifle;

public class Sniper extends Unit {
	public Sniper() {
		super(7);
		maxHealth = health = 125;
		speed = 100;
		
		weapon = new SniperRifle(this);
	}
}
