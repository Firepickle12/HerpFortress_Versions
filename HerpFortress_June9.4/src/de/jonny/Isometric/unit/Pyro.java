package de.jonny.Isometric.unit;

import de.jonny.Isometric.Player;
import de.jonny.Isometric.weapon.Flamethrower;

public class Pyro extends Mob {
	public Pyro(Player player) {
		super(2, player);
		maxHealth = health = 175;
		speed = 100;
		
		weapon = new Flamethrower(this);
	}
	
	public void tick() {
		if (burnTime > 0) burnTime = 0;
		super.tick();
	}
}
