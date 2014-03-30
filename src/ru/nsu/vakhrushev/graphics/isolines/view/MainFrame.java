package ru.nsu.vakhrushev.graphics.isolines.view;

import ru.nsu.vakhrushev.graphics.isolines.controller.ApplicationController;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * @author Maxim Vakhrushev
 */
public class MainFrame extends JFrame{

    private JLabel infoBar;
    private JPanel imagePanel;
    private DecimalFormat doubleFormat;

    public MainFrame(ApplicationController applicationController) {
        imagePanel = new ImagePanel(applicationController, this);
        JPanel interfacePanel = new InterfacePanel(applicationController, this);
        infoBar = new JLabel("Info");

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(infoBar);
        add(imagePanel);
        add(interfacePanel);

        setPreferredSize(new Dimension(600, 600));
        setMaximumSize(new Dimension(1000, 600));
        setMinimumSize(new Dimension(500, 500));
        setSize(new Dimension(600, 600));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        doubleFormat = new DecimalFormat();
        doubleFormat.setMaximumFractionDigits(3);
        doubleFormat.setMaximumIntegerDigits(10);
    }

    public void updateInfoBar(int x, int y, double value) {
        if (x < imagePanel.getWidth() * 8 / 10) {
            infoBar.setText("x =  " + x + ". " +
                    "y = " + y + ". " +
                    "f(x, y) =  " + doubleFormat.format(value) + ".");
        } else {
            infoBar.setText("No info");
        }
    }

    public DecimalFormat getDoubleFormat() {
        return doubleFormat;
    }
}
