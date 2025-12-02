package Objects;

import java.awt.event.MouseEvent;

import Graphism.Animation;
import Level.Level;
import Sound.SoundPlayer;
import UI.UI;
import Utilitary.V2D;

/**
 * Représente un personnage joueur de type guerrier.
 * Le guerrier dispose de deux attaques principales de corps à corps :
 * une attaque de taille (slash) et une attaque d’estoc (thrust),
 * déclenchées à la souris ou à la manette.
 */
public class Warrior extends Player
{
    private Damage runningAttack = null;
    /**
     * Crée un guerrier avec sa taille, sa position et sa zone de collision.
     * La zone de collision est ensuite ajustée pour correspondre au gabarit visuel du guerrier.
     *
     * @param size          taille visuelle du guerrier
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     */
    public Warrior(V2D size, V2D position, V2D collisionSize)
    {
        super(size, position, collisionSize, "Warrior");
        this.getCollision().setPosition(new V2D(position.x - 18.0, position.y + 15.0));
        this.getCollision().setSize(new V2D(34.0, 30.0));
        this.setCaracteristics(new Caracteristics(3, 2, 1, 1.5f, 0.05f));
    }

    @Override
    public void addX(double x)
    {
        super.addX(x);
        if (this.runningAttack != null) this.runningAttack.addX(x * this.getCaracteristics().getSpeed());
    }
    @Override
    public void addY(double y)
    {
        super.addY(y);
        if (this.runningAttack != null) this.runningAttack.addY(y * this.getCaracteristics().getSpeed());
    }

    /**
     * Effectue une attaque de taille (slash) dans la direction actuelle du guerrier.
     * Crée un objet {@link Melee} positionné et dimensionné en fonction de l’orientation,
     * synchronise la vitesse d’animation avec l’animation correspondante, puis
     * déclenche l’animation de slash sur le sprite du guerrier.
     * L’action du personnage est marquée comme en cours.
     */
    private void slash()
    {
        double x = this.getOrientation().x;
        double y = this.getOrientation().y;

        float seuil = 0.1f;

        if (Math.abs(x) < seuil && Math.abs(y) < seuil) return;

        int dmg = this.getCaracteristics().getDamage();
        boolean crit = this.random.nextFloat(0.f, 1.f) <= this.getCaracteristics().getCritical();
        if (crit)
        {
            Effect effect = new Effect(new V2D(3,3), new V2D(this.getPosition().x - 40, this.getPosition().y - 64), new V2D(-1,-1), "crit");
            Level.getActualRoom().addObject(effect);
            dmg *= 2;
        }

        V2D pos = new V2D(-1,-1);
        V2D size = new V2D(-1,-1);

        if (Math.abs(y) > Math.abs(x))
        {
            if (y < 0)
            {
                pos = new V2D(this.getPosition().x - 50, this.getPosition().y - 50);
                size = new V2D(120, 50);
                this.getSprite().setAnimName("slash_top");
            }
            else if (y > 0)
            {
                pos = new V2D(this.getPosition().x - 50, this.getPosition().y + 40);
                size = new V2D(120, 50);
                this.getSprite().setAnimName("slash_bot");
            }
        }
        else
        {
            if (x < 0)
            {
                pos = new V2D(this.getPosition().x - 75, this.getPosition().y - 50);
                size = new V2D(50, 120);
                this.getSprite().setAnimName("slash_left");
            }
            else if (x > 0)
            {
                pos = new V2D(this.getPosition().x + 25, this.getPosition().y - 50);
                size = new V2D(50, 120);
                this.getSprite().setAnimName("slash_right");
            }
        }

        Melee damage = new Melee(new V2D(1, 1), pos, size, dmg, 4, this, 3);
        Animation A = Animation.get(this.getSprite().getAtlasName(), "slash_top");
        damage.setAnimSpeed(A.getAnimationSpeed());
        Level.getActualRoom().addObject(damage);
        this.setbAction(true);
        this.runningAttack = damage;
        SoundPlayer.playSFX("WarriorAttack.wav");
    }

    /**
     * Effectue une attaque d’estoc (thrust) dans la direction actuelle du guerrier.
     * Crée un objet {@link Melee} plus concentré et plus puissant,
     * positionné selon l’orientation, synchronise sa vitesse d’animation
     * avec l’animation correspondante puis lance l’animation de thrust
     * sur le sprite du guerrier. L’action du personnage est marquée comme en cours.
     */
    private void thrust()
    {
        double x = this.getOrientation().x;
        double y = this.getOrientation().y;

        float seuil = 0.1f;

        if (Math.abs(x) < seuil && Math.abs(y) < seuil) return;

        int dmg = this.getCaracteristics().getDamage();
        boolean crit = this.random.nextFloat(0.f, 1.f) <= this.getCaracteristics().getCritical() + 0.1f;
        if (crit)
        {
            Effect effect = new Effect(new V2D(3,3), new V2D(this.getPosition().x - 40, this.getPosition().y - 64), new V2D(-1,-1), "crit");
            Level.getActualRoom().addObject(effect);
            dmg *= 2;
        }

        if (Math.abs(y) > Math.abs(x))
        {
            if (y < 0)
            {
                Melee damage = new Melee(new V2D(1, 1), new V2D(this.getPosition().x, this.getPosition().y - 60), new V2D(20, 50), dmg, 6, this, 4);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "thrust_top");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.runningAttack = damage;
                this.getSprite().setAnimName("thrust_top");
            }
            else if (y > 0)
            {
                Melee damage = new Melee(new V2D(1, 1), new V2D(this.getPosition().x - 10, this.getPosition().y + 40), new V2D(20, 50), dmg, 6, this, 4);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "thrust_bot");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.runningAttack = damage;
                this.getSprite().setAnimName("thrust_bot");
            }
        }
        else
        {
            if (x < 0)
            {
                Melee damage = new Melee(new V2D(1, 1), new V2D(this.getPosition().x - 90, this.getPosition().y + 5), new V2D(60, 20), dmg, 6, this, 4);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "thrust_left");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.runningAttack = damage;
                this.getSprite().setAnimName("thrust_left");
            }
            else if (x > 0)
            {
                Melee damage = new Melee(new V2D(1, 1), new V2D(this.getPosition().x + 30, this.getPosition().y + 5), new V2D(60, 20), dmg, 6, this, 4);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "thrust_right");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.runningAttack = damage;
                this.getSprite().setAnimName("thrust_right");
            }
        }
        this.setbAction(true);
        SoundPlayer.playSFX("Thrust.wav");

    }

    /**
     * Gère les clics de souris pour déclencher les attaques du guerrier.
     * Clic gauche : {@link #slash()}.
     * Clic droit : {@link #thrust()}.
     *
     * @param e événement souris
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
        if (this.isbAction() || UI.ActualState != UI.EGameState.InGame) return;

        //Clic gauche
        if (e.getButton() == 1) slash();
        //Clic droit
        else if (e.getButton() == 3) thrust();
    }

    @Override 
    public void takeDamage(int Damage)
    {
        super.takeDamage(Damage);
        SoundPlayer.playSFX("OofMale.wav");
    }

    /**
     * Action associée au bouton 0 de la manette : attaque de taille.
     */
    @Override
    public void gamepadButtonRT() { slash(); }

    /**
     * Action associée au bouton 1 de la manette : attaque d’estoc.
     */
    @Override
    public void gamepadButtonLT() { thrust(); }

    public void setRunningAttack(Damage runningAttack) {
        this.runningAttack = runningAttack;
    }
}
