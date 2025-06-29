package de.jonny.Isometric.unit;

import de.jonny.Isometric.Player;
import de.jonny.Isometric.weapon.SniperRifle;

public class Sniper extends Mob {
	public Sniper(Player player) {
		super(7, player);
		maxHealth = health = 125;
		speed = 100;
		
		weapon = new SniperRifle(this);
	}
}
