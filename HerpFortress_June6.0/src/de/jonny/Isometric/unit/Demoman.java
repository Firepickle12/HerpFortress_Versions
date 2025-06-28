package de.jonny.Isometric.unit;

public class Demoman extends Unit {
	
	// Fügt nummer 3 in den Konstruktor von unit ein wenn diese Klasse als Unit Instanziiert wird.
	public Demoman() {
		super(3);
	}
	
	// Debug
	 @Override
	 public String toString() {
	     return (this.team == 0 ? "Blue Demoman" : "Red Demoman");
	 }
}
