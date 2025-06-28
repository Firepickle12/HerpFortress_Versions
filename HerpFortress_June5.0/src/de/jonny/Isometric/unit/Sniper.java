package de.jonny.Isometric.unit;

public class Sniper extends Unit {
	public Sniper() {
		super(7);
	}
	
	// Debug
	 @Override
	 public String toString() {
	     return (this.team == 0 ? "Blue Sniper" : "Red Sniper");
	 }
	
}
