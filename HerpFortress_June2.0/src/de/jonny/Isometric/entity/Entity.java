package de.jonny.Isometric.entity;

import de.jonny.Isometric.Sprite;

public class Entity extends Sprite {
	public int xBlockCell, yBlockCell;
	public double xa, ya, za;
	
	// Prüft ob das gegebene rechteck überlappt
	public boolean intersects(double x0, double y0, double z0, double x1, double y1, double z1) {
		if (x1 <= x - xr || x0 > x + xr || y1 <= y - yr || y0 > y + yr || z1 <= z || z0 > z + zh) return false;
		return true;
	}
	
	public void tick() {
		
	}
}
