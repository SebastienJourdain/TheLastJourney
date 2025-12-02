package Objects;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_1;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwJoystickIsGamepad;
import static org.lwjgl.glfw.GLFW.glfwJoystickPresent;

import Graphism.Window;
import Level.Level;
import Sound.SoundPlayer;
import UI.UI;
import Utilitary.Gamepad;
import Utilitary.V2D;

/**
 * Représente le joueur principal.
 * Cette classe gère les entrées clavier, souris et manette,
 * l’orientation, le déplacement, le dash ainsi que certains effets d’équipement.
 */
public class Player extends Character implements KeyListener, MouseMotionListener, MouseListener
{
    /** Coordonnée X actuelle de la souris. */
    private int mouseX = 0;
    /** Coordonnée Y actuelle de la souris. */
    private int mouseY = 0;

    /** Liste des équipements actuellement portés par le joueur. */
    private final List<Equipment> equipments = new ArrayList<>();

    /** Indique si la touche de déplacement vers l’avant est enfoncée. */
    private boolean forward = false;
    /** Indique si la touche de déplacement vers l’arrière est enfoncée. */
    private boolean backward = false;
    /** Indique si la touche de déplacement vers la droite est enfoncée. */
    private boolean right = false;
    /** Indique si la touche de déplacement vers la gauche est enfoncée. */
    private boolean left = false;

    private V2D aim = new V2D(0, 0);

    /** Dernier état du trigger droit de la manette (utilisé pour détecter le relâchement). */
    public int previousTriggerState = GLFW_RELEASE;

    /** Référence statique vers le joueur principal. */
    private static Player Player1 = null;

    /**
     * Retourne le joueur principal.
     *
     * @return l’instance unique de {@code Player}, ou {@code null} si non initialisée
     */
    public static Player getPlayer1() { return Player1; }
    
    /**
     * Construit un joueur avec collision et sprite statique.
     * Initialise sa vitesse de déplacement, enregistre l’instance comme joueur principal
     * et configure GLFW.
     *
     * @param size          taille visuelle du joueur
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     * @param spriteName    nom du sprite
     */
    public Player(V2D size, V2D position, V2D collisionSize, String spriteName)
    {
        super(size, position, collisionSize, spriteName);
        Player1 = this;
    }

    /**
     * Construit un joueur avec collision, sprite et animation.
     * Ajuste la position de la collision et initialise GLFW.
     *
     * @param size          taille visuelle du joueur
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     * @param spriteName    nom du sprite
     * @param animName      nom de l’animation
     */
    public Player(V2D size, V2D position, V2D collisionSize, String spriteName, String animName)
    {
        super(size, position, collisionSize, spriteName, animName);
        Player1 = this;
    }

    /**
     * Ajoute une quantité d’or au joueur.
     *
     * @param gold montant d’or à ajouter
     */
    public void addGold(int gold) { this.setGold(this.getGold() + gold); }

    /**
     * Gestion de l’appui des touches de déplacement.
     *
     * @param e événement clavier
     */
    @Override
    public void keyPressed(KeyEvent e)
    {
        if (!this.forward && e.getKeyCode() == KeyEvent.VK_Z) this.forward = true;
        else if (!this.backward && e.getKeyCode() == KeyEvent.VK_S) this.backward = true;
        if (!this.right && e.getKeyCode() == KeyEvent.VK_D) this.right = true;
        else if (!this.left && e.getKeyCode() == KeyEvent.VK_Q) this.left = true;

        if (e.getKeyCode() == KeyEvent.VK_E)
        {
            for (Collision c : getCollision().getColliding())
            {
                if (c.getObject() instanceof Merchant)
                {
                    UI.ActualState = UI.EGameState.UIMerchant;
                    SoundPlayer.playSFX("ShopDoor.wav");
                    break;
                }
            }
        }
    }

