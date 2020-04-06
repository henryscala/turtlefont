package turtlefont.grammar;

public class Point extends GrammarElement{
	static final String tag = "point";
	public double x; 
	public double y; 
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y; 
	}
	public Point() {
		this(0,0);
	}
	public double distance(Point p) {
		double dx = x-p.x;
		double dy = y-p.y;
		return Math.sqrt(dx*dx+dy*dy);
	}
	@Override
	public String toStringHelper(int level) {
		StringBuilder sbLevel = new StringBuilder(); 
		for (int i=0;i<level;i++) {
			sbLevel.append("    "); 
		}
		String strLevel = sbLevel.toString(); 
		StringBuilder sb = new StringBuilder(); 
		sb.append(String.format("%s %f %f\n", strLevel,x,y));
		return sb.toString();
	}

	@Override
	public double len() {
		// TODO Auto-generated method stub
		return 0;
	}

}