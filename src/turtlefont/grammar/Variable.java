package turtlefont.grammar;

public class Variable extends GrammarElement {
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
	public double len() {
		// TODO Auto-generated method stub
		return grammarElement.len();
	}
}