package de.jonny.Isometric;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import de.jonny.Isometric.entity.Entity;
import de.jonny.Isometric.unit.Unit;

public class PlayerView {
	private static final int SELECT_REGION_DISTANCE = 4;
	public Game game;
	public Player player;
	public Mouse mouse;
	
	public boolean selecting;
	public int xStartSelect;
	public int yStartSelect;
	
	public PlayerView(Game game, Player player, Mouse mouse) {
		this.game = game;
		this.player = player;
		this.mouse = mouse;
	}
	
	public void tick() {
		if (mouse.b0Clicked) {
			selecting = true;
			xStartSelect = mouse.x;
			yStartSelect = mouse.y;
		}
		if (mouse.b0Released) {
			if (hasDraggedBox()) {
				selectAll(xStartSelect, yStartSelect, mouse.x, mouse.y);
			} else {
				selectNearest(xStartSelect, yStartSelect);
			}
			selecting = false;
		}
	}
	
	private void selectNearest(int x0, int y0) {
		player.selected.clear();
		int r = 8;
		
		Unit nearest = null;
		for (Unit u : game.level.getUnitScreenSpace(x0 - r, y0 - r, x0 + r, y0 + r)) {
			if (u.team == player.team) {
				if (nearest == null || u.distanceToScreenSpaceSqr(x0,  y0) < nearest.distanceToScreenSpaceSqr(x0,  y0)) {
					nearest = u;
				}
			}
		}
		
		if (nearest != null) {
			player.selected.add(nearest);
		}
	}
	
	public void selectAll(int x0, int y0, int x1, int y1) {
		if (x0 > x1) {
			int tmp = x0;
			x0 = x1;
			x1 = tmp;
		}
		if (y0 > y1) {
			int tmp = y0;
			y0 = y1;
			y1 = tmp;
		}
		int r = 8;
		
		player.selected.clear();
		for (Unit u : game.level.getUnitScreenSpace(x0 - r, y0 - r, x1 + r, y1 + r)) {
			if (u.team == player.team) {
				player.selected.add(u);
			}
		}
	}
	

	public void drawSelectBox(Bitmap screen, int x0, int y0, int x1, int y1) {
		if (x0 > x1) {
			int tmp = x0;
			x0 = x1;
			x1 = tmp;
		}
		if (y0 > y1) {
			int tmp = y0;
			y0 = y1;
			y1 = tmp;
		}
		
		screen.box(x0, y0, x1, y1, 0xff00ff00);
	}

	public void render(Bitmap screen) {
		game.render(screen);
		
		TreeSet<Sprite> sortedSprites = new TreeSet<Sprite>(game.level.spriteComparator);
		sortedSprites.addAll(player.selected);
		for (Sprite s : sortedSprites) {
			((Unit) s).renderSelected(screen);
		}
		
		if (selecting && hasDraggedBox()) {
			drawSelectBox(screen, xStartSelect, yStartSelect, mouse.x, mouse.y);
		}
	}
	
	private boolean hasDraggedBox() {
		int xd = xStartSelect - mouse.x;
		int yd = yStartSelect - mouse.y;
		if (xd < 0) xd = -xd;
		if (yd < 0) yd = -yd;
		int d = xd > yd ? xd : yd;
		return d > SELECT_REGION_DISTANCE;
	}
}
