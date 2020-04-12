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
	@Override
	public String toString() {
		return String.format("Vector2(%f,%f)", elem[X],elem[Y]); 
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
	//calculate the area of the parallelogram formed by the two vectors
	//it is also the determinant
	public double determinant(Vector2 other) {
		return this.getX()*other.getY() - this.getY()*other.getX();
	}
	//in degree not radian
	public Vector2 rotateDegree(double degree) {
		double radian = Math.toRadians(degree);
		double cos = Math.cos(radian);
		double sin = Math.sin(radian);
		Matrix2 m = new Matrix2();
		m.vector[0].setX(cos);
		m.vector[0].setY(sin);
		m.vector[1].setX(-sin);
		m.vector[1].setY(cos);
		return product(m);
	}
	//matrix product a vector
	//matrix contains 2 column vectors
	public Vector2 product(Matrix2 m) {
		Vector2 v = new Vector2();
		v.setX(m.vector[0].getX()*this.getX()+m.vector[1].getX()*this.getY());
		v.setY(m.vector[0].getY()*this.getX()+m.vector[1].getY()*this.getY());
		return v; 	
	}
}
