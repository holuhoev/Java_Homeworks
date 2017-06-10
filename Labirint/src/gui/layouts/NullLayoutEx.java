package gui.layouts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class NullLayoutEx extends JFrame {

    public NullLayoutEx() {
        setTitle("Null Layout example");

        setBounds(20, 80, 300, 300);   // setting frame size and placement

        Container c = getContentPane();
        c.setLayout(null);

        JButton jb = new JButton("Button");
        jb.setBounds(200, 50, 90, 40); // setting coordinates and size for a button
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("got gui.event: " + e);
            }
        });
        c.add(jb);

        JTextArea jta = new JTextArea();
        jta.setBounds(10, 130, 180, 70);// setting coordinates and size for a text area...
        jta.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println("got gui.event: " + e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("got gui.event: " + e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("got gui.event: " + e);
            }
        });
        jta.setText("You can input text here");
        c.add(jta);
    }

    public static void main(String args[]) {
        NullLayoutEx nl = new NullLayoutEx();
        nl.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        nl.setVisible(true);
    }
}
