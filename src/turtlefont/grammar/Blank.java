package turtlefont.grammar;

public class Blank extends GrammarElement{
	static final String tag = "blank";
	
	public Blank() {

	}
	
	public double distance(Blank p) {
		return 0;
	}
	@Override
	public String toStringHelper(int level) {
		
		return "[]";
	}

	@Override
	public double len() {
		return 0;
	}
	@Override
	public GrammarElement relocate(double x, double y, double width, double height) {
		return this; 
	}
	@Override
	public GrammarElement copy() {
	
		return this;
	}

}