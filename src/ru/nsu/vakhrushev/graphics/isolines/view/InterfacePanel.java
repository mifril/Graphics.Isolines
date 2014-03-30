package ru.nsu.vakhrushev.graphics.isolines.view;

import ru.nsu.vakhrushev.graphics.isolines.controller.ApplicationController;
import ru.nsu.vakhrushev.graphics.isolines.model.PaintType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Maxim Vakhrushev
 */
public class InterfacePanel extends JPanel {
    public InterfacePanel (final ApplicationController applicationController, final MainFrame mainFrame) {
        JCheckBox isolineCheckBox = new JCheckBox("Isolines");
        JCheckBox gridCheckBox = new JCheckBox("Grid");
        JButton colorMapButton = new JButton("Color map");
        JButton ditherButton = new JButton("Interpolation + dither");
        JButton interpolationButton = new JButton("Interpolation");
        JButton optionsButton = new JButton("Options");


        isolineCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applicationController.resetIsolinesEnableFlag();
                mainFrame.repaint();
            }
        });
        gridCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applicationController.resetGridEnableFlag();
                mainFrame.repaint();
            }
        });

        colorMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applicationController.setPaintType(PaintType.COLOR_MAP);
                mainFrame.repaint();
            }
        });
        interpolationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applicationController.setPaintType(PaintType.COLOR_MAP_WITH_INTERPOLATION);
                mainFrame.repaint();
            }
        });
        ditherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applicationController.setPaintType(PaintType.COLOR_MAP_WITH_INTERPOLATION_AND_DITHER);
                mainFrame.repaint();
            }
        });
        optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OptionsDialog dialog = new OptionsDialog(mainFrame, applicationController);
                dialog.setVisible(true);
                mainFrame.repaint();
            }
        });

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(colorMapButton)
                                .addComponent(interpolationButton)
                                .addComponent(ditherButton)
                                )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(isolineCheckBox)
                                .addComponent(gridCheckBox)
                                .addComponent(optionsButton))
        );
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(colorMapButton)
                                .addComponent(isolineCheckBox))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(interpolationButton)
                                .addComponent(gridCheckBox))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(ditherButton)
                                .addComponent(optionsButton)
                        ));    
    }
}
