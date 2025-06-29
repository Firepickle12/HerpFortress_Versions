package de.jonny.Isometric.unit;

import de.jonny.Isometric.Player;
import de.jonny.Isometric.weapon.StickyBombLauncher;

public class Demoman extends Mob {
	public Demoman(Player player) {
		super(3, player);
		maxHealth = health = 175;
		speed = 93;
		
		weapon = new StickyBombLauncher(this);
	}
}
