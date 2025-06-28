package de.jonny.Isometric.unit.order;

import de.jonny.Isometric.unit.Unit;

public class Order {
	public Unit unit;
	
	public void init(Unit unit) {
		this.unit = unit;
	}
	
	public void tick() {
	}

	public boolean finished() {
		return true;
	}
}
