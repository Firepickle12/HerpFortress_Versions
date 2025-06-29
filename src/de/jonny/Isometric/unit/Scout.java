package de.jonny.Isometric.unit;

import de.jonny.Isometric.Player;
import de.jonny.Isometric.weapon.Scattergun;

public class Scout extends Mob {
	public Scout(Player player) {
		super(0, player);
		maxHealth = health = 125;
		speed = 133;
		
		weapon = new Scattergun(this);
	}
	
}
