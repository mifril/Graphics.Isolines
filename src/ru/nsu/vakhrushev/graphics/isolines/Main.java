package ru.nsu.vakhrushev.graphics.isolines;

import ru.nsu.vakhrushev.graphics.isolines.controller.ApplicationController;
import ru.nsu.vakhrushev.graphics.isolines.model.Model;
import ru.nsu.vakhrushev.graphics.isolines.view.MainFrame;

import javax.swing.*;

/**
 * @author Maxim Vakhrushev
 */
public class Main {
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Model model = new Model("resources/config.txt");
                    ApplicationController applicationController = new ApplicationController(model);
                    MainFrame mainFrame = new MainFrame(applicationController);
                    mainFrame.setVisible(true);
                } catch (Exception ex) {
                    System.err.println(ex.getLocalizedMessage());
                    ex.printStackTrace();
                }
            }
        });
    }
}
