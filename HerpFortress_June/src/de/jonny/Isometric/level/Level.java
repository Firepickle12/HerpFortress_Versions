package de.jonny.Isometric.level;

import java.util.*;

import de.jonny.Isometric.Bitmap;
import de.jonny.Isometric.Sprite;
import de.jonny.Isometric.entity.Entity;
import de.jonny.Isometric.particle.Particle;

public class Level {
	public List<Entity> entities = new ArrayList<Entity>();
	public List<Particle> particles = new ArrayList<Particle>();
	public final int w, h;
	public double maxHeight = 64;
	
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
	}
	
	// Adds Entitiys to the Array List
	public void add(Particle p) {
		particles.add(p);
		p.init(this);
	}
	
	// Adds Entitiys to the Array List
	public void add(Entity e) {
		entities.add(e);
		e.init(this);
	}

	// Nimmt Entitäten und Partikel, sortiert sie und rendert sie anschließend.
	public void render(Bitmap screen) {
		
		// Fügt die Unit Instanzen (die von Sprite erben und somit auch Sprite Instanzen sind) in eine geordnete Liste ein.
		// Diese Unit/Sprite instanzen verfügen bereits über Koordinaten die in der HerpFortess.java deklariert wurden.
		TreeSet<Sprite> sortedSprites = new TreeSet<Sprite>(spriteComparator);
		sortedSprites.addAll(entities);
		sortedSprites.addAll(particles);
		
		// Geht das die Sprite/Unit Instanzen von sortedSprites durch, und führt deren jeweiligen render Methoden aus
		for (Sprite s : sortedSprites) {
			s.render(screen);
		}
		
		// Debug Output
		System.out.println("SortedSprites: " + sortedSprites);
	}
}
