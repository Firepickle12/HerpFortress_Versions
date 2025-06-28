package de.jonny.Isometric.unit;

public class Scout extends Unit {
	public Scout() {
		super(0);
	}
	
	// Debug
	 @Override
	 public String toString() {
	     return (this.team == 0 ? "Blue Scout" : "Red Scout");
	 }
	
}
