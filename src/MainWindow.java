import icons.InfoPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

/**
 * Created by Arlis on 23.02.14.
 * Класс главного окна приложения
 */
public class MainWindow extends JFrame{
    private final static int menuBarHeight = 16;
    private DrawingPanel drawingPanel = null;
    private InfoPanel infoPanel = null;

    MainWindow(String s){
        super(s);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Object[] options = {"Yes", "No"};
                int answer = JOptionPane.showOptionDialog(e.getWindow(), "Do you want to close the window?",
                        "Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (answer == 0){
                    e.getWindow().setVisible(false);
                    System.exit(0);
                }
            }
        });
        try {
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (Exception e) { }

        setLayout(new BorderLayout());
        setSize(800, 600);
        setLocationRelativeTo( null);
        setMinimumSize(new Dimension(800, 600));

        add(createMenuPane(), BorderLayout.NORTH);
        infoPanel = new InfoPanel();
        infoPanel.setPreferredSize(new Dimension(300, 100));
        add(infoPanel, BorderLayout.EAST);

        drawingPanel = new DrawingPanel();
        add(drawingPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
        setVisible(true);
    }

    JPanel createMenuPane(){

        JPanel panel = new JPanel();
        panel.setSize(10,menuBarHeight*2);

        JMenuBar menuBar1 = new JMenuBar();
        JMenuBar menuBar2 = new JMenuBar();

        JMenu menuHelp = new JMenu("Help");
        JMenu menuFile = new JMenu("File");
        JMenuItem menuItemExit = new JMenuItem("Exit");
        JMenuItem menuItemSave = new JMenuItem("Save");
        JMenuItem menuItemAbout = new JMenuItem("About");
        JButton menuItemIconQuest = new JButton();
        JButton menuItemIconCross = new JButton(createImageIcon("/icons/cross.png"));
        menuItemIconQuest.setIcon(createImageIcon("/icons/quest.png"));
        menuFile.setPreferredSize( new Dimension(50,menuBarHeight));
        menuFile.add(menuItemSave);
        menuFile.addSeparator();
        menuFile.add(menuItemExit);

        menuItemExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                System.exit(0);
            }
        });
        menuItemAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("About");
                frame.setSize(200, 200);
                frame.setLocationRelativeTo( null);
                frame.add( new JLabel("fffffff"));
                frame.setVisible(true);
            }
        });

        menuHelp.setPreferredSize( new Dimension(50,menuBarHeight));
        menuHelp.add(menuItemAbout);

        panel.setLayout(new BorderLayout());
        menuBar1.setLayout( new BoxLayout(menuBar1, BoxLayout.X_AXIS));
        menuBar1.add(menuFile);
        menuBar1.add(menuHelp);

//        menuBar2.setLayout( new FlowLayout());
        menuBar2.add(menuItemIconQuest);
        menuBar2.add(menuItemIconCross);
//        menuBar2.add(new Button("Перерисуем кнопку!") {
//            public void paint(Graphics g) {
//                g.setColor(Color.BLUE);
//                g.fillRect(2, 2, getWidth() - 5, getHeight() - 5);
//            }
//        });

        panel.add(menuBar1, BorderLayout.NORTH);
        panel.add(menuBar2, BorderLayout.SOUTH);
        return panel;
    }



    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        ImageIcon icon = new ImageIcon(imgURL);
        if (imgURL != null) {
            if(icon.getIconHeight() > menuBarHeight){
                System.err.print("ERROR! Icon's height is bigger than menu's height!");
                icon = new ImageIcon(icon.getImage().getScaledInstance(menuBarHeight, -1, Image.SCALE_DEFAULT));
            }
            return icon;
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }


    public static void main(String[] args){
        MainWindow mainWindow = new MainWindow("Laboratory work №1");
        mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}
