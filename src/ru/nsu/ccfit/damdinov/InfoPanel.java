package ru.nsu.ccfit.damdinov;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Arlis on 02.03.14.
 */
public class InfoPanel extends JPanel {
    JLabel mainText = null;

    public InfoPanel(final DrawingPanel drawingPanel){
        super();
        GridLayout gridLayout = new GridLayout(10,2);

        setLayout(gridLayout);

        add( new JButton("show parametric function"));
        add( new JButton("show implicit function"));
    }

}
