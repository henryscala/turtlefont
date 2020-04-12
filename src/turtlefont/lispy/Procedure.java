package turtlefont.lispy;

import java.util.ArrayList;

//user defined lambda function
public class Procedure{
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