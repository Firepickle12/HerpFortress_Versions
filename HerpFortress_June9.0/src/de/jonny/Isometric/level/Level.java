package de.jonny.Isometric.level;

import java.util.*;

import de.jonny.Isometric.Art;
import de.jonny.Isometric.Bitmap;
import de.jonny.Isometric.Sprite;
import de.jonny.Isometric.entity.Entity;
import de.jonny.Isometric.entity.Rocket;
import de.jonny.Isometric.particle.Explosion;
import de.jonny.Isometric.particle.Particle;

public class Level {
	public static Random random = new Random();
	public List<Entity> entities = new ArrayList<Entity>();
	public List<Particle> particles = new ArrayList<Particle>();
	public final int w, h;
	public final int xs, ys;
	public double maxHeight = 64;
	
	public int[] tiles;
	
	public Blockmap blockmap;
	
	// Sortieralgorithmus, da Sprites ein Objekt ist und TreeSets nur Datentypen ohne Hilfe sortieren können.
	private Comparator<Sprite> spriteComparator = new Comparator<Sprite>() {
		public int compare(Sprite s0, Sprite s1) {
			if (s0.y < s1.y) return -1;
			if (s0.y > s1.y) return 1;
			if (s0.z < s1.z) return -1;
			if (s0.z > s1.z) return 1;
			if (s0.x < s1.x) return -1;
			if (s0.x > s1.x) return 1;
			return 0;
		}
	};
	
	// Konstruktor von Level mit einfluss auf width und height
	public Level(int w, int h) {
		this.w = w;
		this.h = h;
		
		xs = w / 8;
		ys = h / 8;
		blockmap = new Blockmap(w, h, 32);
		
		tiles = new int[xs * ys];
		for (int i = 0; i < xs * ys; i++) {
			// tiles[i] = random.nextInt(10) / 5;
			tiles[i] = 0;
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
	
	public List<Entity> getEntities(double x0, double y0, double z0, double x1, double y1, double z1) {
		return blockmap.getEntities(x0, y0, z0, x1, y1, z1);
	}
	
	public void renderBg(Bitmap bm) {
		for (int y = 0; y < ys; y++) {
			for (int x = 0; x < xs; x++) {
				if (tiles[x + y * xs] == 0) {
					bm.draw(Art.i.mapTiles[0][0], x * 8, y * 8);
				} else {
					bm.draw(Art.i.mapTiles[1][0], x * 8, y * 8);
				}
			}
		}
	}
	
	
	public void renderShadows(Bitmap bm) {
		TreeSet<Sprite> sortedSprites = new TreeSet<Sprite>(spriteComparator);
		sortedSprites.addAll(entities);
		sortedSprites.addAll(particles);
		
		for (Sprite s : sortedSprites) {
			s.renderShadow(bm);
		}
	}
	
	public void renderSprites(Bitmap bm) {
		TreeSet<Sprite> sortedSprites = new TreeSet<Sprite>(spriteComparator);
		sortedSprites.addAll(entities);
		sortedSprites.addAll(particles);
		
		for (Sprite s : sortedSprites) {
			s.render(bm);
		}
	}

	public void explode(Rocket rocket, double x, double y, double z, int dmg, double radius) {
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
