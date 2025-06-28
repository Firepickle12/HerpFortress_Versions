package de.jonny.Isometric.unit;

public class Soldier extends Unit {

	public Soldier() {
		super(1);
	}
	
	// Debug
	 @Override
	 public String toString() {
	     return (this.team == 0 ? "Blue Soldier" : "Red Soldier");
	 }

}
