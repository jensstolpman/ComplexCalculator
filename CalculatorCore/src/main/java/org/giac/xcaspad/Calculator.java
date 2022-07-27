package org.giac.xcaspad;

import javagiac.context;
import javagiac.gen;
import javagiac.giac;

import javax.naming.Context;

/**
 * Created by leonel on 17/10/17.
 * <p>
 * This is the interface to call the native functions from C++ to Java.
 * This turns the input expressions into 2d pretty print results
 */


public class Calculator {

    static {
        try {
            System.loadLibrary("crystax");
        } catch (Throwable e){
            //do nothing. If crystax isn't loaded, but necessary, you will get an Exception while loading javagiac
            //otherwise you will always get an error during unit tests
            //for unit testing crystax isn't necessary, because it will load the .dll, not the .so
        }
        try {
            System.loadLibrary("javagiac");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
        }
    }


    /*this function retrieves the computed result from a math expression*/
    public static String calculate(String input) {
        context ctx=new context();
        gen g = new gen(input,ctx);
        g = giac._evalc(g,ctx);
        g = giac._simplify(g,ctx);
        g = giac._evalf(g,ctx);

        return g.print(ctx);
    }

}
