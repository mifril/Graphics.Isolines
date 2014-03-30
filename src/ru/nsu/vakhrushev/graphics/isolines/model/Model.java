package ru.nsu.vakhrushev.graphics.isolines.model;

import ru.nsu.vakhrushev.graphics.isolines.model.function.Function;
import ru.nsu.vakhrushev.graphics.isolines.model.function.LegendFunction;
import ru.nsu.vakhrushev.graphics.isolines.model.function.MapFunction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Maxim Vakhrushev
 */
public class Model {
    private int gridWidth;
    private int gridHeight;
    private int levelsNumber;
    private Color[] levelsColors;
    private Color isolineColor;
    private BufferedImage mapImage;
    private BufferedImage legendImage;
    private Function mapFunction;
    private Function legendFunction;

    private boolean gridEnable = false;
    private boolean isolinesEnable = false;

    public Model(String fileName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(fileName));
        this.gridWidth = scanner.nextInt();
        this.gridHeight = scanner.nextInt();
        //because levels started with 0
        this.levelsNumber = scanner.nextInt() + 1;
        //colors: c0 ... cn
        this.levelsColors = new Color[this.levelsNumber];
        for (int i = 0; i < this.levelsNumber; ++i) {
            this.levelsColors[i] = new Color(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
        }
        this.isolineColor = new Color(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
        mapFunction = new MapFunction(0, 10, 0, 10, gridWidth, gridHeight);
        legendFunction = new LegendFunction();
    }

    public void setNewMapImageSize(int width, int height) {
        mapImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void setNewLegendImageSize(int width, int height) {
        legendImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }


    public void setMapImage(BufferedImage mapImage) {
        this.mapImage = mapImage;
    }

    public void setLegendImage(BufferedImage legendImage) {
        this.legendImage = legendImage;
    }

    public void resetGridEnable() {
        this.gridEnable = !this.gridEnable;
    }

    public void resetIsolinesEnable() {
        this.isolinesEnable = !this.isolinesEnable;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public int getLevelsNumber() {
        return levelsNumber;
    }

    public Color[] getLevelsColors() {
        return levelsColors;
    }

    public Color getColorByLevel(int level) {
        return levelsColors[level];
    }

    public Color getIsolineColor() {
        return isolineColor;
    }

    public boolean isGridEnable() {
        return gridEnable;
    }

    public boolean isIsolinesEnable() {
        return isolinesEnable;
    }

    public BufferedImage getMapImage() {
        return mapImage;
    }

    public BufferedImage getLegendImage() {
        return legendImage;
    }

    public Function getMapFunction() {
        return mapFunction;
    }

    public Function getLegendFunction() {
        return legendFunction;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
    }

    public void setMapFunction(double a, double b, double c, double d) {
        this.mapFunction = new MapFunction(a, b, c, d, gridWidth, gridHeight);
    }
}
