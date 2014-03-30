package ru.nsu.vakhrushev.graphics.isolines.controller;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Maxim Vakhrushev
 */
public class Interpolator {

    public Interpolator() {
    }

    public int getBilinearInterpolatedPixel(double value, double[] criticalValues, Color[] colors) {
        double colorComponent = 0;
        int index = 0;
        for (int k = 1; k < criticalValues.length; ++k) {
            if (value < criticalValues[k]) {
                index = k;
                break;
            }
        }
        if (value > criticalValues[criticalValues.length - 1]) {
            index = criticalValues.length - 1;
        }

        int colorPrev = (index != 0) ? (colors[index - 1].getRGB()) : (colors[colors.length - 1].getRGB());
        int colorNext = (index != criticalValues.length - 1) ? (colors[index].getRGB()) : (colors[index - 1].getRGB());
        double valuePrev = (index != 0) ? (criticalValues[index - 1]) : (criticalValues[criticalValues.length - 1]);
        double valueNext = criticalValues[index];
        int pixel = 0;

        for (int i = 0; i < 3; ++i) {
            colorComponent = (((colorPrev >> i * 8) & 0xFF) * (valueNext - value) / (valueNext - valuePrev) +
                    ((colorNext >> i * 8) & 0xFF) * (value - valuePrev) / (valueNext - valuePrev));
            pixel |= ((int) colorComponent << i * 8);
        }

        return pixel;
    }

    public void useDithering(BufferedImage image, Color[] colors) {
        int width = image.getWidth();
        int height = image.getHeight();

        int oldPixel = 0;
        int newPixel = 0;
        int quantError = 0;
        int[] colorsRGB = new int[colors.length];

        for (int i = 0; i < colors.length; ++i) {
            colorsRGB[i] = colors[i].getRGB();
        }

        for (int y = 0; y < height - 1; ++y) {
            for (int x = 1; x < width - 1; ++x) {
                oldPixel = image.getRGB(x, y);
                newPixel = findNearestColor(oldPixel, colorsRGB);
                quantError = 0;
                int colorComponent = 0;
                for (int i = 0; i < 3; ++i) {
                    colorComponent = (((oldPixel >> i*8) & 0xFF) - ((newPixel >> i*8) & 0xFF));
                    quantError |= (colorComponent << i*8);
                }

                image.setRGB(x, y, newPixel);
                image.setRGB(x + 1, y + 0, (changeColor(image.getRGB(x + 1, y + 0), quantError, 7)));
                image.setRGB(x - 1, y + 1, (changeColor(image.getRGB(x - 1, y + 1), quantError, 3)));
                image.setRGB(x + 0, y + 1, (changeColor(image.getRGB(x + 0, y + 1), quantError, 5)));
                image.setRGB(x + 1, y + 1, (changeColor(image.getRGB(x + 1, y + 1), quantError, 1)));
            }
        }
    }


    private int changeColor(int color, int quantError, int factor) {
            int colorComponent = 0;
            int newColor = 0;
            for (int i = 0; i < 3; ++i) {
                colorComponent = (int)Math.round(((color >> i*8) & 0xFF) + ((quantError >> i*8) & 0xFF) * (factor / 16.0));
                newColor |= (colorComponent << i*8);
            }
            return newColor;
   }

    public int findNearestColor (int pixel, int [] colorsRGB) {
        int [] diffs = new int[colorsRGB.length];
        int colorComponent = 0;
        for (int i = 0; i < colorsRGB.length; ++i) {
            diffs[i] = 0;
            for (int j = 0; j < 3; ++j) {
                diffs[i] += Math.pow(((colorsRGB[i] >> j*8) & 0xFF) - ((pixel >> j*8) & 0xFF), 2);
            }
        }
        int minDiff = Integer.MAX_VALUE;
        int minDiffIndex = -1;
        for (int i = 0; i < diffs.length; ++i) {
            if (diffs[i] < minDiff) {
                minDiff = diffs[i];
                minDiffIndex = i;
            }
        }
        if (minDiffIndex == -1) {
            throw new IllegalStateException("Illegal index");
        }

        return colorsRGB[minDiffIndex];
    }
}