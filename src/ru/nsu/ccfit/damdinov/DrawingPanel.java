package ru.nsu.ccfit.damdinov;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 * Created by Arlis on 01.03.14.
 */
public class DrawingPanel extends JPanel implements MouseMotionListener, MouseWheelListener, KeyListener {
    Dimension fullscreen = Toolkit.getDefaultToolkit().getScreenSize();
    BufferedImage bufferedImage;

    private int canvasWidth, canvasHeight;
    private int centerAxisX = 250, centerAxisY = 100;
    private int currentX, currentY;
    private int motionStep = 10, sclareStep = 10;
    private boolean drugAndDrop, scrollWheel;
    private double maxX, minX, maxY, minY;


    Graphics2D graphics2D;
    private double length = 20;
    boolean dnd = false;

    DrawingPanel(){
        super();

        addMouseMotionListener(this);
        addMouseWheelListener(this);

        bufferedImage = new BufferedImage((int) fullscreen.getWidth(), (int) fullscreen.getHeight(), 1);
        graphics2D = bufferedImage.createGraphics();
        graphics2D.setPaint( new Color(255, 255, 255));
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        canvasHeight = getHeight();
        canvasWidth = getWidth();

        drawAxis();

        drawFunction();
        graphics2D.drawImage(bufferedImage, null, 0, 0);
    }



    private void drawFunction() {

        double t = - 2 - Math.sqrt(5.0);
        double realMaxX = t*t / ( t*t - 1);
        double realMaxY = ( t*t + 1 ) / ( t +2 );
        double realMinX;
        double realMinY;

        maxX = ( canvasWidth - centerAxisX )/length;
        minX = centerAxisX / length;
        maxY = centerAxisY/ length;
        minY = ( canvasHeight - centerAxisY )/ length;
       // System.out.println(canvasHeight +"; "+ centerAxisY +"; "+minY);

        /*вычисляем интервалы t, по которым необходимо пройтись, чтобы нарисовать кривую*/
        double[] intervalT = getIntervalForT(Double.NEGATIVE_INFINITY, t, realMaxX, 1, realMaxY, Double.NEGATIVE_INFINITY, false, true);
        /*проходимся по этим интервалам с определенным шагом по t и рисуем значения*/
        drawCurve(intervalT);

        t = -2;
        realMinX = realMaxX;
        realMaxX = t*t / ( t*t - 1);

        intervalT = getIntervalForT( (- 2.0 - Math.sqrt(5.0)) , t, realMaxX, realMinX, realMaxY, Double.NEGATIVE_INFINITY, true, false );
        drawCurve(intervalT);


        t = -1;
        realMinX = realMaxX;
        realMinY = (t*t + 1)/( t + 2);
        intervalT = getIntervalForT( -2 , t, Double.POSITIVE_INFINITY, realMinX,  Double.POSITIVE_INFINITY, realMinY, false , false );
        drawCurve(intervalT);

        t = -1;
        realMaxY = (t * t + 1) / (t + 2);
        t = 0;
        realMinY = (t * t + 1) / (t + 2);
        intervalT = getIntervalForT(-1, 0, 0, Double.NEGATIVE_INFINITY, realMaxY, realMinY, false, true);
        drawCurve(intervalT);

        t = 0;
        realMaxX = (t * t) / (t * t - 1);
        realMaxY = (t * t + 1) / (t + 2);
        t = -2 + Math.sqrt(5);
        realMinX = (t * t) / (t * t - 1);
        realMinY = (t * t + 1) / (t + 2);
        intervalT = getIntervalForT(0, -2 + Math.sqrt(5), realMaxX, realMinX, realMaxY, realMinY, true, true);
        drawCurve(intervalT);

        t = -2 + Math.sqrt(5);
        realMaxX = (t * t) / (t * t - 1);
        realMinY = (t * t + 1) / (t + 2);
        t = 1;
        realMaxY = (t * t + 1) / (t + 2);
        intervalT = getIntervalForT(-2 + Math.sqrt(5), 1, realMaxX, Double.NEGATIVE_INFINITY, realMaxY, realMinY, true, false);
        drawCurve(intervalT);


        intervalT = getIntervalForT(1, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 1, Double.POSITIVE_INFINITY, realMinY, false, false);
        drawCurve(intervalT);
    }

    private void drawCurve(double[] intervalT) {

        if ( intervalT == null)
            return;
        double s = ( maxY + minY)/ canvasHeight;

        double m = Math.max( Math.max( Math.abs(derivativeY(intervalT[0])), Math.abs(derivativeY(intervalT[1]))),
                             Math.max( Math.abs(derivativeX(intervalT[0])), Math.abs(derivativeX(intervalT[1]))) );
        double dt = s / m;

        for (double t = intervalT[1]; t < intervalT[0]; t += dt){
            double x = (t * t) / (t * t - 1);
            double y = (t * t + 1)/(t + 2);

            int coordinateX = (int) Math.round((x) * length + centerAxisX);
            int coordinateY = (int) Math.round((-y) * length + centerAxisY);

            if ((coordinateX > 0 && coordinateX < canvasWidth) && (coordinateY > 0 && coordinateY < canvasHeight))
                bufferedImage.setRGB(coordinateX, coordinateY, 0);
        }
    }

