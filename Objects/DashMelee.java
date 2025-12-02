package Objects;

import Level.Level;
import Utilitary.V2D;

/**
 * Représente une attaque de mêlée : une zone de dégâts brève et généralement
 * synchronisée avec une animation d’arme (coup d’épée, griffe, etc.).
 *  
 * Une attaque de mêlée :
 * <ul>
 *   <li>possède une durée de vie très courte ;</li>
 *   <li>devient active uniquement à partir d’un certain frame
 *       ({@link Damage#getActivationFrame()}) ;</li>
 *   <li>inflige immédiatement des dégâts aux {@link Character} touchés ;</li>
 *   <li>se détruit automatiquement lorsque sa durée de vie atteint zéro.</li>
 * </ul>
 */
public class DashMelee extends Damage
{
    private final V2D dashDirection;
    /**
     * Crée une hitbox d’attaque de mêlée.
     *
     * @param size            taille visuelle de l’impact
     * @param position        position initiale
     * @param collisionSize   dimensions de la zone de collision
     * @param damage          dégâts infligés
     * @param lifespan        durée de vie totale
     * @param owner           créateur de l’attaque
     * @param activationFrame moment où l’attaque devient active
     */
    public DashMelee(V2D size, V2D position, V2D collisionSize, int damage, int lifespan, CObject owner, int activationFrame, V2D dashDirection)
    {
        super(size, position, collisionSize, damage, lifespan, activationFrame, owner, "");
        this.dashDirection = dashDirection;
    }

    /**
     * Déclenché lorsqu’un objet entre en collision avec la zone de mêlée.
     *  
     * <ul>
     *   <li>Ignore l’attaquant lui-même ;</li>
     *   <li>Inflige des dégâts à toute cible de type {@link Character}.</li>
     * </ul>
     */
    @Override
    public void onCollisionBegin(CObject o)
    {
        if (o == this.getOwner()) {}
        else if (!this.getAlreadyHitted().contains(o))
        {
            if (o instanceof Character c) c.takeDamage(this.getDamage());
            this.getAlreadyHitted().add(o);
        }
    }

    /**
     * Ne fait rien de particulier lorsque la collision se termine.
     *  
     * La logique de dégâts se limite au moment d’entrée dans la hitbox.
     */
    @Override
    public void onCollisionEnd(CObject o)
    {
        if (o == this.getOwner()) {}
    }

    /**
     * Met à jour le cycle de vie de l’attaque de mêlée.
     *  
     * <ul>
     *   <li>Accumule le temps jusqu’à atteindre le délai entre deux “frames”.</li>
     *   <li>Diminue la durée de vie ({@link Damage#decLifespan()}).</li>
     *   <li>Active la hitbox lorsque {@code lifespan == activationFrame}.</li>
     *   <li>Détruit l’objet et sa collision lorsque la durée de vie atteint zéro.</li>
     * </ul>
     *
     * @param dt temps écoulé depuis la dernière mise à jour
     */
    @Override
    public void update(float dt)
    {
        this.setTime(this.getTime() + dt);

        if (this.getTime() > (1.f / (10.f * this.getAnimSpeed())))
        {
            this.decLifespan();

            if (this.getLifespan() <= this.getActivationFrame() && !this.getCollision().isbActive())
                {
                    if (this.getOwner() instanceof Boss b) b.attackDash(this.dashDirection);
                    this.getCollision().addX(this.dashDirection.x);
                    this.getCollision().addY(this.dashDirection.y);
                    this.getCollision().setbActive(true);
                }

            this.setTime(0.f);

            if (this.getLifespan() == 0)
            {
                Level.getActualRoom().getCollisionsToRemove().add(this.getCollision());
                Level.getActualRoom().removeObject(this);
            }
        }
    }
}
