package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LookAndFeelTest extends JFrame {

    public LookAndFeelTest(){
        // discover screen size:
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        System.out.println("toolkit: " + toolkit);

        Dimension screenSize = toolkit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        // position in screen center:
        setSize(screenWidth / 2, screenHeight / 2);
        setLocation(screenWidth / 4, screenHeight / 4);
        // set title and pictogram icon (java cup icon!):
//        Image image = toolkit.getImage("icon.gif");
//        setIconImage(image);
        setTitle("LookAndFeelTest");

        System.out.println("frame layout: " + getContentPane().getLayout());

        add(new LookAndFeelPanel());
    }
    public static void main(String[] args){
        JFrame f = new LookAndFeelTest();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}

class LookAndFeelPanel extends JPanel {

    LookAndFeelPanel(){
        System.out.println("panel layout: " + getLayout());

        UIManager.LookAndFeelInfo[] infos =
                UIManager.getInstalledLookAndFeels();
        for(UIManager.LookAndFeelInfo info : infos){
            System.out.println(info.getName());
            makeButton(info.getName(), info.getClassName());
        }
    }

    void makeButton(String name, final String className){
        JButton b = new JButton(name);
        add(b);
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try{
                    UIManager.setLookAndFeel(className);
                    SwingUtilities.updateComponentTreeUI(
                            LookAndFeelPanel.this);
                }catch(Exception ex){
                    System.err.println("Exception: " + ex);
                }
            }
        });
    }
}