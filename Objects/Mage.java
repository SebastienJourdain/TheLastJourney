package Objects;

import java.awt.event.MouseEvent;

import Level.Level;
import Sound.SoundPlayer;
import UI.UI;
import Utilitary.V2D;

/**
 * Représente un personnage joueur de type mage.
 * Le mage dispose de deux attaques magiques : une boule de feu rapide
 * et une explosion de zone plus puissante mais plus lente.
 */
public class Mage extends Player
{
    /**
     * Crée un mage avec sa taille, sa position et sa zone de collision.
     * Ajuste la zone de collision pour correspondre au gabarit visuel du personnage.
     *
     * @param size          taille visuelle du mage
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     */
    public Mage(V2D size, V2D position, V2D collisionSize)
    {
        super(size, position, collisionSize, "Mage");
        this.getCollision().setPosition(new V2D(position.x - 20.0, position.y + 15.0));
        this.getCollision().setSize(new V2D(30.0, 30.0));
        this.setCaracteristics(new Caracteristics(3, 0, 2, 1.5f, 0.1f));
    }

    /**
     * Lance une boule de feu (Fireball) dans la direction actuelle du mage.
     * La puissance est modérée, mais le sort est rapide et réactif.
     * Choisit automatiquement l’animation de lancement appropriée.
     * Marque le personnage comme étant en action.
     */
    private void fireBall()
    {
        double x = this.getAim().x;
        double y = this.getAim().y;

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

        Fireball damage = new Fireball(new V2D(this.getPosition().x, this.getPosition().y), dmg, this, 4, new V2D(this.getAim().x * 5.f, this.getAim().y * 5.f));
        Level.getActualRoom().addObject(damage);

        if (Math.abs(y) > Math.abs(x))
        {
            if (y < 0) this.getSprite().setAnimName("fast_cast_top");
            else if (y > 0) this.getSprite().setAnimName("fast_cast_bot");
        }
        else
        {
            if (x < 0) this.getSprite().setAnimName("fast_cast_left");
            else if (x > 0) this.getSprite().setAnimName("fast_cast_right");
        }
        this.setbAction(true);
    }

    /**
     * Lance un sort d’explosion de zone (Fire Blast) autour du mage.
     * Le sort inflige un fort dégât en zone et possède une animation lente et puissante.
     * L’attaque crée une instance de {@code ZoneDamage} représentant
     * l’explosion magique.
     * Marque le personnage comme étant en action.
     */
    private void fireBlast()
    {
        double x = this.getOrientation().x;
        double y = this.getOrientation().y;

        float seuil = 0.1f;

        if (Math.abs(x) < seuil && Math.abs(y) < seuil) return;

        int dmg = (int)((float)this.getCaracteristics().getDamage() * 1.5f);
        boolean crit = this.random.nextFloat(0.f, 1.f) <= this.getCaracteristics().getCritical();
        if (crit)
        {
            Effect effect = new Effect(new V2D(3,3), new V2D(this.getPosition().x - 40, this.getPosition().y - 64), new V2D(-1,-1), "crit");
            Level.getActualRoom().addObject(effect);
            dmg *= 2;
        }

        ZoneDamage damage = new ZoneDamage(new V2D(6.0, 6.0), this.getPosition(), new V2D(32, 32), dmg, 18, 12, this, "big_explosion", 110.f, true);
        Level.getActualRoom().addObject(damage);

        if (Math.abs(y) > Math.abs(x))
        {
            if (y < 0) this.getSprite().setAnimName("slow_cast_top");
            else if (y > 0) this.getSprite().setAnimName("slow_cast_bot");
        }
        else
        {
            if (x < 0) this.getSprite().setAnimName("slow_cast_left");
            else if (x > 0) this.getSprite().setAnimName("slow_cast_right");
        }
        this.setbAction(true);
    }

    /**
     * Gère l’appui de la souris pour déclencher les sorts du mage.
     * Clic gauche : {@link #fireBall()}.
     * Clic droit : {@link #fireBlast()}.
     *
     * @param e événement souris
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
        if (this.isbAction() || UI.ActualState != UI.EGameState.InGame) return;

        //Clic gauche
        if (e.getButton() == 1) fireBall();
        //Clic droit
        else if (e.getButton() == 3) fireBlast();
    }

     @Override 
    public void takeDamage(int Damage)
    {
        super.takeDamage(Damage);
        SoundPlayer.playSFX("OofMale.wav");
    }

    /**
     * Action associée au bouton 0 de la manette : lance une boule de feu.
     */
    @Override
    public void gamepadButtonRT() { fireBall(); }

    /**
     * Action associée au bouton 1 de la manette : lance une explosion de zone.
     */
    @Override
    public void gamepadButtonLT() { fireBlast(); }
}
