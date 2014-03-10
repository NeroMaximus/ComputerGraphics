package ru.nsu.ccfit.damdinov;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Arlis on 02.03.14.
 */
public class InfoPanel extends JPanel {
    JLabel mainText = null;

    public InfoPanel(final DrawingPanel drawingPanel){
        super();
        JButton drawCircle = new JButton("Draw circle");
        add( drawCircle);
        drawCircle.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer radius = Integer.parseInt(JOptionPane.showInputDialog("Radius", "100"));
                if (radius != null) {
                    drawingPanel.drawRound(250, 250, radius);
                    drawingPanel.repaint();
                }
            }
        });
    }

}
