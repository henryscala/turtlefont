package turtlefont.grammar;

import java.util.ArrayList;

public class PolyLine extends GrammarElement {
	static final String tag = "polyline";
	public ArrayList<Point> pointList = new ArrayList<Point>();
	@Override
	public String toStringHelper(int level) {
		StringBuilder sbLevel = new StringBuilder(); 
		for (int i=0;i<level;i++) {
			sbLevel.append("    "); 
		}
		String strLevel = sbLevel.toString(); 
		StringBuilder sb = new StringBuilder(); 
		sb.append(String.format("%s%s", strLevel,"polyline{\n"));

		for(Point elem:pointList) {
			sb.append(elem.toStringHelper(level+1));
		}			
		
		sb.append(String.format("%s%s",strLevel, "}\n"));
		return sb.toString();
	}
	@Override
	public double len() {
		Point point = pointList.get(0);
		double d=0;
		for(int i=1;i<pointList.size();i++) {
			Point pointNext = pointList.get(i);
			d+=point.distance(pointNext);
			point = pointNext; 
		}
		return d;
	}
	@Override
	public GrammarElement relocate(double x, double y, double width, double height) {
		PolyLine pl =  (PolyLine)this.copy(); 
				
		for (int i=0;i<pl.pointList.size();i++) {
			Point pointNext = pl.pointList.get(i);
			pointNext=(Point)pointNext.relocate(x, y, width, height);			
			pl.pointList.set(i, pointNext);
		}
		return pl;
	}
	@Override
	public GrammarElement copy() {
		PolyLine pl = new PolyLine();
		for (Point p:this.pointList) {
			pl.pointList.add((Point)p.copy());
		}
		return pl;
	}
}