package turtlefont.lispy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Lispy {
	//"Read a Scheme expression from a string."
	public static Object parse(String program) throws Exception {    
		String[] tokenArray = tokenize(program);
		ArrayList<String> tokenList = new ArrayList<String>();
		tokenList.addAll(Arrays.asList(tokenArray));
	    return readFromTokens(tokenList);
	}
	
	// "Evaluate an expression in an environment."
	//@SuppressWarnings("unchecked")
	@SuppressWarnings("unchecked")
	public static Object eval(Object x, Env env) throws Exception {
		if (x instanceof String) {
			String symbol = (String)x; 
			Env e = env.find(symbol);
			return e.dict.get(x); 
		}
		
		if (!(x instanceof ArrayList<?>)) {
			return x; 
		}
		
		ArrayList<Object> list = (ArrayList<Object>)x;
		String firstKeyword = (String)list.get(0);
		if (firstKeyword.equals("quote")) {
			return first(rest(list));
		}
		
		if (firstKeyword.equals("if")) {
			Object test = first(rest(list));
			
			Object alt1 = first(rest(rest(list)));
			Object alt2 = first(rest(rest(rest(list))));
			if ((boolean)eval(test,env)) {
				return eval(alt1,env);
			} else {
				return eval(alt2,env);
			}
		}
		if (firstKeyword.equals("define")) {
			Object var = first(rest(list));
			Object expr = first(rest(rest(list))); 
			Object val = eval(expr,env);
			env.dict.put((String)var, val);
			return val; 
		}
		if (firstKeyword.equals("set!")) {
			Object var = first(rest(list));
			Object expr = first(rest(rest(list))); 
			Object val = eval(expr,env);
			Env e = env.find((String)var);
			e.dict.put((String)var, val);
			return val; 
		}
		if (firstKeyword.equals("lambda")) {
			Object params = first(rest(list));
			Object body = first(rest(rest(list))); 

			return new Procedure((ArrayList<String>)params,body,env); 
		}
		Function proc = (Function)eval(firstKeyword, env);
		ArrayList<Object> args = new ArrayList<Object>();
		ArrayList<Object> restList = rest(list);
		for(int i=0;i<restList.size();i++) {
			args.add(eval(restList.get(i),env));
		}
		return proc.call(args);
	}
	
	public static String lispStr(Object o) {
		if (o instanceof ArrayList<?>) {
			ArrayList<?> list = (ArrayList<?>)o;
			return '(' + list.stream().map(x->lispStr(x)).collect(Collectors.joining(" ")) + ')';
		}
		return o.toString();
	}
	
	private static <T> T first(ArrayList<T> list){
		return list.get(0);
	}
	private static <T> ArrayList<T> rest(ArrayList<T> list){
		ArrayList<T> newList = new ArrayList<T>();
		for(int i=1;i<list.size();i++) {
			newList.add(list.get(i));
		}
		return newList; 
	}
	//"Convert a string into a list of tokens."
	private static String[] tokenize(String s) {
		s = s.replaceAll("\\(", " ( ");
		s = s.replaceAll("\\)", " ) ");
		String[] tokens = s.split("\\s");
		Object[]  tokenArray = Arrays.stream(tokens).filter(x->x.trim().length() != 0).toArray();
		tokens = new String[tokenArray.length];
		int i = 0; 
		for (Object token:tokenArray) {
			tokens[i++]=(String)token;
		}
		return tokens; 
	}
	
	//"Numbers become numbers; every other token is a symbol."
	private static Object atom(String token) {
		try {
			double d = Double.valueOf(token);
			return d; 
		} catch (Exception e) {
			return token;
		}
	}
	
	//"Read an expression from a sequence of tokens."
	private static Object readFromTokens(ArrayList<String> tokens) throws Exception{
		if (tokens == null || tokens.size() == 0) {
			throw new Exception("unexpected EOF while reading"); 
		}

		String token = tokens.get(0); 
		tokens.remove(0);
		
		if (token.equals("(")) {
			ArrayList<Object> list = new ArrayList<Object>();
			while ( !tokens.get(0).equals(")")){
				Object o = readFromTokens(tokens);
				list.add(o);
			}
			tokens.remove(0);
			return list;
		} else if (token.equals(")")) {
			throw new Exception("unexpected )");
		} else {
			return atom(token);
		}
	}
	    



	    
}
