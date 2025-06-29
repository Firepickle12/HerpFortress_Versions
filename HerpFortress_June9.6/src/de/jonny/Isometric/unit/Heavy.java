package de.jonny.Isometric.unit;

import de.jonny.Isometric.Player;
import de.jonny.Isometric.weapon.Minigun;

public class Heavy extends Mob {
	public Heavy(Player player) {
		super(4, player);
		maxHealth = health = 300;
		speed = 77;
		
		weapon = new Minigun(this);
	}
}
