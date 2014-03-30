package ru.nsu.vakhrushev.graphics.isolines.controller;

import ru.nsu.vakhrushev.graphics.isolines.model.Model;
import ru.nsu.vakhrushev.graphics.isolines.model.PaintType;
import ru.nsu.vakhrushev.graphics.isolines.model.function.Function;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * @author Maxim Vakhrushev
 */
public class PaintController {

    private Model model;
    private double [] criticalValues;
    private double [] isolineValues;
    private Interpolator interpolator;
    private Color [] levelsColors;
    private double mouseIsolineValue = -1;

    public PaintController(Model model) {
        this.model = model;
        interpolator = new Interpolator();
        levelsColors = model.getLevelsColors();
    }

    public void paintColorMap(PaintType paintType) {
        model.setMapImage(paintFunction(model.getMapFunction(), model.getMapImage(), paintType));
        if (model.isGridEnable()) {
            paintGrid();
        }
        if (model.isIsolinesEnable()) {
            for (int k = 1; k < criticalValues.length - 1; ++k) {
                paintIsoline(criticalValues[k]);
            }
        }
        if (mouseIsolineValue != -1) {
            paintIsoline(mouseIsolineValue);
        }

        isolineValues = new double[criticalValues.length - 2];
        System.arraycopy(criticalValues, 1, isolineValues, 0, isolineValues.length);

        if (paintType != PaintType.COLOR_MAP_WITH_INTERPOLATION_AND_DITHER) {
            model.setLegendImage(paintFunction(model.getLegendFunction(), model.getLegendImage(), paintType));
        } else {
            model.setLegendImage(paintFunction(model.getLegendFunction(), model.getLegendImage(), PaintType.COLOR_MAP));
        }

    }

    private void paintGrid() {
        BufferedImage image = model.getMapImage();
        int gridColor = 0;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        // -1 because gridWidth = number of horizontal grid points. We need number of cells
        int gridWidth = model.getGridWidth() - 1;
        // -//-
        int gridHeight = model.getGridHeight() - 1;
        double offsetX = imageWidth / (gridWidth);
        double offsetY = imageHeight / (gridHeight);

        for (int y = 0; y < imageHeight; y += offsetY) {
            if (y > imageHeight - offsetY) {
                paintLine(image, gridColor, 0, imageHeight - 1, imageWidth - 1, imageHeight - 1);
            } else {
                paintLine(image, gridColor, 0, y, imageWidth - 1, y);
            }
        }
        for (int x = 0; x < imageWidth; x += offsetX) {
            if (x > imageWidth - offsetX) {
                paintLine(image, gridColor, imageWidth - 1, 0, imageWidth - 1, imageHeight - 1);
            } else {
                paintLine(image, gridColor, x, 0, x, imageHeight - 1);
            }
        }
    }

