package Objects;

import java.awt.event.MouseEvent;

import Level.Level;
import Sound.SoundPlayer;
import UI.UI;
import Utilitary.V2D;

/**
 * Représente un personnage joueur de type Archer.
 * Cet acteur est capable d’effectuer des tirs simples ou multiples,
 * selon les interactions de la souris ou de la manette.
 */
public class Archer extends Player
{
    /**
     * Crée un Archer avec sa taille, sa position et sa zone de collision.
     * Initialise également la collision en ajustant sa position et sa dimension.
     *
     * @param size          dimensions du personnage
     * @param position      position initiale du personnage
     * @param collisionSize taille de la zone de collision
     */
    public Archer(V2D size, V2D position, V2D collisionSize)
    {
        super(size, position, collisionSize, "Archer");
        this.getCollision().setPosition(new V2D(position.x - 16.0, position.y + 5.0));
        this.getCollision().setSize(new V2D(30.0, 30.0));
        this.setCaracteristics(new Caracteristics(4, 0, 1, 2.f, 0.15f));
    }

    /**
     * Effectue un tir simple dans la direction actuelle du personnage.
     * Le tir est annulé si l’orientation est trop faible.
     * Le type d’animation est choisi selon l’axe dominant de l’orientation.
     */
    private void singleShoot()
    {
        double x = this.getAim().x;
        double y = this.getAim().y;

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

        Arrow damage = new Arrow(new V2D(this.getPosition().x, this.getPosition().y), dmg, this, 4, new V2D(this.getAim().x * 10.f, this.getAim().y * 10.f));
        Level.getActualRoom().addObject(damage);

        if (Math.abs(y) > Math.abs(x))
        {
            if (y < 0) this.getSprite().setAnimName("fast_shoot_top");
            else if (y > 0) this.getSprite().setAnimName("fast_shoot_bot");
        }
        else
        {
            if (x < 0) this.getSprite().setAnimName("fast_shoot_left");
            else if (x > 0) this.getSprite().setAnimName("fast_shoot_right");
        }
        this.setbAction(true);
    }

    /**
     * Effectue un tir multiple en éventail,
     * générant plusieurs flèches réparties sur différents angles.
     * L’animation dépend de l’axe dominant de l’orientation.
     */
    private void multipleShoot()
    {
        double x = this.getAim().x;
        double y = this.getAim().y;

        float seuil = 0.1f;

        if (Math.abs(x) < seuil && Math.abs(y) < seuil) return;

        int dmg = this.getCaracteristics().getDamage() / 2;
        dmg = dmg == 0 ? 1 : dmg;
        boolean crit = this.random.nextFloat(0.f, 1.f) <= this.getCaracteristics().getCritical();
        if (crit)
        {
            Effect effect = new Effect(new V2D(3,3), new V2D(this.getPosition().x - 40, this.getPosition().y - 64), new V2D(-1,-1), "crit");
            Level.getActualRoom().addObject(effect);
            dmg *= 2;
        }

        // Définition du cône : quatre projectiles répartis sur 90°
        double[] angles = { -45.0, -30.0, -15.0, 0.0, 15.0, 30.0, 45.0 };

        for (double a : angles)
        {
            // Conversion en radians
            double rad = Math.toRadians(a);

            // Rotation du vecteur d’orientation (rotation 2D classique)
            double rx = x * Math.cos(rad) - y * Math.sin(rad);
            double ry = x * Math.sin(rad) + y * Math.cos(rad);

            // Création du projectile avec la direction modifiée
            Arrow damage = new Arrow(new V2D(this.getPosition().x, this.getPosition().y), dmg, this, 7, new V2D(rx * 10.f, ry * 10.f));

            Level.getActualRoom().addObject(damage);
        }

        if (Math.abs(y) > Math.abs(x))
        {
            if (y < 0) this.getSprite().setAnimName("slow_shoot_top");
            else if (y > 0) this.getSprite().setAnimName("slow_shoot_bot");
        }
        else
        {
            if (x < 0) this.getSprite().setAnimName("slow_shoot_left");
            else if (x > 0) this.getSprite().setAnimName("slow_shoot_right");
        }
        this.setbAction(true);
    }

    /**
     * Gestion du clic souris pour déclencher un tir simple ou multiple.
     *
     * @param e événement souris détecté
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
        if (this.isbAction() || UI.ActualState != UI.EGameState.InGame) return;

        //Clic gauche
        if (e.getButton() == 1) singleShoot();
        //Clic droit
        else if (e.getButton() == 3) multipleShoot();
    }

    @Override
    public void onAnimationEnd()
    {
        super.onAnimationEnd();
        if (this.getSprite().getAnimName().startsWith("fast"))SoundPlayer.playSFX("SimpleArcherAttack.wav");
        else if (this.getSprite().getAnimName().startsWith("slow"))SoundPlayer.playSFX("ShotgunArcher.wav");
    }

    @Override 
    public void takeDamage(int Damage)
    {
        super.takeDamage(Damage);
        SoundPlayer.playSFX("OofArcher.wav");
    }

    /**
     * Action associée au bouton 0 de la manette : tir simple.
     */
    @Override
    public void gamepadButtonRT() { singleShoot(); }

    /**
     * Action associée au bouton 1 de la manette : tir multiple.
     */
    @Override
    public void gamepadButtonLT() { multipleShoot(); }
}
