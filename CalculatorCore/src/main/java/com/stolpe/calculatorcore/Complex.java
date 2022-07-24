package com.stolpe.calculatorcore;

import org.giac.xcaspad.Calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;


public class Complex {
    public static final String PLUS = "+";
    public static final String IMAGINARY = "i";
    public static final String MINUS = "-";
    public static final String TIMES = "*";
    public static final String DIVIDE = "/";
    private final BigDecimal re;   // the real part
    private final BigDecimal im;   // the imaginary part
    private MathContext mathContext = MathContext.UNLIMITED;

    public MathContext getMathContext() {
        return mathContext;
    }

    public void setMathContext(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    // create a new object with the given real and imaginary parts
    public Complex(double real, double imag) {
        re = BigDecimal.valueOf(real);
        im = BigDecimal.valueOf(imag);
    }

    public Complex(Complex complex){
        re = complex.re;
        im = complex.im;
        mathContext=complex.getMathContext();
    }

    private BigDecimal applySign(String complex, String value, BigDecimal operand){
        BigDecimal result = operand;
        int pos = complex.indexOf(value);
        if (pos>0){
            String sign = complex.substring(pos-1, pos);
            if (sign.startsWith("-")){
                result = operand.negate();
            }
        }
        return result;
    }

    public Complex(String complexString){
        BigDecimal re1 = BigDecimal.ZERO;
        BigDecimal im1 = BigDecimal.ZERO;
        if (complexString==null || complexString.equals("")){
            throw new RuntimeException("No valid parameter.");
        }
        complexString = complexString.replaceAll("\\s",""); //strip all spaces
        complexString = complexString.replaceAll("\\*",""); //strip all "*"
        String[] valueComponents = complexString.split("[+-]");
        List<String> componentList = new ArrayList<>(Arrays.asList(valueComponents));
        for (String component : componentList) {
            if (!component.equals("")) {
                if (component.endsWith(IMAGINARY)) {
                    im1 = new BigDecimal(component.substring(0, component.length() - 1));
                    im1 = applySign(complexString, component, im1);
                } else {
                    re1 = new BigDecimal(component);
                    re1 = applySign(complexString, component, re1);
                }
            }
        }
        re = re1;
        im = im1;
    }

    private BigDecimal format(BigDecimal formatted){
        return formatted.round(getMathContext()).stripTrailingZeros();
    }
    // return a string representation of the invoking Complex object
    public String toString() {
        String result = "";
        if (im.compareTo(BigDecimal.ZERO)==0)
            result = format(re) + "";
        if (im.compareTo(BigDecimal.ZERO)!=0 && re.compareTo(BigDecimal.ZERO)==0)
            result = (format(im)) + "i";
        if (im.compareTo(BigDecimal.ZERO)!=0 && re.compareTo(BigDecimal.ZERO)!=0)
            if (im.signum()==-1)
                result = format(re) + ""+ format(im)+ "i";
            else
                result = format(re) + PLUS+ format(im)+ "i";
        return result;
    }

    // return abs/modulus/magnitude
    public double abs() {
        return Math.hypot(re.doubleValue(), im.doubleValue());
    }

    // return angle/phase/argument, normalized to be between -pi and pi
    public double phase() {
        return Math.atan2(im.doubleValue(), re.doubleValue());
    }

    // return a new Complex object whose value is (this + b)
    public Complex plus(Complex b) {
        Complex result = binaryOperation(PLUS, this, b);
        return result;
    }

    public Complex binaryOperation(String operation, Complex a, Complex b){
        Complex result = new Complex(0,0);
        String aString = a.toString();
        String bString = b.toString();
        StringBuilder expression = new StringBuilder("(").append(aString).append(")").append(operation).append("(").append(bString).append(")");
        //expression = new StringBuilder("evalc(").append(expression).append(')');
        //expression = new StringBuilder("simplify(").append(expression).append(')');
        //expression = new StringBuilder("evalf(").append(expression).append(')');
        try {
            String executed = Calculator.calculate(expression.toString());
            result = new Complex(executed);
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    // return a new Complex object whose value is (this - b)
    public Complex minus(Complex b) {
        Complex result = binaryOperation(MINUS, this, b);
        return result;
    }

    // return a new Complex object whose value is (this * b)
    public Complex times(Complex b) {
        Complex result = binaryOperation(TIMES, this, b);
        return result;
    }

    // return a new object whose value is (this * alpha)
    public Complex scale(double alpha) {
        return new Complex(alpha * re.doubleValue(), alpha * im.doubleValue());
    }

    // return a new Complex object whose value is the conjugate of this
    public Complex conjugate() {
        return new Complex(re.doubleValue(), -im.doubleValue());
    }

    // return a new Complex object whose value is the reciprocal of this
    public Complex reciprocal() {
        double scale = re.doubleValue()*re.doubleValue() + im.doubleValue()*im.doubleValue();
        return new Complex(re.doubleValue() / scale, -im.doubleValue() / scale);
    }

    // return the real or imaginary part
    public double re() { return re.doubleValue(); }
    public double im() { return im.doubleValue(); }

    // return a / b
    public Complex divides(Complex b) {
        Complex result = binaryOperation(DIVIDE, this, b);
        return result;
    }

    // return a new Complex object whose value is the complex exponential of this
    public Complex exp() {
        return new Complex(Math.exp(re.doubleValue()) * Math.cos(im.doubleValue()), Math.exp(re.doubleValue()) * Math.sin(im.doubleValue()));
    }

    // return a new Complex object whose value is the complex sine of this
    public Complex sin() {
        return new Complex(Math.sin(re.doubleValue()) * Math.cosh(im.doubleValue()), Math.cos(re.doubleValue()) * Math.sinh(im.doubleValue()));
    }

    // return a new Complex object whose value is the complex cosine of this
    public Complex cos() {
        return new Complex(Math.cos(re.doubleValue()) * Math.cosh(im.doubleValue()), -Math.sin(re.doubleValue()) * Math.sinh(im.doubleValue()));
    }

    // return a new Complex object whose value is the complex tangent of this
    public Complex tan() {
        return sin().divides(cos());
    }

    // See Section 3.3.
    public boolean equals(Object x) {
        boolean result = false;
        if (x != null && this.getClass() == x.getClass()) {
            Complex that = (Complex) x;
            result = (this.re.equals(that.re)) && (this.im.equals(that.im));
        }
        return result;
    }

    // See Section 3.3.
    public int hashCode() {
        return Objects.hash(re, im);
    }

    // sample client for testing
    public static void main(String[] args) {
    }
}
