package Graphism;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import Objects.Player;
import UI.UI;
import Utilitary.V2D;

public class Window {

    public static Window window = null;

    private final JFrame frame;
    private final GamePanel panel;

    // Facteurs de scale
    public static double scaleX = 1.0;
    public static double scaleY = 1.0;

    public Window() {
        this.frame = new JFrame("THE LAST JOURNEY");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(true);

        // Création du panel de jeu
        this.panel = new GamePanel();
        this.panel.setPreferredSize(new Dimension(1920, 1080));
        this.frame.setContentPane(this.panel);

        // Récupérer la taille de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Calcul des facteurs de scale en se basant sur une résolution de référence (1920x1080)
        scaleX = screenWidth / 1920.0;
        scaleY = screenHeight / 1080.0;

        // Passer en plein écran
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.frame.setVisible(true);
        this.panel.repaint();
    }

    public static void initialize()
    {
        window = new Window();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB); // totalement transparent
        Cursor cursorInvisible = toolkit.createCustomCursor(image, new Point(0, 0), "invisible");
        window.getPanel().setCursor(cursorInvisible);
        window.getPanel().setFocusable(true);
        window.getPanel().requestFocusInWindow();
        window.getPanel().addKeyListener(UI.UIs.get(UI.ActualState));
        window.getPanel().setFocusTraversalKeysEnabled(false);
        window.getPanel().addMouseMotionListener(UI.UIs.get(UI.ActualState));
        window.getPanel().addMouseListener(UI.UIs.get(UI.ActualState));

        Player p = new Player(new V2D(0,0), new V2D(0,0), new V2D(0,0), "");
        Player.setPlayer1(p);
    }

    public GamePanel getPanel() {
        return this.panel;
    }

    public JFrame getFrame()
    {
        return this.frame;
    }
}
