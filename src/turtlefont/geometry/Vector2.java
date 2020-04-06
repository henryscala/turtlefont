package turtlefont.geometry;

public class Vector2 {
	public static final int X = 0; 
	public static final int Y = 1; 
	public double elem[]=new double[2];
	public Vector2(double x,double y) {
		elem[X] = x; 
		elem[Y] = y; 
	}
	public Vector2(Vector2 other) {
		elem[X] = other.getX();
		elem[Y] = other.getY(); 
	}
	public Vector2() {
		this(0,0);
	}
	public double getX() {
		return elem[X];
	}
	public double getY() {
		return elem[Y];
	}
	public void setX(double x) {
		elem[X] = x; 
	}
	public void setY(double y) {
		elem[Y] = y;
	}
	public Vector2 scale(double factor) {
		Vector2 v = new Vector2(getX()*factor,getY()*factor);
		return v; 
	}
	public double dotProduct(Vector2 other) {
		return other.getX()*getX()+other.getY()*getY();
	}
	public Vector2 minus() {
		return new Vector2(-getX(),-getY());
	}
	
	public Vector2 add(Vector2 other) {
		return new Vector2(other.getX()+getX(),other.getY()+getY());
	}
	public Vector2 subtract(Vector2 other) {
		return new Vector2(-other.getX()+getX(),-other.getY()+getY());
	}
}
