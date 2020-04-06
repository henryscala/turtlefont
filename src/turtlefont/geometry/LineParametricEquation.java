package turtlefont.geometry;

public class LineParametricEquation {
	public Vector2 coefficient; 
	public Vector2 costantterm;
	public LineParametricEquation(Vector2 point1, Vector2 point2) {
		costantterm = new Vector2(point1);
		coefficient = point2.subtract(point1);
	}
}
