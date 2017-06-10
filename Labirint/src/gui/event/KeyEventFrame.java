package gui.event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyEventFrame extends JFrame implements KeyListener {

    public static void main(String[] args){
        JFrame f = new KeyEventFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }


    public KeyEventFrame(){
        setTitle("Key gui.event frame...");
        getContentPane().setBackground(Color.GREEN);
        setBounds(10, 20, 100, 100);
        addKeyListener(this);
    }



    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("" + e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("" + e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("" + e);
    }
}
