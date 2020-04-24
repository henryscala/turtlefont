package turtlefont.grammar;

import java.util.ArrayList;
import java.util.HashMap;

import turtlefont.geometry.Vector2;
import turtlefont.lispy.Env;
import turtlefont.lispy.Function;
import turtlefont.lispy.Lispy;


public class GrammarParse {
	//store all chars 
	private HashMap<String, GrammarElementList> charMap = new HashMap<>();
	public HashMap<String, GrammarElementList> getCharMap(){
		return charMap; 
	}
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
	//register a char to the system dictionary 
	@SuppressWarnings("unchecked")
	private Function register = (ArrayList<Object> objects)->{

		//workaround, the first element might be double number. Need to convert the double to int
		Object o = objects.get(0);
		String charName; 
		if (o instanceof Double) {
			Double d = (Double)o;
			int i = (int)d.doubleValue();
			charName = String.valueOf(i);
		} else {
			charName = (String)objects.get(0);
		}
		
		//list of list of list 
		ArrayList<ArrayList<ArrayList<Double>>> polyLineList = (ArrayList<ArrayList<ArrayList<Double>>>)objects.get(1);
		GrammarElementList list = toGrammarElementList(polyLineList);
		charMap.put(charName, list);
		return polyLineList;
	};
	private GrammarElementList toGrammarElementList(ArrayList<ArrayList<ArrayList<Double>>> polyLineList) {
		GrammarElementList elementList = new GrammarElementList(); 
		
		for (ArrayList<ArrayList<Double>> o:polyLineList) {
			
			PolyLine pl = new PolyLine();
			for (ArrayList<Double> p:o) {
				
				double x = (double)p.get(0);
				double y = (double)p.get(1);
				Point a= new Point(x,y);
				pl.pointList.add(a); 
			}
			elementList.elementList.add(pl);
		}
		return elementList; 
	}
	@SuppressWarnings("unchecked")
	public GrammarElementList parse2(String program) throws Exception {
		
		
		Object ast = Lispy.parse(program);
		Env globalEnv = Env.standardEnv();
		globalEnv.dict.put("position", position);//extends it to support position function
		globalEnv.dict.put("rotate", rotate);//extends it to support rotate function
		globalEnv.dict.put("register", register);//extends it to support rotate function
		
		//list of list of list 
		ArrayList<ArrayList<ArrayList<Double>>> polyLineList = (ArrayList<ArrayList<ArrayList<Double>>>)Lispy.eval(ast, globalEnv);
		GrammarElementList elementList =  toGrammarElementList(polyLineList);
		
		return elementList;
	}

}
