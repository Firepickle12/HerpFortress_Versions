package de.jonny.Isometric.unit;

import de.jonny.Isometric.entity.Rocket;
import de.jonny.Isometric.weapon.RocketLauncher;

public class Soldier extends Unit {

	public Soldier() {
		super(1);
		maxHealth = health = 200;
		speed = 80;
		
		weapon = new RocketLauncher(this);
	}
}
