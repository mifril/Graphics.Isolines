package ru.nsu.vakhrushev.graphics.isolines.model.function;

/**
 * @author Maxim Vakhrushev
 */
public interface Function {
    public double findValue(double x, double y);
    public double getA();
    public double getB();
    public double getC();
    public double getD();
    public double getMax();
    public double getMin();
}
