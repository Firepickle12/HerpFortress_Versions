package de.jonny.Isometric.unit;

public class Pyro extends Unit {
	public Pyro() {
		super(2);
	}

	// Debug
	 @Override
	 public String toString() {
	     return (this.team == 0 ? "Blue Pyro" : "Red Pyro");
	 }
	
}
