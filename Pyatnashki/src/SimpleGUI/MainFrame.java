package SimpleGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class MainFrame extends JFrame
        implements ActionListener {

    private JMenuItem newMenuItem, exitMenuItem;
    private JPanel panel;

    private int[] replaces = new int[16];
    private JButton[] buttons = new JButton[16];

    private static int count = 0;

    public MainFrame() {
        super("Пятнашки");

        for (int i = 0; i < 16; i++)
            buttons[i] = new JButton();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;

        // position in screen center:
        setBounds(0, 0, 400, 400);
        setLocation(screenWidth / 4, screenHeight / 4);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setJMenuBar(createMenuBar());
        panel = new JPanel(new GridLayout(4, 4, 2, 2));

        Container container = getContentPane();
        panel.setDoubleBuffered(true);
        container.add(panel);

        setResizable(false);
        setVisible(true);


        runGame("rez.jpg");
    }

    private void runGame(String path) {
        for (int i = 0; i < replaces.length; ++i)
            replaces[i] = i;

        if (parseImageAndFillButtons(path)) {
            fillPanel();
            unlockButtons();

            Object[] options = {"OK"};
            int n = JOptionPane.showOptionDialog(this,
                    "You have some time to have a go at remembering the picture", "Are you ready? ",
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            generateReplaces();
            fillPanel();
        }
    }

    private BufferedImage createResizedCopy(Image originalImage,
                                            int scaledWidth, int scaledHeight,
                                            boolean preserveAlpha) {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

    private boolean parseImageAndFillButtons(String path) {
        try {
            Image tempImage = null;

            //read Image
            File pathToFile = new File(path);
            BufferedImage image = ImageIO.read(pathToFile);

            //do resize
            image = createResizedCopy(image, 400, 400, false);

            //parse
            if (image != null) {
                int step = image.getWidth() / 4;

                for (int i = 0; i < buttons.length; i++) {
                    int x = (i % 4) * step;
                    int y = (i / 4) * step;
                    tempImage = createImage(new FilteredImageSource(image.getSource(), new CropImageFilter(x, y, step, step)));

                    buttons[i].setIcon(new ImageIcon(tempImage));
                }

                return true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;

    }

    private void fillPanel() {
        panel.removeAll(); // clear previous buttons

        ImageIcon[] images = new ImageIcon[16];


        //save images
        for (int i = 0; i < replaces.length; ++i)
            images[i] = (ImageIcon) buttons[i].getIcon();

        //put images in the buttons
        for (int i = 0; i < replaces.length; ++i) {

            if (replaces[i] == 15)
                buttons[i].setVisible(false);
            else
                buttons[i].setVisible(true);

            buttons[i].setIcon(images[replaces[i]]);

            buttons[i].setName(Integer.toString(i));
            buttons[i].setFocusable(false);

            buttons[i].addActionListener(new ButtonActionListiner());

            panel.add(buttons[i]);
        }

        panel.validate();
        panel.repaint();
    }


    private void generateReplaces() {
        Random generator = new Random();

        do {
            for (int i = 0; i < replaces.length; i++)
                replaces[i] = 0;

            for (int i = 1; i < replaces.length; i++) {
                int temp;

                do {
                    temp = generator.nextInt(16);
                }
                while (replaces[temp] != 0);

                replaces[temp] = i;
            }
        }
        while (!canBeSolved(replaces));
    }

    private boolean canBeSolved(int[] replaces) {
        int sum = 0;

        for (int i = 0; i < 16; i++) {
            if (replaces[i] == 15) {
                sum += i / 4 + 1; //номер ряда пустой клетки
                continue;
            }

            for (int j = i + 1; j < 16; j++) {
                if (replaces[j] < replaces[i])
                    sum++;
            }
        }

        return sum % 2 == 0;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu;
        JRadioButtonMenuItem rbMenuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("Game");
        menuBar.add(menu);


        newMenuItem = new JMenuItem("New", KeyEvent.VK_N);
        newMenuItem.addActionListener(this);
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
        menu.add(newMenuItem);

        exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_Q);
        exitMenuItem.addActionListener(this);
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menu.add(exitMenuItem);

        menu = new JMenu("Style");
        menuBar.add(menu);

        //Add items to style menu
        ButtonGroup group = new ButtonGroup();

        //get info about lookandfeels
        UIManager.LookAndFeelInfo[] infos =
                UIManager.getInstalledLookAndFeels();

        for (UIManager.LookAndFeelInfo info : infos) {
            rbMenuItem = new JRadioButtonMenuItem(info.getName());
            group.add(rbMenuItem);
            menu.add(rbMenuItem);

            rbMenuItem.addActionListener(e -> {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                    SwingUtilities.updateComponentTreeUI(MainFrame.this);
                } catch (Exception ex) {
                    System.err.println("Exception: " + ex);
                }
            });
        }

        return menuBar;
    }

    private class ButtonActionListiner implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            String name = ((JButton) e.getSource()).getName();
            changeReplaces(name);
        }
    }

    private void changeReplaces(String name) {
        int n = Integer.parseInt(name);
        int j = 0;

        //find empty button
        while (replaces[j] != 15)
            j++;

        //check whether change step can be solves
        if (isNear(n, j)) {
            swap(n, j);
            count++;
            changePanel(n, j);
        }

        if (checkWin()) {
            lockButtons();
            String text = (Integer.toString(count));
            buttons[n].setText(text);
            buttons[n].setVisible(true);
            JOptionPane.showMessageDialog(null, "YOU WIN!", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void lockButtons() {
        for (JButton button : buttons) {
            button.setEnabled(false);
        }
    }

    private void unlockButtons() {
        for (JButton button : buttons) {
            button.setEnabled(true);
        }
    }

    private void changePanel(int i, int j) {
        //Change image place
        ImageIcon imageIcon = (ImageIcon) buttons[i].getIcon();
        buttons[i].setIcon(null);
        buttons[i].setVisible(false);

        buttons[j].setIcon(imageIcon);
        buttons[j].setVisible(true);
    }

    public boolean checkWin() {
        for (int i = 0; i < replaces.length; ++i)
            if (replaces[i] != i)
                return false;

        return true;
    }

    void swap(int i, int j) {
        int temp = replaces[i];
        replaces[i] = replaces[j];
        replaces[j] = temp;
    }

    boolean isNear(int i, int j) {
        if (i == j + 1)
            return true;
        if (i == j - 1)
            return true;
        if (i == j - 4)
            return true;
        if (i == j + 4)
            return true;
        return false;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newMenuItem) {
            try {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                //set filter
                FileFilter imageFilter = new FileNameExtensionFilter(
                        "Image files", ImageIO.getReaderFileSuffixes());
                chooser.addChoosableFileFilter(imageFilter);
                chooser.setAcceptAllFileFilterUsed(false);

                chooser.showOpenDialog(getParent());

                File file = chooser.getSelectedFile();
                runGame(file.getAbsolutePath());

            } catch (Exception ex) {
                System.err.println("Exception: " + ex);
            }
        }
        if (e.getSource() == exitMenuItem) {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }
}
