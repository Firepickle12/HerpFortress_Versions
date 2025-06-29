package de.jonny.Isometric.unit;

import de.jonny.Isometric.Player;

public class Medic extends Mob {
	public Medic(Player player) {
		super(6, player);
		maxHealth = health = 150;
		speed = 106.07;
	}
	
}
