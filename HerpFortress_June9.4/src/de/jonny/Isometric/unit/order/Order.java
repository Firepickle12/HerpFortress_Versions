package de.jonny.Isometric.unit.order;

import de.jonny.Isometric.unit.Mob;
import de.jonny.Isometric.unit.Unit;

public class Order {
	public Mob unit;
	
	public void init(Mob unit) {
		this.unit = unit;
	}
	
	public void tick() {
	}

	public boolean finished() {
		return true;
	}
}
