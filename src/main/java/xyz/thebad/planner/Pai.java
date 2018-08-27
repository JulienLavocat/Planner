package xyz.thebad.planner;

public class Pai {

	public int x;
	public int y;
	
	public Pai(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(!(obj instanceof Pai))
			return super.equals(obj);
		else {
			Pai o = (Pai) obj;
			return o.x <= x && o.y <= y;
		}
	}
	
}
