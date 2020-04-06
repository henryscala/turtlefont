package turtlefont.geometry;

public class LineCrossPointSolver {
	public LineParametricEquation line1;
	public LineParametricEquation line2;
	public Vector2 calculateCrossPoint() {
		Vector2 column1 = line1.coefficient;
		Vector2 column2 = line2.coefficient.minus();
		
		/*                     [u1]
		 * [column1,column2] * [u2] = b. Calculate the u1 and u2 using Cramer's rule. 
		 */
		Matrix2 matrix = new Matrix2();
		matrix.vector[0]=column1;
		matrix.vector[1]=column2;
//		System.out.println("column1 "+column1);
//		System.out.println("column2 "+column2);
		Vector2 b = line2.costantterm.subtract(line1.costantterm);
//		System.out.println("b "+b);
		double areaBColumn2 = b.determinant(column2);
		double areaColumn1B = column1.determinant(b);
		double areaColumn1Column2 = column1.determinant(column2);
//		System.out.println("d1 "+areaBColumn2);
//		System.out.println("d2 "+areaColumn1B);
//		System.out.println("d3 "+areaColumn1Column2);
		
		double u1 = areaBColumn2/areaColumn1Column2;
		double u2 = areaColumn1B/areaColumn1Column2;
		Vector2 result1=line1.coefficient.scale(u1).add(line1.costantterm);
		Vector2 result2=line2.coefficient.scale(u2).add(line2.costantterm);
//		System.out.println("result2 " + result2);
//		System.out.println("result1 " + result1);
		return result1; 
	}
}
