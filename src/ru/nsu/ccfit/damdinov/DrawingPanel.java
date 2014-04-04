package ru.nsu.ccfit.damdinov;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

/**
 * Created by Arlis on 01.03.14.
 */
public class DrawingPanel extends JPanel implements MouseMotionListener, MouseWheelListener{
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    BufferedImage bufferedImage;


    int oldWidth = 0;
    int oldHeight = 0;
    DrawingPanel(){
        super();
        bufferedImage = new BufferedImage((int) d.getWidth(), (int) d.getHeight(), 1);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setPaint( new Color(255, 255, 255));

        graphics2D.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
    }

    private int Convert_Y( int y) {
        return -y;
    }
    public void paintComponent(Graphics graphics){
        BufferedImage newBufferedImage = null;
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        graphics2D.drawImage(bufferedImage, null, 0,0);
    }

    public void drawRound( int center_x, int center_y, int r) {
        int x = 0;
        int y = r;
        int delta = 1 - 2*r; //difference between the diagonal pixel and radius; (x + 1)*(x + 1) + (y - 1)*(y - 1) - r*r;
        int error = 0;
        bufferedImage.flush();

        while(y >= 0) {
            if ( center_x + x > 0 && center_y + Convert_Y(y) > 0)
                bufferedImage.setRGB( center_x + x, center_y + Convert_Y(y), 255);
            if ( center_x + x > 0 && center_y - Convert_Y(y) > 0)
                bufferedImage.setRGB( center_x + x, center_y - Convert_Y(y), 255);
            if ( center_x - x > 0 && center_y + Convert_Y(y) > 0)
                bufferedImage.setRGB( center_x - x, center_y + Convert_Y(y), 255);
            if ( center_x - x > 0 && center_y - Convert_Y(y) > 0)
                bufferedImage.setRGB( center_x - x, center_y - Convert_Y(y), 255);

            error = 2 * (delta + y) - 1;
            if(delta < 0 && error <= 0) {// 1,2
                ++x;
                delta += 2 * x + 1;
                continue;
            }
            error = 2 * (delta - x) - 1;
            if(delta > 0 && error > 0) {//3,4
                --y;
                delta += 1 - 2 * y;
                continue;
            }
            ++x;
            delta += 2 * (x - y);
            --y;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}

