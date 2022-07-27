package com.stolpe.commandprocessor;

import com.stolpe.calculatorcore.CalculatorCore;
import com.stolpe.calculatorcore.Complex;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandInterpreter {

    public static final String GET_MANTISSA = "getMantissa";
    private CalculatorCore calculator;
    private Config config = new Config();
    private static final HashMap<String, Method> calculatorCoreMethods = new HashMap<>();

    private static final CommandInterpreter interpreter = new CommandInterpreter();
    public static CommandInterpreter getInstance(){
        return interpreter;
    }
    private CommandInterpreter(){
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
        result.setErrors(errors);
        result.setValue(getMantissa());
        result.setStack(getStack());
        return result;
    }

    private List<String> getStack() {
        return calculator.getStack();
    }

    public String getMantissa() {
        Complex mantissa = getMantissa(getCalculator());
        MathContext mathContext = new MathContext(getConfig().getDecimals(), RoundingMode.HALF_EVEN);
        mantissa.setMathContext(mathContext);
        return mantissa.toString();
    }

    private Complex invokeMethod(final String methodName, final CalculatorCore calculator){
        Complex result = new Complex(0,0);
        Method method = calculatorCoreMethods.get(methodName);
        try {
            result = (Complex) method.invoke(calculator);
        } catch (Exception e) {
            System.err.print(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    private Complex getMantissa(CalculatorCore calculator) {
        Complex result = invokeMethod(GET_MANTISSA, calculator);
        return result;
    }
    private List<String> invokeCommand(Command command, CalculatorCore calculator) {
        List<String> errors = new ArrayList<>();
        Complex value = new Complex(command.getValue());
        if (command.getNewEnter()==1){
            value=null;
        }
        Method method = calculatorCoreMethods.get(command.getCommand());
        try {
            method.invoke(calculator, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            errors.add(e.getMessage());
            e.printStackTrace();
        }
        return errors;
    }

    public void setConfig(Config newConfig) {
        config = newConfig;
    }

    public Config getConfig(){
        return config;
    }

}
