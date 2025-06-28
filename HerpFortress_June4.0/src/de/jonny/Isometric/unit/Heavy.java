package de.jonny.Isometric.unit;

public class Heavy extends Unit {
	public Heavy() {
		super(4);
	}
	
	// Debug
	 @Override
	 public String toString() {
	     return (this.team == 0 ? "Blue Heavy" : "Red Heavy");
	 }

}
