package com.stolpe.calculatorcore;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CalculatorCore {
    private final Stack<Complex> stack;

    public List<String> getStack(){
        ArrayList<String> result = new ArrayList<>(stack.size());
        for (Complex complex : stack) {
            result.add(complex.toString());
        }
        return result;
    }
    public void setStack(List<String> newStack){
        stack.clear();
        for (String value : newStack) {
            stack.add(new Complex(value));
        }
    }

    public CalculatorCore() {
        this.stack = new Stack<>();
        stack.push(new Complex(0,0));
        stack.push(new Complex(0,0));
    }

    private FunctionArguments getTwoFunctionArguments() {
        FunctionArguments result = new FunctionArguments();
        if (stack.size()<2)
            throw new InsufficientArgumentsException("Function call needs two arguments.");
        result.setA(stack.pop());
        result.setB(stack.pop());
        return result;
    }

    /**
     * Pushes the number an the stack
     * @param number complex number
     */
    public void enter(Complex number){
        if (number!=null)
            stack.push(number);
    }

    /**
     * Pops the last two elements from the stack,
     * adds them and pushes the result on the stack.
     * @param number complex number
     */
    public void add(Complex number){
        enter(number);
        executeBinaryFunction( (Complex a, Complex b) -> b.plus(a) );
    }

    /**
     * Pops the last two elements from the stack,
     * subtracts the last element from the second last
     * and pushes the result on the stack.
     * @param number complex number
     */
    public void subtract(Complex number){
        enter(number);
        executeBinaryFunction( (Complex a, Complex b) -> b.minus(a) );
    }
    /**
     * Pops the last two elements from the stack,
     * multiplies them and pushes the result on the stack.
     * @param number complex number
     */
    public void multiply(Complex number){
        enter(number);
        executeBinaryFunction( (Complex a, Complex b) -> b.times(a) );
    }

    /**
     * Pops the last two elements from the stack,
     * divides the second last by the last
     * and pushes the result on the stack.
     * @param number complex number
     */
    public void divide(Complex number){
        enter(number);
        executeBinaryFunction( (Complex a, Complex b) -> b.divides(a) );
    }

    /**
     * Get the last element from the stack
     * @return complex number
     */
    public Complex getMantissa(){
        return new Complex(stack.peek());
    }

    private void executeBinaryFunction(FunctionBinary command){
        FunctionArguments arguments = getTwoFunctionArguments();
        Complex result = command.execute(arguments.getA(), arguments.getB());
        enter(result);
    }

    @FunctionalInterface
    interface FunctionBinary {
        Complex execute(Complex a, Complex b);
    }

    @FunctionalInterface
    interface FunctionUnary {
        Complex execute(Complex a);
    }

}
