package de.jonny.Isometric.unit;

public class Engineer extends Unit {
	public Engineer() {
		super(5);
	}
	
	// Debug
	 @Override
	 public String toString() {
	     return (this.team == 0 ? "Blue Engineer" : "Red Engineer");
	 }
	
}
