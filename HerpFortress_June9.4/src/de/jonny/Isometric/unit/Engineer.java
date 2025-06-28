package de.jonny.Isometric.unit;

import de.jonny.Isometric.Player;
import de.jonny.Isometric.weapon.Shotgun;

public class Engineer extends Mob {
	public Engineer(Player player) {
		super(5, player);
		maxHealth = health = 125;
		speed = 100;
		
		weapon = new Shotgun(this);
	}
}
