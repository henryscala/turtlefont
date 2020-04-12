package turtlefont.grammar;

public abstract class GrammarElement {
	public abstract String toStringHelper(int level);
	public abstract double len(); 
	@Override
	public String toString() {
		return toStringHelper(0);
	}
	
	public static void relocate(GrammarElement element, double x, double y, double width, double height) {
		if (element instanceof GrammarElementList) {
			GrammarElementList list = (GrammarElementList)element;
			for(int i=0;i<list.elementList.size();i++) {
				GrammarElement child = list.elementList.get(i);
				relocate(child,x,y,width,height);		
			}
			return; 
		}
		if (element instanceof PolyLine) {
			PolyLine pl =  (PolyLine)element; 
			
	
			for (int i=0;i<pl.pointList.size();i++) {
				Point pointNext = pl.pointList.get(i);
				relocate(pointNext,x,y,width,height);
				
			}
			return; 
		}
		if (element instanceof Point) {
			Point point = (Point)element; 
			double x1 = (point.x*width)+x;
			double y1 = (point.y*height)+y;
			point.x = x1; 
			point.y = y1; 
			return; 
		}
	}
}