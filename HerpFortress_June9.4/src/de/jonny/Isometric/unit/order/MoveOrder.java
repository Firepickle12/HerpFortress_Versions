package de.jonny.Isometric.unit.order;

public class MoveOrder extends Order {
	public double x, y;
	
	public MoveOrder(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void tick() {
		if (unit.turnTowards(unit.angleTo(x, y))) {
			unit.MoveForwards();
		}
	}
	
	public boolean finished() {
		return unit.distanceTo(x, y) < 8;
	}
}
