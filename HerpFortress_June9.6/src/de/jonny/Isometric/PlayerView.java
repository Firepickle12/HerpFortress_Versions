package de.jonny.Isometric;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import de.jonny.Isometric.unit.Mob;
import de.jonny.Isometric.unit.Unit;
import de.jonny.Isometric.unit.order.MoveOrder;

public class PlayerView {
	private static final int SELECT_REGION_DISTANCE = 4;
	public Game game;
	public Player player;
	public Input input;
	
	public boolean dragging;
	public int xStartDrag;
	public int yStartDrag;
	public Team team;
	
	public double xScroll = 442;
	public double yScroll = 286;
	public double xScrollA = 0;
	public double yScrollA = 0;
	public double xScrollT = 0;
	public double yScrollT = 0;
	public double scrollSpeed = 2;
	public int scrollSteps = 0;
	public int time = 0;
	
	public PlayerView(Game game, Player player, Input input) {
		this.game = game;
		this.player = player;
		this.input = input;
	}
	
	public void sendAllSelectedTo(int x, int y) {
		double xx = (x + xScroll) / Sprite.SCALE_X / 2;
		double yy = (y + yScroll) / Sprite.SCALE_X;
		double xt = xx + yy;
		double yt = yy - xx;
		
		
		Mob unit = player.getSelectedUnit();
		if (unit != null) {
			unit.setOrder(new MoveOrder(xt, yt));
		}
	}
	
	public void tick() {
		player.tick();
		time ++;
		
		if (input.b0Clicked) {
			Mob nearest = getNearest(input.x, input.y);
			if (nearest != null && nearest != player.getSelectedUnit()) {
				player.selected = nearest.unitClass;
			}
		}
		
		if (input.b1Clicked) {
			sendAllSelectedTo(input.x, input.y);
		}
		
		if (input.b2Clicked) {
			dragging = true;
			xStartDrag = input.x;
			yStartDrag = input.y;
		}
		
		if (input.b2) {
			xScroll -= input.x - xStartDrag;
			yScroll -= input.y - yStartDrag;
			xStartDrag = input.x;
			yStartDrag = input.y;
			xScrollA = yScrollA = 0;
			scrollSteps = 0;
		}
	}

	private Mob getNearest(int x0, int y0) {
		x0 += xScroll;
		y0 += yScroll;
		int	r = 8;
		
		Mob nearest = null;
		for (Unit u : game.level.getUnitScreenSpace(x0 - r, y0 - r, x0 + r, y0 + r)) {
			if (u.team == player.team && u instanceof Mob) {
				if (nearest == null || u.distanceToScreenSpaceSqr(x0,  y0) < nearest.distanceToScreenSpaceSqr(x0,  y0)) {
					nearest = (Mob) u;
				}
			}
		}
		
		return nearest;
	}
	
	public void selectAll(int x0, int y0, int x1, int y1) {
		x0 += xScroll;
		y0 += yScroll;
		x1 += xScroll;
		y1 += yScroll;
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
		
		for (Unit u : game.level.getUnitScreenSpace(x0 - r, y0 - r, x1 + r, y1 + r)) {
			if (u instanceof Mob) {
				Mob m = (Mob) u;
				if (m.team == player.team) {
					player.selected = m.unitClass;
				}
			}
		}
	}

	public void render(Bitmap screen) {
		screen.fill(0, 0, screen.w, screen.h, 0xffff00ff);
		game.render(screen, (int) (Math.floor(xScroll)), (int) (Math.floor(yScroll)));
		
		Mob u = player.getSelectedUnit();
		if (u != null) u.renderSelected(screen);
		
		screen.xOffs = 0;
		screen.yOffs = 0;
		
		for (int i = 0; i < 9; i++) {
			if (player.selected == i) {
				screen.draw(Art.i.blu[i][14], i * 20 + 64, 240 - 16);
				screen.draw(Art.i.cursors[1][0], i * 20 + 64, 240 - 16 - 10 - (int) (Math.abs(Math.sin(time / 10.0) * 6.5)));
			} else {
				screen.blendDraw(Art.i.blu[i][14], i * 20 + 64, 240 - 14, 0);
			}
		}
	}
}
