package gui;
import javax.swing.*;

public class SimpleFrameTest extends JFrame {

    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 200;

    public SimpleFrameTest(){
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);     //todo: compare with previous & warning...
//        setUndecorated(true);                                      //todo: show uncommented...
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
//        setTitle("My First Frame");
        setTitle("Мой первый Frame");

//        add(new JButton("Моя кнопка - не трогать!!!"));
        setVisible(true);
    }

    public static void main(String[] args){
//        System.out.println(Thread.currentThread());

        JFrame frame = new SimpleFrameTest();
System.out.println("frame: " + frame);

//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setUndecorated(true);
//        frame.setTitle("My First Frame");
//        frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
//        frame.setVisible(true);
         System.out.println("FINISHING MAIN");
    }

    protected void finalize(){
        System.out.println("my finalize() invoked...");
    }
}



