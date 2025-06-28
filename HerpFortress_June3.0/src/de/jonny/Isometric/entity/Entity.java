package de.jonny.Isometric.entity;

import java.util.List;

import de.jonny.Isometric.Sprite;

public class Entity extends Sprite {
	public int xBlockCell, yBlockCell;
	public double xa, ya, za;
	
	// Prüft ob das gegebene rechteck überlappt
	public boolean intersects(double x0, double y0, double z0, double x1, double y1, double z1) {
		if (x1 <= x - xr || x0 > x + xr || y1 <= y - yr || y0 > y + yr || z1 <= z || z0 > z + zh) return false;
		return true;
	}
	
	public boolean blocks(Entity e) {
		return true;
	}
	
	public void tick() {
		
	}
	
	public void attemptMove() {
		int steps = (int) (Math.sqrt(xa * xa + ya * ya + za * za) + 1);
		for (int i = 0; i < steps; i++) {
			_move(xa / steps, 0, 0);
			_move(0, ya / steps, za / steps);
		}
	}
	
	private void _move(double xxa, double yya, double zza) {
		if (removed) return;
		double xn = x + xxa;
		double yn = y + yya;
		double zn = z + zza;
		
		if (xn < 0 || yn < 0 || xn >= level.w || yn >= level.h || zn < 0 || zn > level.maxHeight) {
			collide(null, xxa, yya, zza);
			return;
		}
		List<Entity> entities = level.getEntities(xn - xr, yn - yr, zn, xn + xr, yn + yr, zn);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e == this) continue;
			if (e.blocks(this) && blocks(e)) {
				collide(e, xxa, yya, zza);
				return;
			}
		}
		x = xn;
		y = yn;
		z = zn;
		return;
	}
	
	public void collide(Entity e, double xxa, double yya, double zza) {
		if (xxa != 0) xa = 0;
		if (yya != 0) ya = 0;
		if (zza != 0) za = 0;
	}
}
