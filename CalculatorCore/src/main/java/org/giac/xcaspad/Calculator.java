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
            //String name = System.mapLibraryName("javagiac");
            System.loadLibrary("crystax");
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
