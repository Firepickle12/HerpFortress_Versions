package de.jonny.Isometric.unit;

import de.jonny.Isometric.entity.Rocket;

public class Soldier extends Unit {

	public Soldier() {
		super(1);
	}
	
	public void shoot() {
		double xt = random.nextDouble() * 300 + 10;
		double yt = random.nextDouble() * 220 + 10;
		double zt = 0;
		
		double xo = x;
		double yo = y;
		double zo = z + 5;
		
		double xd = xt - xo;
		double yd = yt - yo;
		double zd = zt - zo;
		double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
		xd /= dd;
		yd /= dd;
		zd /= dd;
		
		level.add(new Rocket(this, xo, yo, zo, xd, yd, zd));
	}
	
	// Debug
	 @Override
	 public String toString() {
	     return (this.team == 0 ? "Blue Soldier" : "Red Soldier");
	 }

}
