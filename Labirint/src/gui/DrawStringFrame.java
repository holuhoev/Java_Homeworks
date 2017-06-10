package gui;

import javax.swing.*;
import java.awt.*;

public class DrawStringFrame extends JFrame {

    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 200;

    public static void main(String[] args){
        JFrame f = new DrawStringFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public DrawStringFrame(){
        setTitle("DrawStringFrame");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        JPanel p = new DrawStringPanel();
System.out.println("frame Layout: " + this.getContentPane().getLayout() + "; thread = " + Thread.currentThread());
        add(p);
//        getContentPane().add(p);
    }
}

class DrawStringPanel extends JPanel{
    public static final int MESSAGE_X = 100; //75;      //todo: play with that values ...
    public static final int MESSAGE_Y = 100;

    DrawStringPanel(){
        setBackground(Color.GREEN);
        System.out.println(" panel Layout: " + this.getLayout() + "; thread = " + Thread.currentThread());
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g); // to paint background etc... //todo: comment & uncomment it...
System.out.println("paintComponent(" + g + ") invoked...; \r\n thread = " + Thread.currentThread());
        g.drawString("MARIA", MESSAGE_X, MESSAGE_Y);
    }
}