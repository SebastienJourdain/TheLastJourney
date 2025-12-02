package Objects;

import Utilitary.V2D;

/**
 * Représente un projectile de type flèche.
 * La flèche est initialisée avec une position ajustée, une vitesse,
 * un propriétaire, des dégâts et une durée de vie.
 */
public class Arrow extends Projectile
{
    /**
     * Crée une flèche tirée par un acteur du jeu.
     * La position initiale est décalée dans la direction de tir
     * afin d’éviter que le projectile n’apparaisse directement dans l’acteur.
     * La vitesse est doublée avant d’être transmise au projectile.
     *
     * @param position   position de base avant ajustement
     * @param damage     quantité de dégâts infligés par la flèche
     * @param owner      entité ayant tiré la flèche
     * @param lifespan   durée de vie en nombre de frames
     * @param velocity   direction et vitesse initiale de la flèche
     */
    public Arrow(V2D position, int damage, CObject owner, int lifespan, V2D velocity)
    {
        super(  new V2D(0.75, 0.75),
                new V2D(position.x + (velocity.x * 2.f), position.y + (velocity.y * 2.f)),
                new V2D(25, 25), 
                damage, lifespan, owner, 0, 
                new V2D(velocity.x * 2.f, velocity.y * 2.f), 
                "Effects");
        this.getSprite().setAnimName("arrow");
    }

    @Override
    public void update(float dt)
    {
        if (this.getOwner() instanceof Archer c && !this.getCollision().isbActive())
        {
            V2D movement = new V2D(this.getPosition());
            if (c.getSprite().getAnimName().startsWith("fast"))
            {
                this.getVelocity().x = c.getAim().x * 20.f;
                this.getVelocity().y = c.getAim().y * 20.f;
            }
            this.setPosition(new V2D(c.getPosition().x + (this.getVelocity().x * 2.f), c.getPosition().y + (this.getVelocity().y * 2.f)));
            movement.x = this.getPosition().x - movement.x;
            movement.y = this.getPosition().y - movement.y;
            this.getCollision().addX(movement.x);
            this.getCollision().addY(movement.y);
        }
        super.update(dt);
    }
}
