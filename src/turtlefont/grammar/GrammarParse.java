package turtlefont.grammar;

import java.util.ArrayList;


import turtlefont.lispy.Env;
import turtlefont.lispy.Lispy;


public class GrammarParse {
	@SuppressWarnings("unchecked")
	public GrammarElementList parse2(String program) throws Exception {
		GrammarElementList elementList = new GrammarElementList(); 
		
		Object ast = Lispy.parse(program);
		Env globalEnv = Env.standardEnv();
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
