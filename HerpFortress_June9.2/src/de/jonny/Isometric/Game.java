package de.jonny.Isometric;

import de.jonny.Isometric.level.Level;

public class Game {
	public Player[] players = new Player[2];
	
	public Level level;
	private Bitmap shadows;
	
	public Game() {
		level = new Level(512, 512);
		players[0] = new Player(Team.blu);
		players[1] = new Player(Team.red);
	}
	
	public void render(Bitmap screen) {
		if (shadows == null) shadows = new Bitmap(screen.w, screen.h);
		shadows.clear(0);
		
		level.renderBg(screen);
		level.renderShadows(shadows);
		screen.shade(shadows);
		level.renderSprites(screen);
	}
	
	public void tick() {
		level.tick();
	}
}