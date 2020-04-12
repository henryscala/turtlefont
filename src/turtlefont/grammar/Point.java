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
		return 0;
	}
	@Override
	public GrammarElement relocate(double x, double y, double width, double height) {
		Point point = (Point)this.copy(); 
		double x1 = (point.x*width)+x;
		double y1 = (point.y*height)+y;
		point.x = x1; 
		point.y = y1; 
		return point; 
	}
	@Override
	public GrammarElement copy() {
		Point p = new Point(this.x,this.y); 
		return p;
	}

}