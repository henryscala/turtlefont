package turtlefont.grammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;


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
