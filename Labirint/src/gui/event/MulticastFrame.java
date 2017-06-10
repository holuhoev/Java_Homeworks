package gui.event;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MulticastFrame extends JFrame {
    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 200;

    public static void main(String[] args) {
        JFrame f = new MulticastFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public MulticastFrame(){
        setTitle("MulticastTest");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        add(new MulticastPanel());
    }
}

class MulticastPanel extends JPanel {
    MulticastPanel(){
        JButton newButton = new JButton("New");
        add(newButton);
        final JButton closeAllButton = new JButton("Close all");
        add(closeAllButton);

        ActionListener newListener =  new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                BlankFrame bf = new BlankFrame(closeAllButton);
                bf.setVisible(true);
            }
        };
        newButton.addActionListener(newListener);

    }
}

class BlankFrame extends JFrame /*implements WindowListener*/{
    public static final int DEFAULT_WIDTH = 200;
    public static final int DEFAULT_HEIGHT = 150;
    public static final int SPACING = 40;
    private static int counter = 0;

    private ActionListener closeListener;
    private final JButton closeButton;

    BlankFrame(final JButton closeButton){
        this.closeButton = closeButton;
        counter++;
        setTitle("Frame " + counter);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocation(SPACING * counter, SPACING * counter);
        closeListener = new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                BlankFrame.this.closeButton.removeActionListener(closeListener);
                dispose();
                counter--;
            }
        };
        closeButton.addActionListener(closeListener);
    }
}