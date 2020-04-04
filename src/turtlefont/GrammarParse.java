package turtlefont;

import java.util.ArrayList;

class GrammarElement {
	
}

class GrammarElementList extends GrammarElement {
	static final String tag = "list";
	ArrayList<GrammarElement> elementList = new ArrayList<GrammarElement>();
}

class Point extends GrammarElement{
	static final String tag = "point";
	double x; 
	double y; 
	@Override
	public String toString() {
		return String.format("(%d,%d)", x,y);
	}
}

class Variable extends GrammarElement {
	static final String tag = "var"; 
	String name; 
	GrammarElement grammarElement; 
}

class PolyLine extends GrammarElement {
	static final String tag = "polyline";
	ArrayList<Point> pointList = new ArrayList<Point>();
}

public class GrammarParse {
	ArrayList<String> wordStack = new ArrayList<String>(); 
	public GrammarElementList parse(String grammar) {
		GrammarElementList grammarElementList = new GrammarElementList(); 
		
		return grammarElementList; 
	}
}
