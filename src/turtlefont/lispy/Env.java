package turtlefont.lispy;

import java.util.ArrayList;
import java.util.HashMap;

public class Env {

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
		env.dict.put("list?",islist);
		env.dict.put("list",list);
		env.dict.put("append",append);
		env.dict.put("first",first);
		env.dict.put("rest",rest);
		env.dict.put("cons",cons);
		env.dict.put("map",map);
		env.dict.put("length",length);
		env.dict.put("begin", begin);
		env.dict.put("abs",abs);
		env.dict.put("neg",neg);
		env.dict.put("not",not);
		env.dict.put("and",and);
		env.dict.put("or",or);
		env.dict.put("true",true);
		env.dict.put("false",false);
		
		return env; 
	}
	private static Function not = (ArrayList<Object> objects)->{
		Object first = objects.get(0); 
		Boolean d = (Boolean)first;
		return !d; 
	};
	private static Function neg = (ArrayList<Object> objects)->{
		Object first = objects.get(0); 
		Double d = (Double)first;
		return -d; 
	};
	private static Function abs = (ArrayList<Object> objects)->{
		Object first = objects.get(0); 
		Double d = (Double)first;
		return Math.abs(d); 
	};
	private static Function islist = (ArrayList<Object> objects)->{
		Object first = objects.get(0); 
		return first instanceof ArrayList<?>; 
	};
	private static Function list = (ArrayList<Object> objects)->{
		return objects; 
	};
	private static Function begin = (ArrayList<Object> objects)->{
		return objects.get(objects.size()-1); 
	};
	private static Function append = (ArrayList<Object> objects)->{
		@SuppressWarnings("unchecked")
		ArrayList<Object> list = (ArrayList<Object>)objects.get(0);
		for(int i=1;i<objects.size();i++) {
			list.add(objects.get(i));
		}
		return list; 
	};
	private static Function cons = (ArrayList<Object> objects)->{
		@SuppressWarnings("unchecked")
		ArrayList<Object> list = (ArrayList<Object>)objects.get(1);
		ArrayList<Object> newList = new ArrayList<Object>();
		newList.add(objects.get(0));
		newList.addAll(list);
		return newList; 
	};
	private static Function map = (ArrayList<Object> objects)->{
		ArrayList<Object> newList = new ArrayList<Object>();
		
		Object o = objects.get(0);
		@SuppressWarnings("unchecked")
		ArrayList<Object> list = (ArrayList<Object>)objects.get(1);
		
		if (o instanceof Procedure) {
			Procedure f = (Procedure)o;
			for(int i=0;i<list.size();i++) {
				Object arg = list.get(i);
				ArrayList<Object> args = new ArrayList<Object>();
				args.add(arg);
				try {
					newList.add(f.call(args));
				} catch (Exception e) {
					throw new RuntimeException(e); 
				}
			}
			return newList;
		}
		
		
		throw new RuntimeException("not supported procedure"); 
	};
	private static Function rest = (ArrayList<Object> objects)->{
		ArrayList<Object> newList = new ArrayList<Object>();
		@SuppressWarnings("unchecked")
		ArrayList<Object> list = (ArrayList<Object>)objects.get(0);
		for(int i=1;i<list.size();i++) {
			newList.add(list.get(i));
		}
		return newList; 
	};
	private static Function length = (ArrayList<Object> objects)->{
		@SuppressWarnings("unchecked")
		ArrayList<Object> list = (ArrayList<Object>)objects.get(0);
		return list.size();
	};
	private static Function first = (ArrayList<Object> objects)->{
		@SuppressWarnings("unchecked")
		ArrayList<Object> list = (ArrayList<Object>)objects.get(0);
		return list.get(0);
	};
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
	
	private static Function or = (ArrayList<Object> objects)->{
		boolean result = false; 
		for(Object o:objects) {
			result = result || (Boolean)o;
		}
		return result; 
	};
	
	private static Function and = (ArrayList<Object> objects)->{
		boolean result = true; 
		for(Object o:objects) {
			result = result && (Boolean)o;
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
   