package turtlefont.grammar;

import java.util.ArrayList;

import turtlefont.geometry.Vector2;
import turtlefont.lispy.Env;
import turtlefont.lispy.Function;
import turtlefont.lispy.Lispy;


public class GrammarParse {
	@SuppressWarnings("unchecked")
	private Function rotate = (ArrayList<Object> objects)->{
		ArrayList<ArrayList<ArrayList<Double>>> newPolyLineList = new ArrayList<ArrayList<ArrayList<Double>>>();
		//list of list of list 
		ArrayList<Object> polyLineList = (ArrayList<Object>)objects.get(0);
		double x = (double)objects.get(1);//rotate relative to this point 
		double y = (double)objects.get(2);
		double degree = (double)objects.get(3);
		
		Vector2 center = new Vector2(x,y); 
		
		
		for (Object o:polyLineList) {
			ArrayList<ArrayList<Double>> newPointList = new ArrayList<ArrayList<Double>>(); 
			
			ArrayList<Object> pointList = (ArrayList<Object>)o;
			
			for (Object p:pointList) {
				ArrayList<Double> newCoordinateList = new ArrayList<Double>();
				ArrayList<Object> coordinateList = (ArrayList<Object>)p;
				x = (double)coordinateList.get(0);
				y = (double)coordinateList.get(1);
				Vector2 vector = new Vector2(x-center.getX(),y-center.getY());
				vector = vector.rotateDegree(degree);
				Vector2 newLocation = center.add(vector);
				newCoordinateList.add(newLocation.getX());
				newCoordinateList.add(newLocation.getY());
				newPointList.add(newCoordinateList);
			}
			newPolyLineList.add(newPointList);
		}
		return newPolyLineList;
	};
	@SuppressWarnings("unchecked")
	private Function position = (ArrayList<Object> objects)->{
		ArrayList<ArrayList<ArrayList<Double>>> newPolyLineList = new ArrayList<ArrayList<ArrayList<Double>>>();
		//list of list of list 
		ArrayList<Object> polyLineList = (ArrayList<Object>)objects.get(0);
		double newx = (double)objects.get(1);
		double newy = (double)objects.get(2);
		double width = (double)objects.get(3);
		double height = (double)objects.get(4);
		
		for (Object o:polyLineList) {
			ArrayList<ArrayList<Double>> newPointList = new ArrayList<ArrayList<Double>>(); 
			
			ArrayList<Object> pointList = (ArrayList<Object>)o;
			
			for (Object p:pointList) {
				ArrayList<Double> newCoordinateList = new ArrayList<Double>();
				ArrayList<Object> coordinateList = (ArrayList<Object>)p;
				double x = (double)coordinateList.get(0);
				double y = (double)coordinateList.get(1);
				x = newx + width*x;
				y = newy + height*y; 
				newCoordinateList.add(x);
				newCoordinateList.add(y);
				newPointList.add(newCoordinateList);
			}
			newPolyLineList.add(newPointList);
		}
		return newPolyLineList;
	};
	@SuppressWarnings("unchecked")
	public GrammarElementList parse2(String program) throws Exception {
		GrammarElementList elementList = new GrammarElementList(); 
		
		Object ast = Lispy.parse(program);
		Env globalEnv = Env.standardEnv();
		globalEnv.dict.put("position", position);//extends it to support position function
		globalEnv.dict.put("rotate", rotate);//extends it to support rotate function
		//list of list of list 
		ArrayList<Object> polyLineList = (ArrayList<Object>)Lispy.eval(ast, globalEnv);
		for (Object o:polyLineList) {
			ArrayList<Object> pointList = (ArrayList<Object>)o;
			PolyLine pl = new PolyLine();
			for (Object p:pointList) {
				ArrayList<Object> coordinateList = (ArrayList<Object>) p;
				double x = (double)coordinateList.get(0);
				double y = (double)coordinateList.get(1);
				Point a= new Point(x,y);
				pl.pointList.add(a); 
			}
			elementList.elementList.add(pl);
		}
		return elementList;
	}

}
