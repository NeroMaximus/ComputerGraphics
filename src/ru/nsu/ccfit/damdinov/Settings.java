package ru.nsu.ccfit.damdinov;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by nero on 4/9/14.
 */
public class Settings extends JFrame implements ActionListener, ItemListener {
    private JPanel testPane;
    private JCheckBox includeScrollWheelCheckBox;
    private JCheckBox dragNDropCheckBox;
    private JSpinner sclareStep;
    private JSpinner motionStep;
    private JButton acceptButton;
    private JButton cancelButton;
    private DrawingPanel drawingPanel;

    Settings(DrawingPanel drawingPanel){
        super("Settings");
        this.drawingPanel = drawingPanel;
        String[] stepValues = new String[]{"1","2","3","4","5","6","7","8","9","10"};
        SpinnerListModel model = new SpinnerListModel(stepValues);
        SpinnerListModel model2 = new SpinnerListModel(stepValues);

        sclareStep.setModel(model);
        motionStep.setModel(model2);

        sclareStep.setValue(drawingPanel.getSclareStep().toString());
        motionStep.setValue(drawingPanel.getMotionStep().toString());

        setMinimumSize(new Dimension(300, 300));
        setContentPane(testPane);

        cancelButton.addActionListener( this);
        acceptButton.addActionListener(this);
        dragNDropCheckBox.addItemListener(this);
        if (drawingPanel.dnd)
        dragNDropCheckBox.setSelected(true);
        includeScrollWheelCheckBox.addItemListener(this);
        includeScrollWheelCheckBox.setSelected(true);

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Accept")){
            drawingPanel.setSclareStep(Integer.parseInt((String) sclareStep.getValue()));
            drawingPanel.setMotionStep(Integer.parseInt((String) motionStep.getValue()));
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
        if (e.getActionCommand().equals("Cancel")){
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();

        if (source == dragNDropCheckBox) {
            drawingPanel.changeDND();
        } else if (source == includeScrollWheelCheckBox) {
            //TODO
        }
    }
}
