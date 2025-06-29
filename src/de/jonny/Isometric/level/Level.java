package de.jonny.Isometric.level;

import java.util.*;

import de.jonny.Isometric.Art;
import de.jonny.Isometric.Bitmap;
import de.jonny.Isometric.Player;
import de.jonny.Isometric.Sprite;
import de.jonny.Isometric.Team;
import de.jonny.Isometric.entity.Bullet;
import de.jonny.Isometric.entity.Entity;
import de.jonny.Isometric.entity.Rocket;
import de.jonny.Isometric.particle.Explosion;
import de.jonny.Isometric.particle.Particle;
import de.jonny.Isometric.unit.Mob;
import de.jonny.Isometric.unit.Unit;

public class Level {
	public static Random random = new Random();
	public List<Entity> entities = new ArrayList<Entity>();
	public List<Particle> particles = new ArrayList<Particle>();
	public List<Sprite> mapSprites = new ArrayList<Sprite>();
	public final int w, h;
	public final int xs, ys;
	public double maxHeight = 128;
	
	public int[] tiles;
	public List<Unit> units = new ArrayList<Unit>();
	
	public Blockmap blockmap;
	
	// Sortieralgorithmus, da Sprites ein Objekt ist und TreeSets nur Datentypen ohne Hilfe sortieren können.
	public Comparator<Sprite> spriteComparator = new Comparator<Sprite>() {
		public int compare(Sprite s0, Sprite s1) {
			if (s0.y + s0.x < s1.y + s1.x) return -1;
			if (s0.y + s0.x > s1.y + s1.x) return 1;
			if (s0.y < s1.y) return -1;
			if (s0.y > s1.y) return 1;
			if (s0.z < s1.z) return -1;
			if (s0.z > s1.z) return 1;
			if (s0.x < s1.x) return -1;
			if (s0.x > s1.x) return 1;
			return 0;
		}
	};
	
	public Player redPlayer;
	public Player bluPlayer;

	
	public Level() {
		Bitmap bmp = Art.load("/levels/level.png");
		
		int w = bmp.w + 8;
		int h = bmp.h + 8;
		
		this.w = w;
		this.h = h;
		
		xs = w;
		ys = h;
		blockmap = new Blockmap(w * 16, h * 16, 32);
		
		tiles = new int[xs * ys];
		for (int i = 0; i < xs * ys; i++) {
			if (random.nextInt(10) == 1) tiles[i] = 1;
		}
		
		redPlayer = new Player(this, Team.red);
		bluPlayer = new Player(this, Team.blu);
		
		spawnUnits(bluPlayer, 16, 16);
		spawnUnits(redPlayer, 310, 16);
	}
	
	private void spawnUnits(Player player, double x, double y) {
		for (int i = 0; i < 9; i++) {
			if (player.getUnit(i) == null) {
				Unit u = Mob.create(i, player);
				u.x = x;
				u.y = y + i * 24;
				add(u);
			}
		}
	}
	
	// Adds Entitiys to the Array List
	public void add(Particle p) {
		particles.add(p);
		p.init(this);
	}
	
	// Adds Entitiys to the Array List
	public void add(Entity e) {
		entities.add(e);
		blockmap.add(e);
		e.init(this);
	}
	
	public void tick() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (!e.removed) e.tick();
			if (e.removed) {
				blockmap.remove(e);
				e.onRemoved();
				entities.remove(i);
			} else {
				blockmap.update(e);
			}
		}
		
		for (int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			if (!p.removed) p.tick();
			if(p.removed) particles.remove(i--);
		}
	}
	
	public List<Unit> getUnitScreenSpace(double x0, double y0, double x1, double y1) {
		List<Unit> result = new ArrayList<Unit>();
		for (Unit u : units) {
			if (u.intersectsScreenSpace(x0, y0, x1, y1)) {
				result.add(u);
			}
		}
		return result;
	}
	
	public List<Entity> getEntities(double x0, double y0, double z0, double x1, double y1, double z1) {
		return blockmap.getEntities(x0, y0, z0, x1, y1, z1);
	}
	
	public boolean wallBlocks(double x0, double y0, double z0, double x1, double y1, double z1) {
		int xx0 = (int) Math.floor(x0 / 16);
		int yy0 = (int) Math.floor(y0 / 16);
		int xx1 = (int) Math.floor(x1 / 16);
		int yy1 = (int) Math.floor(y1 / 16);
		
		if (xx0 < 0) xx0 = 0;
		if (yy0 < 0) yy0 = 0;
		if (xx1 >= xs) xx1 = xs - 1;
		if (yy1 >= ys) yy1 = ys - 1;
		
		for (int y = yy0; y <= yy1; y++) {
			for (int x = xx0; x <= xx1; x++) {
				if (tiles[x + y * xs] == 1) return true;
			}
		}
		return false;
	}
	
	public void renderBg(Bitmap bm, int xScroll, int yScroll) {
		int w = bm.w / 24 + 4;
		int h = bm.h / 6 + 5;
		
		int x0 = xScroll / 24 - 2;
		int y0 = yScroll / 6 - 3;
		int x1 = x0 + w;
		int y1 = y0 + h;
		
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				int xt = x + (y >> 1) + (y & 1);
				int yt = (y >> 1) - x;

				if (xt > 0 && yt > 0 && xt < xs - 3 && yt < ys - 3) {
					if (tiles[xt + yt * xs] == 1) {
						mapSprites.add(new MapSprite(Art.i.mapTiles[0][1], xt * 16, yt * 16));
					}
					bm.draw(Art.i.mapTiles[((yt ^ xt) & 1) == 0 ? 0 : 1][0], x * 24 + (y & 1) * 12, y * 6 - 6);
				}
			}
		}
	}
	
	public void renderShadows(Bitmap bm) {
		for (Sprite s : entities) {
			s.renderShadow(bm);
		}
		
		for (Sprite s : particles) {
			s.renderShadow(bm);
		}
	}
	
	public void renderSprites(Bitmap bm) {
		TreeSet<Sprite> sortedSprites = new TreeSet<Sprite>(spriteComparator);
		for (Sprite s : entities) {
			sortedSprites.add(s);
		}
		for (Sprite s : particles) {
			sortedSprites.add(s);
		}
		sortedSprites.addAll(mapSprites);
		for (Sprite s : sortedSprites) {
			s.render(bm);
		}
	}

	public void explode(Bullet rocket, double x, double y, double z, int dmg, double radius) {
		double r = radius;
		List<Entity> entities = getEntities(x - r, y - r, z - r, x + r, y + r, z + r);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			double xd = e.x - x;
			double yd = e.y - y;
			double zd = (e.z + e.zh / 2) - z;
			
			if (xd * xd + yd * yd + zd * zd < r * r) {
				double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
				xd /= dd;
				yd /= dd;
				zd /= dd;
				dd /= r;
				double falloff = (1 - dd) * 0.5 + 0.5;
				falloff *= 0.5;
				e.handleExplosion(rocket, (int) (dmg * falloff), xd * 5 * (1 - dd), yd * 5 * (1 - dd), zd * 5 * (1 - dd));
			}
		}
		add(new Explosion(x, y, z));
	}
}
