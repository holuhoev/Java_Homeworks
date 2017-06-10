package gui.event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonFrame extends JFrame {
    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 200;

    public static void main(String[] args){
        ButtonFrame f = new ButtonFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public ButtonFrame(){
        setTitle("ButtonTest");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        ButtonPanel p = new ButtonPanel();
        add(p);
    }
}

class ButtonPanel extends JPanel /*implements ActionListener*/ {

    JButton yellowButton = makeButton("Yellow" , Color.YELLOW);
    JButton blueButton = makeButton("Blue", Color.BLUE);
    JButton redButton = makeButton("Red", Color.RED);

//    JButton yellowButton = makeButton("Yellow");
//    JButton blueButton = makeButton("Blue");
//    JButton redButton = makeButton("Red");

    JButton makeButton(String name,  final Color c){
        JButton b = new JButton(name);
        add(b);
        b.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                setBackground(c);
            }
        });

        return b;
    }

//    JButton makeButton(String name){
//        JButton b = new JButton(name);
//        add(b);
//        b.addActionListener(this);
//        return b;
//    }

//    public void actionPerformed(ActionEvent e) {
//        final Object eventSource = e.getSource();
//        if(eventSource == yellowButton)
//            setBackground(Color.YELLOW);
//        else if(eventSource == blueButton)
//            setBackground(Color.BLUE);
//        else if(eventSource == redButton)
//            setBackground(Color.RED);
//    }
}