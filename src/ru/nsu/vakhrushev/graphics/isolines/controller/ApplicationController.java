package ru.nsu.vakhrushev.graphics.isolines.controller;

import ru.nsu.vakhrushev.graphics.isolines.model.Model;
import ru.nsu.vakhrushev.graphics.isolines.model.PaintType;
import ru.nsu.vakhrushev.graphics.isolines.model.function.Function;

import java.awt.image.BufferedImage;

/**
 * @author Maxim Vakhrushev
 */
public class ApplicationController {

    private Model model;
    private PaintType paintType = PaintType.COLOR_MAP;
    private PaintController paintController;

    public ApplicationController(Model model) {
        this.model = model;
        paintController = new PaintController(this.model);
    }

    public void resetGridEnableFlag() {
        model.resetGridEnable();
    }

    public void resetIsolinesEnableFlag() {
        model.resetIsolinesEnable();
    }

    public void paint(int width, int height) {
        model.setNewMapImageSize(width * 8 / 10, height);
        model.setNewLegendImageSize(width / 10, height);
        paintController.paintColorMap(paintType);
    }

    public double getFunctionValue(int x, int y) {
        BufferedImage image = model.getMapImage();
        Function function = model.getMapFunction();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        double factorX = (function.getB() - function.getA()) / (imageWidth);
        double factorY = (function.getD() - function.getC()) / (imageHeight);

        return function.findValue(x * factorX, y * factorY);
    }

    public void setMouseIsoline(int x, int y) {
        paintController.setMouseIsolineValue(getFunctionValue(x, y));
    }

    public void unsetMouseIsoline() {
        paintController.setMouseIsolineValue(-1);
    }


    public void setPaintType(PaintType paintType) {
        this.paintType = paintType;
    }

    public double [] getIsolineValues() {
        return paintController.getIsolineValues();
    }

    public BufferedImage getMapImage() {
        return model.getMapImage();
    }

    public BufferedImage getLegendImage() {
        return model.getLegendImage();
    }

    public Model getModel() {
        return model;
    }



    public void setModel(Model model) {
        this.model = model;
    }

}
