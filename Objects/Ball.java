package Objects;

import Level.Level;
import Utilitary.V2D;

/**
 * Représente un projectile de type bille de fronde.
 * Cette classe initialise une bille légère et rapide, lancée avec une impulsion
 * déterminée par la vitesse fournie, et destinée à infliger des dégâts sur une courte durée.
 */
public class Ball extends Projectile
{
    /**
     * Crée une bille de fronde lancée par un acteur du jeu.
     * La position initiale est décalée dans la direction du tir afin d’éviter toute
     * superposition avec le lanceur. La vitesse est amplifiée pour simuler l’effet
     * d’une fronde projetant une petite bille métallique ou minérale.
     *
     * @param position   position d’origine avant décalage
     * @param damage     dégâts infligés par la bille
     * @param owner      entité ayant lancé le projectile
     * @param lifespan   durée de vie du projectile en nombre de frames
     * @param velocity   direction et vitesse du lancer
     */
    public Ball(V2D position, int damage, CObject owner, int lifespan, V2D velocity)
    {
        super(  new V2D(0.75, 0.75),
                new V2D(position.x + (velocity.x * 2.f), position.y + (velocity.y * 2.f)),
                new V2D(12, 12), 
                damage, lifespan, owner, 0, 
                new V2D(velocity.x * 2.f, velocity.y * 2.f), 
                "Effects");
        this.getSprite().setAnimName("ball");
    }

    @Override
    public void update(float dt)
    {
        if (this.getOwner() == null || (this.getOwner() instanceof Character c && c.getCaracteristics().getActHP() == 0))
        {
            Level.getActualRoom().removeObject(this);
            return;
        }
        super.update(dt);
    }
}