    private double derivativeX(double t)
    {
        return (-2 * t)/(t * t - 1)/(t * t - 1);
    }

    private double derivativeY(double t)
    {
        return (t * t + 4 * t - 1)/(t + 2)/(t + 2);
    }

    private double[] getIntervalForT(double minT, double maxT, double maxXT, double minXT, double maxYT, double minYT,
                                     boolean includeLeftLimitForT, boolean includeRightLimitForT) {
        double[] limits;

        /*minY- минимально видимая координата Y( не кривой, а вообще)
        * minYT- минимальное значение Y( возможно, не видно)
        * visibleMinY - минимальное видимое значение кривой по Y*/
        double visibleMaxX = Math.min(maxX, maxXT);
        double visibleMaxY = Math.min(maxY, maxYT);
        double visibleMinY = Math.max(-minY, minYT);
        double visibleMinX = Math.max(-minX, minXT);

        //System.out.println(visibleMaxX +", "+visibleMinX +", "+visibleMaxY +", "+visibleMinY);
        /* бред, такого быть не должно- ошибка т.е. кривую не видно*/
        if (visibleMaxX < visibleMinX || visibleMaxY < visibleMinY)
            return null;

        /* решаем 4 уравнения: для всех видимыx максимумов и минимумов , например, visibleMaxX = x(t) => находим t */
        Vector<Double> rootsForX = solveTwoEquationForX( visibleMaxX, visibleMinX, minT, maxT, includeLeftLimitForT, includeRightLimitForT);
        Vector<Double> rootsForY = solveTwoEquationForY(visibleMaxY, visibleMinY, minT, maxT, includeLeftLimitForT, includeRightLimitForT);
        //отмечаем все эти t на прямой и выбираем промежутки, по которым нам ходить необходимо, а по каким не обязательно.
        rootsForX.addAll(rootsForY);
        limits = selectMainInterval( rootsForX );

        //записываем промежутки, по которым t должно проходить в limits
        return limits;
    }

    private double[] selectMainInterval(Vector<Double> values) {
        if (values.size() == 0){
            return null;
        }
        double max = values.get(0), min = values.get(0);
        for (int i = 0; i < values.size(); i ++ ){
            System.out.print(values.get(i)+" ");
            if ( values.get(i) > max)
                max = values.get(i);
            if ( values.get(i) < min)
                min = values.get(i);
        }
        System.out.println();
        double[] limits = new double[]{max,min};
        return limits;
    }


    /*задано  значение x(t), нужно найти корни этого уравнения- найти все t*/
    Vector<Double> solveTwoEquationForX(double equationsValue1,double equationsValue2, double leftLimitForT, double rightLimitForT,
                                        boolean includeLeftLimitForT, boolean includeRightLimitForT ){
        Vector<Double> roots = new Vector<Double>();

        if (equationsValue1 != 1.0 || equationsValue1 != -1.0) {

        double rootT1 = Math.sqrt( equationsValue1 / (equationsValue1 - 1) );

        if ( checkRootLyingInInterval(rootT1, leftLimitForT, rightLimitForT, includeLeftLimitForT, includeRightLimitForT))
            roots.add(rootT1);

        if ( checkRootLyingInInterval( -rootT1, leftLimitForT, rightLimitForT, includeLeftLimitForT, includeRightLimitForT))
                roots.add( -rootT1);

        if (equationsValue2 != 1.0 || equationsValue2 != -1.0){
        double rootT2 = Math.sqrt( equationsValue2 / (equationsValue2 - 1) );

        if ( checkRootLyingInInterval(rootT2, leftLimitForT, rightLimitForT, includeLeftLimitForT, includeRightLimitForT))
            roots.add(rootT2);
        if ( checkRootLyingInInterval( -rootT2, leftLimitForT, rightLimitForT, includeLeftLimitForT, includeRightLimitForT))
            roots.add( -rootT2);
        }}

        return roots;
    }
     

