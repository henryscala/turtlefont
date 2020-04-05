package turtlefont.grammar;

public abstract class GrammarElement {
	public abstract String toStringHelper(int level);
	public abstract double len(); 
	@Override
	public String toString() {
		return toStringHelper(0);
	}
}