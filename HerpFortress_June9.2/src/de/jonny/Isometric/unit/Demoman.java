package de.jonny.Isometric.unit;

import de.jonny.Isometric.weapon.StickyBombLauncher;

public class Demoman extends Unit {
	
	// Fügt nummer 3 in den Konstruktor von unit ein wenn diese Klasse als Unit Instanziiert wird.
	public Demoman() {
		super(3);
		maxHealth = health = 175;
		speed = 93;
		
		weapon = new StickyBombLauncher(this);
	}
}
