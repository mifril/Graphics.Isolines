package ru.nsu.vakhrushev.graphics.isolines.view;

import ru.nsu.vakhrushev.graphics.isolines.controller.ApplicationController;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

/**
 * @author Maxim Vakhrushev
 */
public class ImagePanel extends JPanel {
    private ApplicationController applicationController;
    private MainFrame mainFrame;

    public ImagePanel(final ApplicationController applicationController, final MainFrame mainFrame) {
        this.applicationController = applicationController;
        this.mainFrame = mainFrame;
        setLayout(null);
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBackground(Color.WHITE);
        addMouseMotionListener(new MouseInputAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                mainFrame.updateInfoBar(e.getX(), e.getY(), applicationController.getFunctionValue(e.getX(), e.getY()));
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getX() < getWidth() * 8 / 10) {
                    applicationController.setMouseIsoline(e.getX(), e.getY());
                }
                mainFrame.repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.removeAll();
        applicationController.paint(this.getWidth(), this.getHeight());
        BufferedImage mapImage = applicationController.getMapImage();
        BufferedImage legendImage = applicationController.getLegendImage();
        int legendOffsetY =  0;
        int legendOffsetX =  this.getWidth() * 9 / 10;
        g.drawImage(mapImage, 0 , 0, null);
        g.drawImage(legendImage, legendOffsetX, legendOffsetY, null);
        putLegendTitles();
    }


    private void putLegendTitles() {
        double [] isolineValues = applicationController.getIsolineValues();
        int width = this.getWidth();
        int height = this.getHeight();
        JLabel [] titles = new JLabel[isolineValues.length];
        DecimalFormat doubleFormat = mainFrame.getDoubleFormat();

        for (int i = isolineValues.length - 1; i >= 0; i--) {
            titles[i] = new JLabel(doubleFormat.format(isolineValues[i]));
            this.add(titles[i]);
            titles[i].setBounds(width * 8 / 10 + width / 30,
                    (isolineValues.length - i) * (height / (isolineValues.length + 1)) - height  / 30,
                    width / 10, height / 10);
        }
    }
}
