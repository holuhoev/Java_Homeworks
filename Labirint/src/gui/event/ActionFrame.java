package gui.event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ActionFrame extends JFrame {
    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 200;

    public static void main(String[] args) {
        JFrame f = new ActionFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public ActionFrame() {
        setTitle("ActionTest");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        add(new ActionPanel());
    }
}

class ActionPanel extends JPanel {
    ActionPanel(){

        Action yellowAction =
                new ColorAction("Yellow", new ImageIcon(getClass().getResource("yellow-ball.gif")), Color.YELLOW);
        Action blueAction =
                new ColorAction("Blue", new ImageIcon(getClass().getResource("blue-ball.gif")), Color.BLUE);
        Action redAction =
                new ColorAction("Red", new ImageIcon(getClass().getResource("red-ball.gif")), Color.RED);

        add(new JButton(yellowAction));
        add(new JButton(blueAction));
        add(new JButton(redAction));

        // link keys to names:   TODO: see - what does it mean...
        InputMap imap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        imap.put(KeyStroke.getKeyStroke("ctrl Y"), "panel.yellow");
        imap.put(KeyStroke.getKeyStroke("ctrl B"), "panel.blue");
        imap.put(KeyStroke.getKeyStroke("ctrl R"), "panel.red");
        // link names to actions: TODO: see - what does it mean...
        ActionMap amap = getActionMap();
        amap.put("panel.yellow", yellowAction);
        amap.put("panel.blue", blueAction);
        amap.put("panel.red", redAction);
    }

    class ColorAction extends AbstractAction {

        ColorAction(String name, Icon icon, Color c){
            putValue(Action.NAME, name);
            putValue(Action.SMALL_ICON, icon);
            putValue(Action.SHORT_DESCRIPTION, "set panel colot to " + name.toLowerCase());
            putValue("color", c);
        }

        public void actionPerformed(ActionEvent e) {
            Color c = (Color)getValue("color");
            setBackground(c);
        }
    }
}

