package gui.layouts;

import javax.swing.*;
import java.awt.*;

public class GridLayoutEx extends JFrame {

    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;

    public static void main(String[] args){
        JFrame f = new GridLayoutEx();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public GridLayoutEx(){
        setTitle("GridLayout");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        init();
    }

    private Component b[] = new Component[7];

    public void init() {
        setLayout(new GridLayout(2, 4)); // 2 strings and 4 columns

        for (int i = 0; i < b.length; i++)
            add((b[i] = new JButton("(" + i +  ")")));
    }
}
