package com.stolpe.commandprocessor;

import com.stolpe.calculatorcore.CalculatorCore;
import com.stolpe.calculatorcore.Complex;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandInterpreter {
    private CalculatorCore calculator;
    private static final HashMap<String, Method> calculatorCoreMethods = new HashMap<>();

    public CommandInterpreter(){
        setCalculator(new CalculatorCore());
        checkCalculatorCoreMethods();
    }

    private void checkCalculatorCoreMethods() {
        Method[] methods = getCalculator().getClass().getMethods();
        if (methods.length == 0){
            throw(new RuntimeException("No CalculatorCore available!"));
        }
        if (calculatorCoreMethods.isEmpty()){
            populateCalculatorCoreMethods(methods);
        }
    }

    private void populateCalculatorCoreMethods(Method[] methods) {
        for (Method method : methods) {
            calculatorCoreMethods.put(method.getName(), method);
        }
    }

    public CalculatorCore getCalculator() {
        return calculator;
    }

    public void setCalculator(CalculatorCore calculator) {
        this.calculator = calculator;
    }

    public Command processCommand(Command command) {
        Command result = new Command();
        CalculatorCore calculator = getCalculator();
        List<String> errors = invokeCommand(command, calculator);
        Complex mantissa = getMantissa(calculator);
        result.setErrors(errors);
        result.setValue(mantissa.toString());
        return result;
    }

    private Complex getMantissa(CalculatorCore calculator) {
        Complex result = new Complex(0,0);
        Method method = calculatorCoreMethods.get("getMantissa");
        try {
            result = (Complex) method.invoke(calculator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<String> invokeCommand(Command command, CalculatorCore calculator) {
        Method method = calculatorCoreMethods.get(command.getCommand());
        List<String> errors = new ArrayList<>();
        Complex value = new Complex(command.getValue());
        try {
            method.invoke(calculator, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            errors.add(e.getMessage());
            e.printStackTrace();
        }
        return errors;
    }
}
