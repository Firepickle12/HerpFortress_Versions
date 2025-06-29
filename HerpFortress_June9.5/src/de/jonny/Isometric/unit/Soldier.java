package de.jonny.Isometric.unit;

import de.jonny.Isometric.Player;
import de.jonny.Isometric.entity.Rocket;
import de.jonny.Isometric.weapon.RocketLauncher;

public class Soldier extends Mob {
	public Soldier(Player player) {
		super(1, player);
		maxHealth = health = 200;
		speed = 80;
		
		weapon = new RocketLauncher(this);
	}
}
