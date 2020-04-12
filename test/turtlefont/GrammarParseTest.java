package turtlefont;


import org.junit.Test;


import turtlefont.lispy.Env;
import turtlefont.lispy.Lispy;
public class GrammarParseTest {

	@Test
	public void testLispy() throws Exception{
		String program ="(begin (var line1 (polyline (point 1 2) (point 3 4) (point 5 6))) "
				+ "(var line2 (polyline (point 7 7) (point 8 8) (point 9 9)))"
				+ "(var picture (list line1 line2)))" ; 
		//String program = "(begin (define l 1) (set! l (+ 2 3) )) ";
		Object ast = Lispy.parse(program);
		Env globalEnv = Env.standardEnv();
		Object result = Lispy.eval( ast, globalEnv) ;
		System.out.println( Lispy.lispStr( result ));
		System.out.println( globalEnv);
	}
}
