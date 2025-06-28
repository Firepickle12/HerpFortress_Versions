package de.jonny.Isometric;

import java.util.HashSet;
import java.util.Set;

import de.jonny.Isometric.unit.Unit;

public class Player {
	public Team team;
	public Set<Unit> selected = new HashSet<Unit>();
	
	public Player(Team team) {
		this.team = team;
	}
}
