package turtlefont;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import turtlefont.grammar.GrammarElement;
import turtlefont.grammar.GrammarParse;
import turtlefont.grammar.SExpr;
import turtlefont.lispy.Env;
import turtlefont.lispy.Lispy;
public class GrammarParseTest {
	@Test
	public void testSExprParse() throws Exception{
		String grammar = "(var line1 (polyline (point 1 2) (point 3 4) (point 5 6))) "
				+ "(var line2 (polyline (point 7 7) (point 8 8) (point 9 9)))"
				+ "(list line1 line2)" ; 
		//String grammar = "(point 1 2) (point 3 4) (point 5 6) ";
		ArrayList<SExpr> sexprList = SExpr.parse(grammar); 
		for (int i=0;i<sexprList.size();i++) {
			System.out.println(sexprList.get(i));
		}
	}
	@Test
	public void testGrammarParse() throws Exception{
		String grammar = "(var line1 (polyline (point 1 2) (point 3 4) (point 5 6))) "
				+ "(var line2 (polyline (point 7 7) (point 8 8) (point 9 9)))"
				+ "(var all (list line1 line2))" ; 
		//String grammar = "(point 1 2) (point 3 4) (point 5 6) ";
		ArrayList<SExpr> sexprList = SExpr.parse(grammar);
		GrammarParse parse = new GrammarParse(); 
		HashMap<String,GrammarElement> map = parse.parse(sexprList);
		System.out.println(map);
	}
	@Test
	public void testLispy() throws Exception{
		String program = "(or true false false ) "; 
		//String program = "(begin (define l 1) (set! l (+ 2 3) )) ";
		Object ast = Lispy.parse(program);
		Object result = Lispy.eval( ast, Env.standardEnv() );
		System.out.println( Lispy.lispStr( result ));
	}
}
