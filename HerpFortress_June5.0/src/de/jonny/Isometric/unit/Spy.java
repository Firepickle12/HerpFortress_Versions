package de.jonny.Isometric.unit;

public class Spy extends Unit {
	public Spy() {
		super(8);
	}
	
	// Debug
	 @Override
	 public String toString() {
	     return (this.team == 0 ? "Blue Spy" : "Red Spy");
	 }
}
