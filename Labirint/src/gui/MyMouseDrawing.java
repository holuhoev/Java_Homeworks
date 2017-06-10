package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MyMouseDrawing extends JFrame {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;

    public static void main(String[] args){
        JFrame f = new MyMouseDrawing();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(WIDTH, HEIGHT);
        f.setVisible(true);
    }

    public MyMouseDrawing(){
        setTitle("Draw by mouse...");
        JPanel p = new DPanel();
        add(p);
    }
}

class DPanel extends JPanel {

    private static final Point breakPoint = new Point(-1, -1);
    java.util.List<Point> points = new ArrayList<Point>();
    Stroke stroke = new BasicStroke(3.F); // 3.0F

    DPanel(){
        setBackground(Color.WHITE);
        MouseAdapter ma = new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                points.add(e.getPoint());
                repaint();
            }
            public void mouseReleased(MouseEvent e){
                points.add(breakPoint);
                repaint();
            }
            public void mouseDragged(MouseEvent e){
                points.add(e.getPoint());
                System.out.println("N = " + points.size());
                repaint();
            }
        };
        this.addMouseListener(ma);
        this.addMouseMotionListener(ma);
    }

//    public void paint(Graphics g){
////        super.paint(g);
//
//        Graphics2D g2 = (Graphics2D)g;
//        g2.setStroke(stroke);
//        g2.setPaint(Color.BLACK);
//        for(int i = 1; i < points.size(); i++){
//            Point p0 = points.get(i - 1);
//            Point p1 = points.get(i);
//            if(p0 != breakPoint && p1 != breakPoint )
//                g2.drawLine(p0.x, p0.y, p1.x, p1.y);
//        }
//    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
System.out.println("paintComponent()...");
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(stroke);
        g2.setPaint(Color.BLACK);
        for(int i = 1; i < points.size(); i++){
            Point p0 = points.get(i - 1);
            Point p1 = points.get(i);
            if(p0 != breakPoint && p1 != breakPoint )
                g2.drawLine(p0.x, p0.y, p1.x, p1.y);
        }
    }
}

