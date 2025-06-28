package de.jonny.Isometric;

public class Mouse {
	public int x, y;
	
	public boolean onScreen;
	
	public boolean b0, b1, b2;
	public boolean b0Clicked;
	public boolean b1Clicked;
	public boolean b2Clicked;
	public boolean b0Released;
	public boolean b1Released;
	public boolean b2Released;
	
	public void update(int x, int y, boolean b0, boolean b1, boolean b2, boolean onScreen) {
		b0Clicked = !this.b0 && b0;
		b1Clicked = !this.b1 && b1;
		b2Clicked = !this.b2 && b2;
		
		b0Released = this.b0 && !b0;
		b1Released = this.b1 && !b1;
		b2Released = this.b2 && !b2;
		
		this.x = x;
		this.y = y;
		this.b0 = b0;
		this.b1 = b1;
		this.b2 = b2;
		this.onScreen = onScreen;
	}
}
