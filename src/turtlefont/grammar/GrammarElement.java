package turtlefont.grammar;

public abstract class GrammarElement {
	public abstract String toStringHelper(int level);
	public abstract double len(); 
	@Override
	public String toString() {
		return toStringHelper(0);
	}
	//new copy is returned
	abstract public GrammarElement relocate( double x, double y, double width, double height);
	abstract public GrammarElement copy(); 
	
}