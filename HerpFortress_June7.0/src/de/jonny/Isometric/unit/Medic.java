package de.jonny.Isometric.unit;

public class Medic extends Unit {
	public Medic() {
		super(6);
	}
	
	// Debug
	 @Override
	 public String toString() {
	     return (this.team == 0 ? "Blue Medic" : "Red Medic");
	 }
	
}
