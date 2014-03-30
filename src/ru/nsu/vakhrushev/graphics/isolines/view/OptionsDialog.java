package ru.nsu.vakhrushev.graphics.isolines.view;

import ru.nsu.vakhrushev.graphics.isolines.controller.ApplicationController;
import ru.nsu.vakhrushev.graphics.isolines.model.Model;
import ru.nsu.vakhrushev.graphics.isolines.model.function.Function;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Maxim Vakhrushev
 */
public class OptionsDialog extends JDialog {
    public OptionsDialog(final JFrame mainFrame, final ApplicationController controller) {
        super(mainFrame, "Change options");

        final Model model = controller.getModel();
        Function mapFunction = model.getMapFunction();
        setLayout(new GridLayout(7, 2));
        final JTextField fieldK = new JTextField(Integer.toString(model.getGridWidth()));
        final JTextField fieldM = new JTextField(Integer.toString(model.getGridHeight()));
        final JTextField fieldA = new JTextField(Double.toString(mapFunction.getA()));
        final JTextField fieldB = new JTextField(Double.toString(mapFunction.getB()));
        final JTextField fieldC = new JTextField(Double.toString(mapFunction.getC()));
        final JTextField fieldD = new JTextField(Double.toString(mapFunction.getD()));

        JLabel labelK = new JLabel("K: ");
        JLabel labelM = new JLabel("M: ");
        JLabel labelA = new JLabel("A: ");
        JLabel labelB = new JLabel("B: ");
        JLabel labelC = new JLabel("C: ");
        JLabel labelD = new JLabel("D: ");

        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setGridWidth(Integer.parseInt(fieldK.getText()));
                model.setGridHeight(Integer.parseInt(fieldM.getText()));
                model.setMapFunction(Double.parseDouble(fieldA.getText()), Double.parseDouble(fieldB.getText()),
                        Double.parseDouble(fieldC.getText()), Double.parseDouble(fieldD.getText()));
                setVisible(false);
                controller.unsetMouseIsoline();
                mainFrame.repaint();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                mainFrame.repaint();
            }
        });

        add(labelK);
        add(fieldK);
        add(labelM);
        add(fieldM);
        add(labelA);
        add(fieldA);
        add(labelB);
        add(fieldB);
        add(labelC);
        add(fieldC);
        add(labelD);
        add(fieldD);
        add(cancelButton);
        add(confirmButton);

        setSize(200, 300);
        setLocationRelativeTo(mainFrame);
    }
}
