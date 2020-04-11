package turtlefont.lispy;

import java.util.ArrayList;
import java.util.HashMap;

public class Env {
	//public static Env globalEnv = standardEnv(); 
	public HashMap<String,Object> dict = new HashMap<String,Object>();
	public Env outer; 
	public Env() {
		
	}
	public Env(ArrayList<String> parameters, ArrayList<Object> arguments, Env outer) {
		this.outer = outer; 
		for(int i=0;i<parameters.size();i++) {
			dict.put(parameters.get(i), arguments.get(i));
		}
	}
	//"Find the innermost Env where var appears."
	public Env find( String var) throws Exception {
        if(dict.containsKey(var)) {
        	return this;
        }
        if(outer == null) {
        	throw new Exception("cannot find a Env for this variable "+var);
        }
        return outer.find(var);
	}
	public static Env standardEnv() {
		Env env = new Env();
		env.dict.put("-", subtract);
		env.dict.put("+", add);
		env.dict.put("*", mul);
		env.dict.put("/", div);
		env.dict.put(">", gt);
		env.dict.put("<", lt);
		env.dict.put("<=", le);
		env.dict.put(">=", ge);
		env.dict.put("=", eq);
		env.dict.put("true",true);
		env.dict.put("false",false);
		return env; 
	}
	
	private static Function eq = (ArrayList<Object> objects)->{
		Double d0 = (Double)objects.get(0);
		for(int i=1;i<objects.size();i++) {
			if (!d0.equals(objects.get(i))) {
				return false;
			}
			d0 = (Double)objects.get(i);
		}
		return true; 
	};
	
	private static Function ge = (ArrayList<Object> objects)->{
		double d0 = (Double)objects.get(0);
		for(int i=1;i<objects.size();i++) {
			if (d0 <= (Double)objects.get(i)) {
				return false;
			}
			d0 = (Double)objects.get(i);
		}
		return true; 
	};
	
	private static Function le = (ArrayList<Object> objects)->{
		double d0 = (Double)objects.get(0);
		for(int i=1;i<objects.size();i++) {
			if (d0 >= (Double)objects.get(i)) {
				return false;
			}
			d0 = (Double)objects.get(i);
		}
		return true; 
	};
	
	private static Function lt = (ArrayList<Object> objects)->{
		double d0 = (Double)objects.get(0);
		for(int i=1;i<objects.size();i++) {
			if (d0 > (Double)objects.get(i)) {
				return false;
			}
			d0 = (Double)objects.get(i);
		}
		return true; 
	};
	
	private static Function gt = (ArrayList<Object> objects)->{
		double d0 = (Double)objects.get(0);
		for(int i=1;i<objects.size();i++) {
			if (d0 < (Double)objects.get(i)) {
				return false;
			}
			d0 = (Double)objects.get(i);
		}
		return true; 
	};
	
	private static Function div = (ArrayList<Object> objects)->{
		double result = (Double)objects.get(0); 
		for(int i=1;i<objects.size();i++ ) {
			Object o = objects.get(i); 
			result /= (Double)o;
		}
		return result; 
	};
	
	private static Function mul = (ArrayList<Object> objects)->{
		double result = 1; 
		for(Object o:objects) {
			result *= (Double)o;
		}
		return result; 
	};
	
	private static Function subtract = (ArrayList<Object> objects)->{
		double result = (Double)objects.get(0); 
		for(int i=1;i<objects.size();i++ ) {
			Object o = objects.get(i); 
			result -= (Double)o;
		}
		return result; 
	};
	
	private static Function add = (ArrayList<Object> objects)->{
		double result = 0; 
		for(Object o:objects) {
			result += (Double)o;
		}
		return result; 
	};

}

interface Function{
	public Object call(ArrayList<Object> list) ;
}

class Procedure{
    public ArrayList<String> params ;
    public Object body ; 
    public Env env;
    public Procedure(ArrayList<String> params, Object body, Env env){
        this.env = env; 
        this.params = params;
        this.body = body; 
    }
    public Object call( ArrayList<Object> args) throws Exception {
        return Lispy.eval(body, new Env(params, args, env)); 
    }
}
   