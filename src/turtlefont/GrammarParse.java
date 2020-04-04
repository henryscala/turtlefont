package turtlefont;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;


abstract class GrammarElement {
	abstract String toStringHelper(int level);
	abstract double len(); 
	@Override
	public String toString() {
		return toStringHelper(0);
	}
}

class GrammarElementList extends GrammarElement {
	static final String tag = "list";
	ArrayList<GrammarElement> elementList = new ArrayList<GrammarElement>();
	
	@Override
	public String toStringHelper(int level) {
		StringBuilder sbLevel = new StringBuilder(); 
		for (int i=0;i<level;i++) {
			sbLevel.append("    "); 
		}
		String strLevel = sbLevel.toString(); 
		StringBuilder sb = new StringBuilder(); 
		sb.append(String.format("%s%s", strLevel,"list{\n"));
		
		for(GrammarElement elem:elementList) {
			sb.append(elem.toStringHelper(level+1));
		}			
		
		sb.append(String.format("%s%s",strLevel, "}\n"));
		return sb.toString();
	}

	@Override
	double len() {
		return elementList.stream().mapToDouble(e->e.len()).sum();
	}
}

class Point extends GrammarElement{
	static final String tag = "point";
	double x; 
	double y; 
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
	double len() {
		// TODO Auto-generated method stub
		return 0;
	}

}

class Variable extends GrammarElement {
	static final String tag = "var"; 
	String name; 
	GrammarElement grammarElement; 
	@Override
	public String toStringHelper(int level) {
		StringBuilder sbLevel = new StringBuilder(); 
		for (int i=0;i<level;i++) {
			sbLevel.append("    "); 
		}
		String strLevel = sbLevel.toString(); 
		StringBuilder sb = new StringBuilder(); 
		sb.append(String.format("%s %s = %s\n", strLevel,name, toStringHelper(level+1)));
		return sb.toString();
	}
	@Override
	double len() {
		// TODO Auto-generated method stub
		return grammarElement.len();
	}
}

class PolyLine extends GrammarElement {
	static final String tag = "polyline";
	ArrayList<Point> pointList = new ArrayList<Point>();
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
	double len() {
		Point point = pointList.get(0);
		double d=0;
		for(int i=1;i<pointList.size();i++) {
			Point pointNext = pointList.get(i);
			d+=point.distance(pointNext);
			point = pointNext; 
		}
		return d;
	}
}

public class GrammarParse {
	private static String[] keywords = {PolyLine.tag, Point.tag, GrammarElementList.tag, Variable.tag};
	private static HashSet<String> keywordsSet = new HashSet<String>();
	static {
		keywordsSet.addAll(Arrays.asList(keywords)); 
	}
	HashMap<String,GrammarElement> variableMap = new HashMap<String,GrammarElement>(); 
	private GrammarElement parseGrammarElement(SExpr sexpr) throws Exception {
		if (sexpr.isAtom) {
			throw new Exception("atom makes no sense here"); 
		}
		SExpr firstChild = sexpr.items.get(0); 
		if (!firstChild.isAtom) {
			throw new Exception("first child must be a atom"); 
		}
		if (firstChild.atom.equals(Point.tag)) {
			SExpr x = sexpr.items.get(1); 
			SExpr y = sexpr.items.get(2);
			Point point = new Point(); 
			point.x = Double.valueOf(x.atom); 
			point.y = Double.valueOf(y.atom); 
			return point; 
		}
		if (firstChild.atom.equals(Variable.tag)) {
			SExpr name = sexpr.items.get(1); 
			SExpr value = sexpr.items.get(2);
			Variable var = new Variable(); 
			var.name = name.atom; 
			var.grammarElement = parseGrammarElement(value); 
			return var; 
		}
		if (firstChild.atom.equals(PolyLine.tag)) {
			PolyLine pl = new PolyLine(); 
			for (int i=1;i<sexpr.items.size();i++) {
				pl.pointList.add((Point)parseGrammarElement(sexpr.items.get(i)));
			}
			
			return pl; 
		}
		if (firstChild.atom.equals(GrammarElementList.tag)) {
			GrammarElementList grammarElementList = new GrammarElementList();
			for (int i=1;i<sexpr.items.size();i++) {
				SExpr child = sexpr.items.get(i);
				if (keywordsSet.contains(child.atom)) {
					GrammarElement childElement = parseGrammarElement(child);
					grammarElementList.elementList.add(childElement); 
				} else if (variableMap.containsKey(child.atom)) {
					grammarElementList.elementList.add(variableMap.get(child.atom)); 
				} else {
					throw new Exception("unknown item " + child.atom);
				}
			}
			return grammarElementList; 
		}
		throw new Exception("unknown tag "+firstChild.atom); 
	}
	private GrammarElementList parseList(SExpr sexpr) throws Exception {
		if (!sexpr.items.get(0).atom.equals(GrammarElementList.tag)) {
			throw new Exception("the element should be a list");
		} 
		return (GrammarElementList)parseGrammarElement(sexpr);
	}
	public HashMap<String, GrammarElement> parse(ArrayList<SExpr> sexprList) throws Exception {
		variableMap.clear();
		for (int i=0;i<sexprList.size();i++) {
			SExpr sexpr = sexprList.get(i); 
			if (!sexpr.isAtom) {
				String name = sexpr.items.get(0).atom;
				if (name.equals(Variable.tag)) {
					GrammarElement element = parseGrammarElement(sexpr.items.get(2));
					variableMap.put(sexpr.items.get(1).atom, element);
				}
				
			}
		}
		return variableMap; 
	}

}
