package ru.nsu.vakhrushev.graphics.isolines.model.function;

import java.util.Arrays;

/**
 * @author Maxim Vakhrushev
 */
public class MapFunction implements Function {

    private double a;
    private double b;
    private double c;
    private double d;
    private double max;
    private double min;
    private int gridWidth;
    private int gridHeight;

    public MapFunction(double a, double b, double c, double d,int gridWidth, int gridHeight) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        findExtremums();
    }

    public double findValue(double x, double y) {
        return (Math.sin(x) + Math.cos(y));
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
        double offsetX = (b - a) / (gridWidth - 1);
        double offsetY = (d - c) / (gridHeight - 1);
        double [] values = new double[gridWidth * gridHeight];

        for (int y = 0; y < gridHeight; ++y) {
            for (int x = 0; x < gridWidth; ++x) {
                values[y * gridWidth + x] = findValue(0. + x * offsetX, 0. + y * offsetY);
            }
        }
        Arrays.sort(values);
        min = values[0];
        max = values[values.length - 1];
    }
}
