package icons;

import javax.swing.*;

/**
 * Created by Arlis on 02.03.14.
 */
public class InfoPanel extends JPanel {
    JLabel mainText = null;

    public InfoPanel(){
        super();
        mainText = new JLabel("Info:");
        add(mainText);
    }

}