    /**
     * Gestion du relâchement des touches de déplacement et du dash (barre d’espace).
     *
     * @param e événement clavier
     */
    @Override
    public void keyReleased(KeyEvent e)
    {
        if (this.forward && e.getKeyCode() == KeyEvent.VK_Z) this.forward = false;
        else if (this.backward && e.getKeyCode() == KeyEvent.VK_S) this.backward = false;
        if (this.right && e.getKeyCode() == KeyEvent.VK_D) this.right = false;
        else if (this.left && e.getKeyCode() == KeyEvent.VK_Q) this.left = false;

        if (!this.isbAction() && e.getKeyCode() == KeyEvent.VK_SPACE) dash();
    }

    /**
     * Non utilisé : saisie de caractère.
     *
     * @param e événement clavier
     */
    @Override
    public void keyTyped(KeyEvent e) { }

    /**
     * Mise à jour de la position de la souris et orientation du joueur
     * en direction de celle-ci, lorsque la manette n’est pas utilisée.
     *
     * @param e événement souris
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {
        if (glfwJoystickPresent(GLFW_JOYSTICK_1) && glfwJoystickIsGamepad(GLFW_JOYSTICK_1)) return;
        this.mouseX = e.getX();
        this.mouseY = e.getY();

        // Calcul du vecteur directionnel
        double dx = this.mouseX - this.getPosition().x;
        double dy = this.mouseY - this.getPosition().y;

        // Calcul de la longueur (norme du vecteur)
        double length = Math.sqrt(dx * dx + dy * dy);

        // Normalisation (éviter la division par zéro)
        if (length != 0) {
            dx /= length;
            dy /= length;
        }

        // Stockage dans le vecteur d’orientation
        if (dx != 0.f) { this.setOrientationX(dx); this.aim.x = dx; }
        if (dy != 0.f) { this.setOrientationY(dy); this.aim.y = dy; }
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
        if (glfwJoystickPresent(GLFW_JOYSTICK_1) && glfwJoystickIsGamepad(GLFW_JOYSTICK_1)) return;
        mouseMoved(e);
    }

    /**
     * Non utilisé : clic souris.
     *
     * @param e événement souris
     */
    @Override
    public void mouseClicked(MouseEvent e) { }

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

    /**
     * Retourne la position courante de la souris.
     *
     * @return un {@link Point} représentant la position de la souris
     */
    public Point getMousePosition()
    {
        return new Point(this.mouseX, this.mouseY);
    }

    /**
     * Action associée au bouton 0 de la manette.
     * Méthode prévue pour être surchargée dans les classes dérivées.
     */
    public void gamepadButton0() {}

    /**
     * Action associée au bouton 1 de la manette.
     * Méthode prévue pour être surchargée dans les classes dérivées.
     */
    public void gamepadButton1() { dash(); }

    /**
     * Action associée au trigger droit de la manette : déclenche un dash.
     */
    public void gamepadButtonRT() { }

    /**
     * Action associée au trigger droit de la manette : déclenche un dash.
     */
    public void gamepadButtonLT() { }

    /**
     * Effectue un dash dans la direction actuelle du joueur.
     * Le dash est limité par les collisions (via {@code getLongestDash}),
     * génère des effets visuels et déplace le joueur en plusieurs étapes.
     */
    protected void dash()
    {
        V2D longestDash = this.getCollision().getLongestDash(this.getOrientation(), 150.0);
        if (longestDash.x == this.getCollision().getPosition().x && longestDash.y == this.getCollision().getPosition().y) return;
        
        this.equipmentEffect("onDash");

        for (int i = 0; i < 3; i++)
        {
            Effect e = new Effect(new V2D(this.getSize()), new V2D(this.getPosition()), new V2D(-1,-1), "dash");
            if (this.getOrientation().x > 0) e.setbReverse(true);
            Level.getActualRoom().addObject(e);
            this.addX((longestDash.x / 3.0) / this.getCaracteristics().getSpeed());
            this.addY((longestDash.y / 3.0) / this.getCaracteristics().getSpeed());
        }
        SoundPlayer.playSFX("Dash.wav");
        
    }

