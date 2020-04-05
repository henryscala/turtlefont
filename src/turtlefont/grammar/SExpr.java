package turtlefont.grammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
(polyline (point 1 2) (point 3 4) (point 5 6)) 
 */
public class SExpr {
	private static HashSet<Character> blankChars = new HashSet<Character>(); 
	private static HashSet<Character> delimiters = new HashSet<Character>(); 
	private static char leftParen = '('; 
	private static char rightParen = ')'; 
	static {
		blankChars.addAll(Arrays.asList( new Character[] {' ','\t','\r','\n'} ));
		delimiters.addAll(Arrays.asList( new Character[] {leftParen,rightParen} ));
	}
	
	boolean isAtom = true; 
	String atom; 
	ArrayList<SExpr> items = new ArrayList<SExpr>();
	
	private String toStringHelper(int level) {
		StringBuilder sbLevel = new StringBuilder(); 
		for (int i=0;i<level;i++) {
			sbLevel.append("    "); 
		}
		String strLevel = sbLevel.toString(); 
		StringBuilder sb = new StringBuilder(); 
		sb.append(String.format("%s%s", strLevel,"SExpr{\n"));
		if (isAtom) {
			sb.append(String.format("%s%s",strLevel,  atom));
			sb.append("\n"); 
		} else {
			for(SExpr expr:items) {
				sb.append(expr.toStringHelper(level+1));
			}			
		}
		sb.append(String.format("%s%s",strLevel, "}\n"));
		return sb.toString();
	}
	@Override 
	public String toString() {
		 return toStringHelper(0);
	}
	
	private static int findLastLeftParenIndex( ArrayList<Object> list){
		for (int i=list.size()-1; i>=0; i--) {
			Object obj = list.get(i);
			if (obj instanceof Character) {
				char ch = (Character)obj;
				if (ch == leftParen) {
					return i;
				}
			}
		}
		return -1;
	}
	private static ArrayList<SExpr> wordsToSExprList(String words){
		String[] wordArray = words.trim().split("\\s");
		ArrayList<SExpr> list = new ArrayList<SExpr>(); 
		for (String word:wordArray) {
			if(word.trim().length()>0) {
				SExpr atom = new SExpr();
				atom.isAtom = true; 
				atom.atom = word;
				list.add(atom);
			}
		}
		return list; 
	}
	private static SExpr constructSExpr(ArrayList<Object> list,int fromIndex) {
		StringBuilder sb = new StringBuilder();
		SExpr sexpr = new SExpr(); 
		sexpr.isAtom = false; 
		for (int i=fromIndex;i<list.size();i++) {
			Object obj = list.get(i); 
			if (obj instanceof Character) {
				sb.append(obj);
			} else {
				if (sb.length() > 0) {
					ArrayList<SExpr> sexprList = wordsToSExprList(sb.toString()); 
					sexpr.items.addAll(sexprList);
					sb.delete(0, sb.length());
				}
				sexpr.items.add((SExpr)obj);
			}
		}
		if (sb.length() > 0) {
			ArrayList<SExpr> sexprList = wordsToSExprList(sb.toString()); 
			sexpr.items.addAll(sexprList);
			sb.delete(0, sb.length());
		}
		return sexpr; 
	}
	public static ArrayList<SExpr> parse(String grammar) throws Exception{
		ArrayList<Object> stack = new ArrayList<Object>(); 
		for (int i=0;i<grammar.length();i++) {
			char ch = grammar.charAt(i); 
			if (ch == rightParen) {
				int lastLeftParenIndex = findLastLeftParenIndex(stack);
				if (lastLeftParenIndex < 0) {
					throw new Exception("no matching left paren found");
				}
				SExpr sexpr = constructSExpr(stack,lastLeftParenIndex+1);
				for (int k=stack.size()-1;k>=lastLeftParenIndex;k--) {
					stack.remove(k);
				}
				stack.add(sexpr);
				continue; 
			}
			stack.add(ch);
		}
		
		ArrayList<SExpr> exprList = new ArrayList<SExpr>(); 
		for(int i=0;i<stack.size();i++) {
			Object obj = stack.get(i); 
			if (obj instanceof SExpr) {
				exprList.add((SExpr)obj);
			} else {
				Character ch = (Character)obj; 
				if (!blankChars.contains(ch)) {
					throw new Exception("unknown char "+ch);
				}
			}
		}
		return exprList; 
	}
}
