package gui;

import javax.swing.*;
import java.awt.*;

public class CenteredFrame extends JFrame {

    private static final String ICON_FILENAME =
            ".\\Seminar_17\\src\\gui\\blue-ball.gif";

    public CenteredFrame(){
        // discover screen size:
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        System.out.println("toolkit: " + toolkit);

        Dimension screenSize = toolkit.getScreenSize();
        System.out.println(screenSize);

        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;

        // position in screen center:
        setSize(screenWidth / 2, screenHeight / 2);
        setLocation(screenWidth / 4, screenHeight / 4);
        // set title and pictogram icon (java cup icon!):
        Image image = toolkit.getImage(ICON_FILENAME);
//        Image image = toolkit.getImage("icon.gif");
        setIconImage(image);
        setTitle("CenteredFrame");

//        add(new JPanel());
    }
    public static void main(String[] args){
        JFrame frame = new CenteredFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