    /**
     * Met à jour l’animation de marche en fonction de l’orientation du joueur.
     * Sélectionne l’animation {@code walk_top}, {@code walk_bot},
     * {@code walk_left} ou {@code walk_right} selon l’axe dominant.
     */
    private void orientate()
    {
        String anim = this.getSprite().getAnimName();
        double x = this.getOrientation().x;
        double y = this.getOrientation().y;

        float seuil = 0.1f;

        if (Math.abs(x) < seuil && Math.abs(y) < seuil) return;

        if (Math.abs(y) > Math.abs(x))
        {
            if (y < 0 && !anim.equals("walk_top")) this.getSprite().setAnimName("walk_top");
            else if (y > 0 && !anim.equals("walk_bot")) this.getSprite().setAnimName("walk_bot");
        }
        else
        {
            if (x < 0 && !anim.equals("walk_left")) this.getSprite().setAnimName("walk_left");
            else if (x > 0 && !anim.equals("walk_right")) this.getSprite().setAnimName("walk_right");
        }
    }

    /**
     * Met à jour le joueur à chaque frame.
     * Si aucune action spéciale n’est en cours, gère l’orientation, puis
     * choisit entre contrôle manette ou clavier/souris pour le déplacement.
     *
     * @param dt temps écoulé depuis la dernière mise à jour
     */
    @Override
    public void update(float dt)
    {
        super.update(dt);
        
        if (!this.isbAction()) orientate();
        
        if (!Gamepad.gamepad.isConnected())
        {
            if (!this.getCollision().forwardBlockCheck(0.f, -1.f * this.getCaracteristics().getSpeed()) && this.forward) this.addY(-1.f);
            else if (!this.getCollision().forwardBlockCheck(0.f, 1.f * this.getCaracteristics().getSpeed()) && this.backward) this.addY(1.f);
            if (!this.getCollision().forwardBlockCheck(-1.f * this.getCaracteristics().getSpeed(), 0.f) && this.left) this.addX(-1.f);
            else if (!this.getCollision().forwardBlockCheck(1.f * this.getCaracteristics().getSpeed(), 0.f) && this.right) this.addX(1.f);
        }
    }

    @Override
    public void takeDamage(int damage)
    {
        super.takeDamage(damage);
        equipmentEffect("onHit");
    }

    /**
     * Callback appelé à la fin d’une animation.
     * Réinitialise l’état d’action du joueur.
     */
    @Override
    public void onAnimationEnd()
    {
        this.setbAction(false);
    }

    @Override
    public void onDeath()
    {
        UI.ActualState = UI.EGameState.GameOver;
        SoundPlayer.stopMusic();
        SoundPlayer.playSFX("DeathCry.wav");
        SoundPlayer.playSFX("GameOver.wav");
        Player1 = null;
        Window.window.getPanel().removeKeyListener(this);
        Window.window.getPanel().removeMouseMotionListener(this);
        Window.window.getPanel().removeMouseListener(this);
    }

    /**
     * Applique les effets d’équipement associés à un événement donné.
     *
     * @param event nom de l’événement (par exemple {@code "roomCleared"})
     */
    public void equipmentEffect(String event)
    {
        switch (event)
        {
            case "roomCleared" -> { for (Equipment e : this.equipments) e.onRoomCleared(); }
            case "onEnnemyKilled" -> { for (Equipment e : this.equipments) e.onEnemyKilled(); }
            case "onEnnemyHit" -> { for (Equipment e : this.equipments) e.onEnemyHit(); }
            case "onHit" -> { for (Equipment e : this.equipments) e.onHit(); }
            case "onDash" -> { for (Equipment e : this.equipments) e.onDash(); }
            default -> { }
        }
    }

    public void equip(Equipment e)
    {
        this.equipments.add(e);
        this.getCaracteristics().add(e.getBonus());
        if (e.getName().equals("Anneau du Paon")) SoundPlayer.techMode();
    }

    public static void setPlayer1(Player Player1) {
        Player.Player1 = Player1;
    }

    public List<Equipment> getEquipments() {
        return this.equipments;
    }

    public V2D getAim() {
        return aim;
    }

    public void setAim(V2D aim) {
        this.aim = aim;
    }
}
