package UI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

import Level.Level;
import Sound.SoundPlayer;
import Utilitary.Gamepad;
import Utilitary.V2D;

public abstract class UI implements KeyListener, MouseMotionListener, MouseInputListener {

    public static UI ActualUI = null;
    protected List<UIElement> elements = new ArrayList<>();
    private static int mouseX = 0;
    private static int mouseY = 0;
    public static UIElement gamepadCursor = new UIElement(new V2D(950, 540), new V2D(1,1), "cursor", false);
    public static boolean bGamepad = false;
    private static float cursorAnimation = 0.f;
    public enum EGameState {
        GameMenu,
        Inventory,
        MainMenu,
        InGame,
        PowerUp,
        CharacterChoice,
        UIMerchant,
        GameOver
    }
    public static EGameState ActualState = EGameState.MainMenu;
    public static Map <EGameState, UI> UIs = Map.of(EGameState.MainMenu, new MainMenu(),EGameState.GameMenu, new GameMenu(),EGameState.Inventory, new Inventory(),EGameState.InGame, new InGame(), EGameState.PowerUp, new PowerUp(), EGameState.CharacterChoice, new CharacterChoice(), EGameState.UIMerchant, new UIMerchant(), EGameState.GameOver, new GameOver());

    public UI() {}

    // Chaque UI override keyPressed / keyReleased / keyTyped
    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        if (UI.ActualState == EGameState.PowerUp) return;
        
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_ESCAPE -> {
                UI.ActualState = UI.ActualState != EGameState.GameMenu?EGameState.GameMenu:EGameState.InGame;
                SoundPlayer.playSFX("Open.wav");
            }
            case KeyEvent.VK_TAB -> {
                UI.ActualState = UI.ActualState != EGameState.Inventory?EGameState.Inventory:EGameState.InGame;
                SoundPlayer.playSFX("Inventory.wav");
            }
            default -> {
            }
        }
    };
    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {};
    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {};

    /**
     * Mise à jour de la position de la souris et orientation du joueur
     * en direction de celle-ci, lorsque la manette n’est pas utilisée.
     *
     * @param e événement souris
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {
        if (Gamepad.gamepad.isConnected()) return;
        mouseX = e.getX();
        mouseY = e.getY();
    }
    /**
     * Gestion du déplacement de la souris en maintenant un bouton enfoncé.
     * Réutilise la logique de {@link #mouseMoved(MouseEvent)} si la manette n’est pas active.
     *
     * @param e événement souris
     */
    @Override
    public void mouseDragged(MouseEvent e)
    {
        
    }

    /**
     * Non utilisé : clic souris.
     *
     * @param e événement souris
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (ActualState == EGameState.InGame) return;

        for (UIElement el : UIs.get(ActualState).elements)
        {
            if (el != null)
            {
                if (el instanceof UIButton btn && btn.contains(mouseX, mouseY)) btn.click();
            }
        }
    }

    /**
     * Non utilisé : bouton souris enfoncé.
     *
     * @param e événement souris
     */
    @Override
    public void mousePressed(MouseEvent e) { }

    /**
     * Non utilisé : bouton souris relâché.
     *
     * @param e événement souris
     */
    @Override
    public void mouseReleased(MouseEvent e) { }

    /**
     * Non utilisé : entrée de la souris dans la fenêtre.
     *
     * @param e événement souris
     */
    @Override
    public void mouseEntered(MouseEvent e) { }

    /**
     * Non utilisé : sortie de la souris de la fenêtre.
     *
     * @param e événement souris
     */
    @Override
    public void mouseExited(MouseEvent e) { }


    public List<UIElement> getElements() {
        return elements;
    }

    public void update(float dt) {};

    public static void update(float dt, JFrame window)
    {
        if (ActualState == EGameState.GameOver && Level.getActualRoom() != null) Level.dischargeLevel();

        UIs.get(ActualState).update(dt);

        gamepadCursor.position.x = mouseX;
        gamepadCursor.position.y = mouseY;
        
        boolean bHover = false;
        for (UIElement e : UIs.get(ActualState).elements)
        {
            if (e != null)
            {
                e.update(dt);
                if (e instanceof UIButton btn && btn.contains(mouseX, mouseY))
                {
                    bHover = true;
                    break;
                }
            }
        }

        if (bHover)
        {
            if (cursorAnimation == 0.f) cursorAnimation = 0.03f;
            else if (gamepadCursor.size.x >= 1.5f) cursorAnimation = -0.03f;
            else if (gamepadCursor.size.x <= 0.85f) cursorAnimation = 0.03f;
            gamepadCursor.size.x += cursorAnimation;
            gamepadCursor.size.y += cursorAnimation;
        }
        else
        {
            gamepadCursor.size.x = 1;
            gamepadCursor.size.y = 1;
            cursorAnimation = 0.f;
        }
    }

    public static void addMouseX(double x) { mouseX += x; }
    public static void addMouseY(double y) { mouseY += y; }
    public static int getMouseX() { return mouseX; }
    public static int getMouseY() { return mouseY; }
}
