package SimpleGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

enum elements {
    Nill, Wall, Hero, Bomb , Boom
}

enum modes {
    Game, Options
}

public class Labirint extends JFrame {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;


    public static void main(String[] args) {
        JFrame f = new SimpleGUI.Labirint();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(WIDTH, HEIGHT);
        f.setResizable(false);
        f.setVisible(true);
    }

    public Labirint() {
        setTitle("Labirint");


        JPanel p = new SimpleGUI.DPanel(this);
        add(p);
    }
}

class DPanel extends JPanel {

    private elements[][] matrix;
    public final int CELL = 25;
    private int size;
    private elements current;
    private Image bomb, hero,wall,boom;
    private boolean isHeroExist;
    private int heroCoordinates;
    private modes mode;

    BufferedImage createResizedCopy(Image originalImage,
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

    DPanel(JFrame frame) {
        isHeroExist = false;
        size = Labirint.WIDTH / CELL;
        matrix = new elements[size][size];

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                matrix[i][j] = elements.Nill;
        try {
            File pathToFile = new File("/Users/evgenijlebedev/IdeaProjects/Labirint/bomb.png");
            BufferedImage image = ImageIO.read(pathToFile);
            bomb = createResizedCopy(image, 25, 25, false);

            pathToFile = new File("/Users/evgenijlebedev/IdeaProjects/Labirint/dota.png");
            image = ImageIO.read(pathToFile);
            hero = createResizedCopy(image, 25, 25, false);

            pathToFile = new File("/Users/evgenijlebedev/IdeaProjects/Labirint/wall.jpg");
            image = ImageIO.read(pathToFile);
            wall = createResizedCopy(image, 25, 25, false);

            pathToFile = new File("/Users/evgenijlebedev/IdeaProjects/Labirint/boom.jpg");
            image = ImageIO.read(pathToFile);
            boom= createResizedCopy(image, 25, 25, false);


        } catch (IOException e) {
            e.printStackTrace();
        }
        KeyListener k = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) { // if pressed ctrl + ....
                    if ((e.getKeyCode() == KeyEvent.VK_G)) {
                        mode = modes.Game;
                        frame.setTitle(mode.toString());
                    } else {
                        mode = modes.Options;
                        if ((e.getKeyCode() == KeyEvent.VK_W)) {
                            current = elements.Wall;
                            frame.setTitle(current.toString());
                            return;
                        }
                        if ((e.getKeyCode() == KeyEvent.VK_B)) {
                            current = elements.Bomb;
                            frame.setTitle(current.toString());
                            return;
                        }
                        if ((e.getKeyCode() == KeyEvent.VK_H)) {
                            current = elements.Hero;
                            frame.setTitle(current.toString());
                            return;
                        }
                    }
                } else {
                    if (mode == modes.Game) {
                        moveHero(e.getKeyCode());
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };


        MouseAdapter ma = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (mode == modes.Options) {
                    int x = (int) e.getPoint().getX();
                    int y = (int) e.getPoint().getY();

                    x = x / CELL;
                    y = y / CELL;

                    //delete previous hero
                    if (current == elements.Hero && isHeroExist)
                        matrix[heroCoordinates / size][heroCoordinates % size] = elements.Nill;

                    matrix[x][y] = current;


                    repaint();
                }
            }
        };

        frame.addKeyListener(k);
        this.addMouseListener(ma);
        this.addMouseMotionListener(ma);
    }

    private void moveHero(int keyCode) {
        if (isHeroExist) {
            int x = heroCoordinates / size;
            int y = heroCoordinates % size;
            int nx = -1;
            int ny = -1;
            switch (keyCode) {
                case KeyEvent.VK_LEFT: {
                    nx = (x - 1 + size) % size;
                    ny = y;
                    break;
                }
                case KeyEvent.VK_RIGHT: {
                    nx = (x+1)%size;
                    ny = y ;
                    break;
                }
                case KeyEvent.VK_UP: {
                    nx = x;
                    ny = (y - 1 + size) % size;
                    break;
                }
                case KeyEvent.VK_DOWN: {
                    nx = x;
                    ny = (y + 1) % size;
                    break;
                }
                default:
                    break;
            }
            if (nx != -1 && ny != -1) {
                if (matrix[nx][ny] == elements.Nill) {
                    matrix[x][y] = elements.Nill;
                    matrix[nx][ny] = elements.Hero;
                    repaint();
                }
                if (matrix[nx][ny] == elements.Bomb) {
                    matrix[x][y] = elements.Nill;
                    matrix[nx][ny] = elements.Boom;
                    repaint();
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Graphics2D g2 = (Graphics2D) g;
                if (matrix[i][j] == elements.Wall) {
                    g.drawImage(wall, i * CELL, j * CELL, null);
                }
                if (matrix[i][j] == elements.Bomb)
                    g.drawImage(bomb, i * CELL, j * CELL, null);
                if (matrix[i][j] == elements.Hero) {
                    isHeroExist = true;
                    g.drawImage(hero, i * CELL, j * CELL, null);
                    heroCoordinates = i * size + j;
                }
                if (matrix[i][j] == elements.Boom) {
                    g.drawImage(boom, i * CELL, j * CELL, null);
                }


            }
        }
    }
}