    Vector<Double> solveTwoEquationForY(double equationsValue1, double equationsValue2, double leftLimitForT, double rightLimitForT,
                                        boolean includeLeftLimitForT, boolean includeRightLimitForT ){
        Vector<Double> roots = new Vector<Double>();

        if (equationsValue1 != -2.0){

            double d1 = equationsValue1 * equationsValue1 - 4 * ( 1 - 2 * equationsValue1);

            if ( d1 > 0 ){
                double rootT11 = ( equationsValue1 + Math.sqrt(d1) ) / 2;
                double rootT12 = ( equationsValue1 - Math.sqrt(d1) ) / 2;

                if (checkRootLyingInInterval( rootT11, leftLimitForT, rightLimitForT, includeLeftLimitForT, includeRightLimitForT))
                    roots.add(rootT11);

                if (checkRootLyingInInterval( rootT12, leftLimitForT, rightLimitForT, includeLeftLimitForT, includeRightLimitForT))
                    roots.add(rootT12);

            } else if ( d1 == 0){
                double rootT1 = equationsValue1/2;
                if (checkRootLyingInInterval( rootT1, leftLimitForT, rightLimitForT, includeLeftLimitForT, includeRightLimitForT))
                    roots.add(rootT1);
            }
        }


        if (equationsValue2 != -2.0){
            double d2 = equationsValue2 * equationsValue2 - 4 * ( 1 - 2 * equationsValue2);

            if ( d2 > 0 ){
                double rootT21 = ( equationsValue2 + Math.sqrt(d2) ) / 2;
                double rootT22 = ( equationsValue2 - Math.sqrt(d2) ) / 2;

                if (checkRootLyingInInterval( rootT21, leftLimitForT, rightLimitForT, includeLeftLimitForT, includeRightLimitForT))
                    roots.add(rootT21);

                if (checkRootLyingInInterval( rootT22, leftLimitForT, rightLimitForT, includeLeftLimitForT, includeRightLimitForT))
                    roots.add(rootT22);

            } else if ( d2 == 0 ){
                double rootT2 = equationsValue2/2;
                if (checkRootLyingInInterval( rootT2, leftLimitForT, rightLimitForT, includeLeftLimitForT, includeRightLimitForT))
                    roots.add(rootT2);
            }
        }

        return roots;
    }

    public boolean checkRootLyingInInterval( double root, double leftLimit, double rightLimit, boolean includeLeftLimit, boolean includeRightLimit){
        if ( ( root < rightLimit || (root <= rightLimit && includeRightLimit))
           && ( root > leftLimit || (root >= leftLimit && includeLeftLimit))){
            return true;
        }
        return false;
    }

    private void drawAxis() {
        graphics2D.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());




        if (centerAxisX < canvasWidth)
        for (int i = (int) (centerAxisX + length); i < canvasWidth; i+=length){
            for (int j = 0; j < canvasHeight; j++)
                if ( (i > 0 && i < canvasWidth) && ( j > 0 && j < canvasHeight)){
                    bufferedImage.setRGB( i, j,0xe4e9e4);
                }
        }
        if (centerAxisX > 0)
            for (int i = (int) (centerAxisX - length); i > 0; i-=length){
                for (int j = 0; j < canvasHeight; j++)
                    if ( (i > 0 && i < canvasWidth) && ( j > 0 && j < canvasHeight)){
                        bufferedImage.setRGB( i, j,0xe4e9e4);
                    }
            }
        if (centerAxisY < canvasHeight)
            for (int i = (int) (centerAxisY + length); i < canvasHeight; i+=length){
                for (int j = 0; j < canvasWidth; j++)
                    if ( (j > 0 && j < canvasWidth) && ( i > 0 && i < canvasHeight)){
                        bufferedImage.setRGB( j, i,0xe4e9e4);
                    }
            }
        if (centerAxisY > 0)
            for (int i = (int) (centerAxisY - length); i > 0; i-=length){
                for (int j = 0; j < canvasWidth; j++)
                    if ( (j > 0 && j < canvasWidth) && ( i > 0 && i < canvasHeight)){
                        bufferedImage.setRGB( j, i, 0xe4e9e4);
                    }
            }

        if (centerAxisY > 0 && centerAxisY < canvasHeight)
            for ( int i = 0; i < canvasWidth; i++)
                bufferedImage.setRGB(i,centerAxisY,0);
        if (centerAxisX > 0 && centerAxisX < canvasWidth)
            for ( int i = 0; i < canvasHeight; i++)
                bufferedImage.setRGB(centerAxisX,i,0);
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        if (!dnd)
            return;
        centerAxisX += e.getX() - currentX;
        currentX = e.getX();
        centerAxisY += e.getY() - currentY;
        currentY = e.getY();

        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currentX = e.getX();
        currentY = e.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() > 0)
            length += sclareStep;
        else length-= sclareStep;
        repaint();
    }


    public void setSclareStep(Integer sclareStepValue) {
        this.sclareStep = sclareStepValue;
    }

    public void setMotionStep(Integer motionStep) {
        this.motionStep = motionStep;
    }

    public Integer getSclareStep() {
        return new Integer(sclareStep);
    }

    public Integer getMotionStep() {
        return new Integer(motionStep);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println(e.getExtendedKeyCode()+"!!!"+e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void moveUp() {
        centerAxisY += motionStep;
        repaint();
    }

    public void moveDown() {
        centerAxisY -= motionStep;
        repaint();
    }

    public void moveLeft() {
        centerAxisX += motionStep;
        repaint();
    }

    public void moveRight() {
        centerAxisX -= motionStep;
        repaint();
    }

    public void zoomIn() {
        length += sclareStep;
        repaint();
    }

    public void zoomOut() {
        length -= sclareStep;
        repaint();
    }

    public void changeDND() {
        if (dnd)
            dnd = false;
        else dnd = true;
    }
}