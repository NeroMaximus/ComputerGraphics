package ru.nsu.ccfit.damdinov;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Arlis on 23.02.14.
 * Класс главного окна приложения
 */
public class MainWindow extends JFrame{
    private final static int menuBarHeight = 16;
    private final MainWindow me;
    private DrawingPanel drawingPanel = null;
    private InfoPanel infoPanel = null;

    //TODO: Properties properties;

    MainWindow(String name){
        super(name);
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, set the GUI to another look and feel.
        }

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));

        infoPanel = new InfoPanel(drawingPanel);
        infoPanel.setPreferredSize(new Dimension(200, 100));

        drawingPanel = new DrawingPanel();

        add(getMenuPanel(), BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Object[] options = {"No", "Yes"};
                int answer = JOptionPane.showOptionDialog(e.getWindow(), "Do you want to close the window?",
                        "Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (answer == 1){
                    e.getWindow().setVisible(false);
                    System.exit(0);
                }
            }
        });
        me = this;
        pack();
        setVisible(true);

    }

    JPanel getMenuPanel(){
        JPanel panel = new JPanel();
        panel.setSize( 10, menuBarHeight * 2 );
        panel.setLayout(new GridLayout( 2, 0));

        JMenuBar menuBar = new JMenuBar();
        JToolBar iconsBar = new JToolBar();
        iconsBar.setFloatable(false);

        ActionListener aboutListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog( me, "This program is laboratory work from NSU. For offers: damdinovr@gmail.com. All rights aren't reserved. 2014.");
            }
        };
        ActionListener settingsListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        Settings settingsWindow = new Settings(drawingPanel);
                        settingsWindow.setLocationRelativeTo(me);
                    }
                });


            }
        };
        ActionListener closingListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        me.dispatchEvent(new WindowEvent(me, WindowEvent.WINDOW_CLOSING));
                    }
                });


            }
        };




        JMenu menuHelp = new JMenu("Help");
        JMenu menuFile = new JMenu("File");
        JMenuItem menuSettings = new JMenuItem("Settings");
        JMenuItem menuItemExit = new JMenuItem("Exit");
        JMenuItem menuItemSave = new JMenuItem("Save");
        JMenuItem menuItemAbout = new JMenuItem("About");

        menuItemExit.addActionListener(closingListener);
        menuSettings.addActionListener(settingsListener);
        menuItemAbout.addActionListener(aboutListener);

        menuFile.setPreferredSize( new Dimension(50, menuBarHeight));
        menuFile.add(menuItemSave);
        menuFile.addSeparator();
        menuFile.add(menuItemExit);
        menuFile.addSeparator();
        menuFile.add(menuSettings);
        menuHelp.setPreferredSize( new Dimension(50,menuBarHeight));
        menuHelp.add(menuItemAbout);

        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.X_AXIS));
        menuBar.add(menuFile);
        menuBar.add(menuHelp);



        JButton menuItemIconQuest = new JButton(createImageIcon("/ru/nsu/ccfit/damdinov/icons/quest.png"));
        JButton menuItemIconCross = new JButton(createImageIcon("/ru/nsu/ccfit/damdinov/icons/cross.png"));
        JButton menuItemIconSettings = new JButton(createImageIcon("/ru/nsu/ccfit/damdinov/icons/settings.png"));
        JButton menuItemIconZoomIn = new JButton(createImageIcon("/ru/nsu/ccfit/damdinov/icons/zoomIn.png"));
        JButton menuItemIconZoomOut = new JButton(createImageIcon("/ru/nsu/ccfit/damdinov/icons/zoomOut.png"));
        JButton menuItemIconUp = new JButton(createImageIcon("/ru/nsu/ccfit/damdinov/icons/up.png"));
        JButton menuItemIconDown = new JButton(createImageIcon("/ru/nsu/ccfit/damdinov/icons/down.png"));
        JButton menuItemIconRight = new JButton(createImageIcon("/ru/nsu/ccfit/damdinov/icons/right.png"));
        JButton menuItemIconLeft = new JButton(createImageIcon("/ru/nsu/ccfit/damdinov/icons/left.png"));

        menuItemIconCross.addActionListener(closingListener);
        menuItemIconQuest.addActionListener(aboutListener);
        menuItemIconSettings.addActionListener(settingsListener);
        menuItemIconUp.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.moveUp();
            }
        });
        menuItemIconDown.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.moveDown();
            }
        });
        menuItemIconLeft.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.moveLeft();
            }
        });
        menuItemIconRight.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.moveRight();
            }
        });
        menuItemIconZoomIn.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.zoomIn();
            }
        });
        menuItemIconZoomOut.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.zoomOut();
            }
        });

        iconsBar.add(menuItemIconQuest);
        iconsBar.add(menuItemIconCross);
        iconsBar.add(menuItemIconSettings);
        iconsBar.add(menuItemIconZoomIn);
        iconsBar.add(menuItemIconZoomOut);
        iconsBar.add(menuItemIconLeft);
        iconsBar.add(menuItemIconUp);
        iconsBar.add(menuItemIconDown);
        iconsBar.add(menuItemIconRight);

        menuBar.add(new Button("Перерисуем кнопку!") {
            public void paint(Graphics g) {
                g.setColor(Color.WHITE);
                g.fillRect(2, 2, getWidth() - 5, getHeight() - 5);
            }
        });

        panel.add(menuBar);
        panel.add(iconsBar);
        return panel;
    }



    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        ImageIcon icon = new ImageIcon(imgURL);
        if (imgURL != null) {
            if(icon.getIconHeight() > menuBarHeight){
                System.err.print("ERROR! Icon'settings height is bigger than menu'settings height!");
                icon = new ImageIcon(icon.getImage().getScaledInstance(menuBarHeight, -1, Image.SCALE_DEFAULT));
            }
            return icon;
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private static void createAndShowGUI() {
        MainWindow mainWindow = new MainWindow("Damdinov");
    }

    public static void main(String[] args){

        if (SwingUtilities.isEventDispatchThread()){
            createAndShowGUI();
        }
        else
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
