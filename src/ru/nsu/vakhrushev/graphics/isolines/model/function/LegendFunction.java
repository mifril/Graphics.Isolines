package ru.nsu.vakhrushev.graphics.isolines.model.function;

/**
 * @author Maxim Vakhrushev
 */
public class LegendFunction implements Function {
    private double a;
    private double b;
    private double c;
    private double d;
    private double max;
    private double min;

    public LegendFunction() {
        a = 0;
        b = 0;
        c = 0;
        d = 10;
        findExtremums();
    }

    public double findValue(double x, double y) {
        return -1*y;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getD() {
        return d;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    private void findExtremums() {
        max = 0;
        min = -d;
    }
}
