import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Arlis on 01.03.14.
 */
public class DrawingPanel extends JPanel{

    DrawingPanel(){
        super();
        setBackground(Color.WHITE);
    }

    private int Convert_Y( int y) {
        return -y;
    }
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), 1);
        drawRound(bufferedImage, 200, 200, 100);
        graphics2D.drawImage(bufferedImage, null, 0,0);
    }

    private void drawRound(BufferedImage bufferedImage, int center_x, int center_y, int r) {
        int x, y;
        for( x = -r; x <= r; x++){
            y = (int) Math.sqrt( r * r - x * x);
            bufferedImage.setRGB( x + center_x, Convert_Y(y) + center_y, 255);
            if( y != 0) {
                bufferedImage.setRGB( x + center_x, Convert_Y( -y) + center_y, 255);
            }
        }
    }
}

