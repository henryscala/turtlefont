package turtlefont.grammar;

import java.util.ArrayList;

public class GrammarElementList extends GrammarElement {
	static final String tag = "list";
	public ArrayList<GrammarElement> elementList = new ArrayList<GrammarElement>();
	
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
	public double len() {
		return elementList.stream().mapToDouble(e->e.len()).sum();
	}

	@Override
	public GrammarElement relocate(double x, double y, double width, double height) {
		GrammarElementList list = (GrammarElementList)this.copy();
		for(int i=0;i<list.elementList.size();i++) {
			GrammarElement child = list.elementList.get(i);
			child=child.relocate(x, y, width, height);		
			list.elementList.set(i, child);
		}
		return list; 
	}

	@Override
	public GrammarElement copy() {
		GrammarElementList list = new GrammarElementList(); 
		for (GrammarElement e:elementList) {
			list.elementList.add(e.copy());
		}
		
		return list; 
	}
}