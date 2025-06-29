package de.jonny.Isometric;

import de.jonny.Isometric.level.Level;
import de.jonny.Isometric.unit.Mob;
import de.jonny.Isometric.unit.Unit;

public class Player {
	public int selected = 0;
	public Team team;
	public Level level;
	
	public Player(Level level, Team team) {
		this.team = team;
		this.level = level;
	}
	
	public void tick() {
	}
	
	public Mob getSelectedUnit() {
		return getUnit(selected);
	}

	public Mob getUnit(int unitClass) {
		for (Unit u : level.units) {
			if (u.team == team && u instanceof Mob && ((Mob) u).unitClass == unitClass) {
				return (Mob) u;
			}
		}
		return null;
	}
}
