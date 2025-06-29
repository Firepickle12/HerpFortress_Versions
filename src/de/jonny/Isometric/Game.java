package de.jonny.Isometric;

import de.jonny.Isometric.level.Level;

public class Game {

	public Level level;
	private Bitmap shadows;

	public Game() {
		level = new Level();
	}

	public void render(Bitmap screen, int xScroll, int yScroll) {
		if (shadows == null) shadows = new Bitmap(screen.w, screen.h);
		shadows.clear(0);

		screen.xOffs = -xScroll;
		screen.yOffs = -yScroll;
		level.renderBg(screen, xScroll, yScroll);

		shadows.xOffs = -xScroll;
		shadows.yOffs = -yScroll;
		level.renderShadows(shadows);
		screen.xOffs = 0;
		screen.yOffs = 0;
		screen.shade(shadows);

		screen.xOffs = -xScroll;
		screen.yOffs = -yScroll;
		level.renderSprites(screen);

	}

	public void tick() {
		level.tick();
	}
}