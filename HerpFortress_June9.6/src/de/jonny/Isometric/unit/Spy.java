package de.jonny.Isometric.unit;

import de.jonny.Isometric.Player;
import de.jonny.Isometric.weapon.Revolver;
import de.jonny.Isometric.weapon.Weapon;

public class Spy extends Mob {
	public Spy(Player player) {
		super(8, player);
		maxHealth = health = 125;
		speed = 100;
		
		weapon = new Revolver(this);
	}
}
