package de.jonny.Isometric.weapon;

import de.jonny.Isometric.entity.Bullet;
import de.jonny.Isometric.entity.Rocket;
import de.jonny.Isometric.unit.Unit;

public class RocketLauncher extends Weapon {
	public RocketLauncher(Unit owner) {
		super(owner);
		 ammoLoaded = maxAmmoLoaded = 4;
		 ammoCarried = maxAmmoCarried = 20;
		 
		 shootDelayTime = 0.8;
		 startReloadDelayTime = 0.8;
		 reloadDelayTime = 0.92;
		 aimAtGround = true;
		 aimLead = 2;
		 
		 highRamp = 125;
		 lowRamp = 53;
	}
	
	public void shoot(double xa, double ya, double za) {
		super.shoot(xa, ya, za);
		double spread = 0.001;
		owner.level.add(new Rocket(owner, this, xa, ya, za, 90));
		shootDelay = shootDelayTime;
	}
}