    private BufferedImage paintFunction(Function function, BufferedImage image, PaintType paintType) {
        int width = image.getWidth();
        int height = image.getHeight();
        double factorX = (function.getB() - function.getA()) / (width);
        double factorY = (function.getD() - function.getC()) / (height);
        double [] values = new double[width * height];
        double maxValue = function.getMax();
        double minValue = function.getMin();
        double value = 0;
        int [] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                value = function.findValue(x * factorX, y * factorY);
                values[y * width + x] = value;
            }
        }

        findCriticalValues(minValue, maxValue);
        return setColors(image, values, paintType);
    }

    private void findCriticalValues(double minValue, double maxValue) {
        int levelsNumber = model.getLevelsNumber();
        // because critical numbers: z1...zn, levels: 0...n
        criticalValues = new double[levelsNumber + 1];

        double valueOffset = (maxValue - minValue) / levelsNumber;
        for (int i = 0; i < levelsNumber + 1; ++i) {
            criticalValues[i] = minValue + i * valueOffset;
        }
    }

    private BufferedImage setColors(BufferedImage image, double [] values, PaintType paintType) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                for (int k = 1; k < criticalValues.length; ++k) {
                    if (values[y * width + x] < criticalValues[k]) {
                        image.setRGB(x, y, model.getColorByLevel(k - 1).getRGB());
                        break;
                    }
                }
                if (values[y * width + x] > criticalValues[criticalValues.length - 1]) {
                    image.setRGB(x, y, model.getColorByLevel(criticalValues.length - 2).getRGB());
                }
            }
        }

        if (paintType != PaintType.COLOR_MAP) {
            for (int y = 1; y < height - 1; ++y) {
                for (int x = 1; x < width - 1; ++x) {
                    image.setRGB(x, y, interpolator.getBilinearInterpolatedPixel(values[y * width + x], criticalValues, levelsColors));
                }
            }
        }

        if (paintType == PaintType.COLOR_MAP_WITH_INTERPOLATION_AND_DITHER) {
            interpolator.useDithering(image, levelsColors);
        }
        return image;
    }

    private void paintIsoline(double levelValue) {
        BufferedImage image = model.getMapImage();
        Function function = model.getMapFunction();
        int isolineColor = model.getIsolineColor().getRGB();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        // -1 because gridWidth = number of horizontal grid points. We need number of cells
        int gridWidth = model.getGridWidth() - 1;
        // -//-
        int gridHeight = model.getGridHeight() - 1;
        double offsetX = imageWidth / (gridWidth);
        double offsetY = imageHeight / (gridHeight);
        double factorX = (function.getB() - function.getA()) / (imageWidth);
        double factorY = (function.getD() - function.getC()) / (imageHeight);

        double valueNW = 0;
        double valueNE = 0;
        double valueSW = 0;
        double valueSE = 0;
        // 0 - up, 1 - down, 2 - left, 3 - right
        boolean [] isCrossedBorder = new boolean[4];
        int [] crossingPoints = new int[4];
        int count = 0;

        for (int y = 0; y < imageHeight; y += offsetY) {
            if (imageHeight - y < offsetY) {
                break;
            }

            for (int x = 0; x < imageWidth; x += offsetX) {
                if (imageWidth - x < offsetX) {
                    break;
                }
                Arrays.fill(isCrossedBorder, true);
                Arrays.fill(crossingPoints, -1);
                valueNW = function.findValue(x * factorX, y * factorY);
                valueNE = function.findValue((x + offsetX) * factorX, y * factorY);
                valueSW = function.findValue(x * factorX, (y + offsetY) * factorY);
                valueSE = function.findValue((x + offsetX) * factorX, (y + offsetY) * factorY);

                if ((levelValue < valueNW && levelValue < valueNE)|| (levelValue > valueNW && levelValue > valueNE)) {
                    isCrossedBorder[0] = false;
                } else {
                    double factor1 = (valueNE > valueNW) ? (levelValue - valueNW) : (levelValue - valueNE);
                    double factor2 = (valueNE > valueNW) ? (valueNE - valueNW) : (valueNW - valueNE);
                    crossingPoints[0] = (int)Math.round(x + offsetX * factor1 / factor2);
                }
                if ((levelValue < valueSW && levelValue < valueSE)|| (levelValue > valueSW && levelValue > valueSE)) {
                    isCrossedBorder[1] = false;
                } else {
                    double factor1 = (valueSE > valueSW) ? (levelValue - valueSW) : (levelValue - valueSE);
                    double factor2 = (valueSE > valueSW) ? (valueSE - valueSW) : (valueSW - valueSE);
                    crossingPoints[1] = (int)Math.round(x + offsetX * factor1 / factor2);
                }


                if ((levelValue < valueNW && levelValue < valueSW)|| (levelValue > valueNW && levelValue > valueSW)) {
                    isCrossedBorder[2] = false;
                } else {
                    double factor1 = (valueSW > valueNW) ? (levelValue - valueNW) : (levelValue - valueSW);
                    double factor2 = (valueSW > valueNW) ? (valueSW - valueNW) : (valueNW - valueSW);
                    crossingPoints[2] = (int)Math.round(y + offsetY * factor1 / factor2);
                }
                if ((levelValue < valueNE && levelValue < valueSE)|| (levelValue > valueNE && levelValue > valueSE)) {
                    isCrossedBorder[3] = false;
                } else {
                    double factor1 = (valueSE > valueNE) ? (levelValue - valueNE) : (levelValue - valueSE);
                    double factor2 = (valueSE > valueNE) ? (valueSE - valueNE) : (valueNE - valueSE);
                    crossingPoints[3] = (int)Math.round(y + offsetY * factor1 / factor2);
                }

                count = countCrossedBorders(isCrossedBorder);

                switch (count) {
                    case 0:
                        break;
                    case 2:
                        int [] pointsX = new int[2];
                        int [] pointsY = new int[2];
                        int index = 0;
                        if (isCrossedBorder[0]) {
                            pointsX[index] = crossingPoints[0];
                            pointsY[index] = y;
                            ++index;
                        }
                        if (isCrossedBorder[1]) {
                            pointsX[index] = crossingPoints[1];
                            pointsY[index] = (int)Math.round(y + offsetY);
                            ++index;
                        }
                        if (isCrossedBorder[2]) {
                            pointsX[index] = x;
                            pointsY[index] = crossingPoints[2];
                            ++index;
                        }
                        if (isCrossedBorder[3]) {
                            pointsX[index] = (int)Math.round(x + offsetX);
                            pointsY[index] = crossingPoints[3];
                            ++index;
                        }

                        for (int i = 0; i < pointsX.length; ++i) {
                            if (pointsX[i] == imageWidth) {
                                --pointsX[i];
                            }
                            if (pointsY[i] == imageHeight) {
                                --pointsY[i];
                            }
                        }
                        paintLine(image, isolineColor, pointsX[0], pointsY[0], pointsX[1], pointsY[1]);
                        break;
                    case 4:
                        boolean isCenterBigger = (function.findValue(x + offsetX / 2, y + offsetY / 2) > levelValue);
                        if (isCenterBigger) {
                            paintLine(image, isolineColor, crossingPoints[0], y,
                                    (int)Math.round(x + offsetX), crossingPoints[3]);
                            paintLine(image, isolineColor, crossingPoints[1], (int)Math.round(y + offsetY),
                                    x, crossingPoints[2]);
                        } else {
                            paintLine(image, isolineColor, crossingPoints[0], y, x, crossingPoints[2]);
                            paintLine(image, isolineColor, crossingPoints[1], (int)Math.round(y + offsetY),
                                    (int)Math.round(x + offsetX), crossingPoints[3]);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private int countCrossedBorders(boolean [] isCrossedBorder) {
        int count = 0;
        for (boolean value : isCrossedBorder) {
            if (value) {
                ++count;
            }
        }
        return count;
    }

    private long paintLine(BufferedImage image, int color, int startX, int startY, int endX, int endY) {
        int fixingDeltaX = 0;
        int fixingDeltaY = 0;
        int fixingDeltaError = 0;

        int deltaError = 0;
        int deltaX = endX - startX;
        int deltaY = endY - startY;
        int signX = (int)Math.signum(deltaX);
        int signY = (int)Math.signum(deltaY);
        deltaX = Math.abs(deltaX);
        deltaY = Math.abs(deltaY);

        if (deltaX > deltaY) {
            fixingDeltaX = signX;
            fixingDeltaY = 0;
            fixingDeltaError = deltaY;
            deltaError = deltaX;
        } else {
            fixingDeltaX = 0;
            fixingDeltaY = signY;
            fixingDeltaError = deltaX;
            deltaError = deltaY;
        }

        int currX = startX;
        int currY = startY;
        int error = deltaError/2;
        long pixelsDrew = 1;

        image.setRGB(currX, currY, color);

        for (int i = 1; i < deltaError; ++i) {
            error -= fixingDeltaError;
            if (error < 0) {
                error += deltaError;
                currX += signX;
                currY += signY;
            } else {
                currX += fixingDeltaX;
                currY += fixingDeltaY;
            }
            image.setRGB(currX, currY, color);
            ++pixelsDrew;
        }
        return pixelsDrew;
    }

    public void setMouseIsolineValue(double mouseIsolineValue) {
        this.mouseIsolineValue = mouseIsolineValue;
    }

    public double[] getIsolineValues() {
        return isolineValues;
    }
}
