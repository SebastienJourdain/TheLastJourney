package Objects;

import Level.Level;
import Utilitary.V2D;

/**
 * Représente un projectile se déplaçant dans l’espace de jeu.
 *  
 * Cette classe couvre tous les projectiles génériques : flèches, boules d’énergie,
 * projectiles magiques, etc.  
 *  
 * Un projectile :
 * <ul>
 *   <li>hérite du système de dégâts de {@link Damage} ;</li>
 *   <li>possède une vitesse (vecteur velocity) appliquée à chaque mise à jour ;</li>
 *   <li>ne devient actif qu’à partir d’un certain frame
 *       ({@link Damage#getActivationFrame()}) ;</li>
 *   <li>se détruit lorsqu’il touche une cible, sort de l’écran ou expire.</li>
 * </ul>
 */
public class Projectile extends Damage
{
    /** Vecteur vitesse appliqué au projectile à chaque tick. */
    private final V2D velocity;

    /**
     * Construit un projectile générique.
     *
     * @param size            taille visuelle
     * @param position        position initiale
     * @param collisionSize   dimensions de la hitbox
     * @param damage          dégâts infligés en cas d’impact
     * @param lifespan        durée de vie totale
     * @param owner           créateur du projectile
     * @param activationFrame moment à partir duquel la hitbox devient active
     * @param velocity        vecteur de déplacement appliqué à chaque update
     * @param spriteName      nom du sprite associé
     */
    public Projectile(V2D size, V2D position, V2D collisionSize, int damage, int lifespan, CObject owner, int activationFrame, V2D velocity, String spriteName)
    {
        super(size, position, collisionSize, damage, lifespan, activationFrame, owner, spriteName);
        this.velocity = velocity;
    }

    /**
     * Déclenché lorsque le projectile touche un objet.
     *  
     * <ul>
     *   <li>Ignore le propriétaire ;</li>
     *   <li>Ignore les autres objets {@link Damage} ;</li>
     *   <li>Inflige des dégâts aux {@link Character} ;</li>
     *   <li>Se détruit ensuite en retirant sa collision et lui-même de la salle.</li>
     * </ul>
     */
    @Override
    public void onCollisionBegin(CObject o)
    {
        if (o == this.getOwner() || o instanceof Damage) {}
        else
        {
            if (!this.getAlreadyHitted().contains(o))
            {
                this.getAlreadyHitted().add(o);
                if (o instanceof Character c)
                {
                    c.takeDamage(this.getDamage());
                    if (this.getOwner() instanceof Player p)
                    {
                        if (!this.isEnnemyHitted())
                        {
                            this.setEnnemyHitted(true);
                            p.equipmentEffect("onEnnemyHit");
                        }
                        if (c.getCaracteristics().getActHP() == 0) p.equipmentEffect("onEnnemyKilled");
                    }
                }
            }
            Level.getActualRoom().getCollisionsToRemove().add(this.getCollision());
            Level.getActualRoom().removeObject(this);
        }
    }

    /**
     * Ne réalise aucune action particulière lors de la fin de collision.
     */
    @Override
    public void onCollisionEnd(CObject o)
    {
        if (o == this.getOwner()) {}
    }

    /**
     * Met à jour le projectile :
     * <ul>
     *   <li>déplace le projectile si sa hitbox est active ;</li>
     *   <li>active la hitbox lorsque {@code lifespan <= activationFrame} ;</li>
     *   <li>décrémente le lifespan à intervalles réguliers ;</li>
     *   <li>supprime le projectile lorsqu’il sort de l’écran ou expire.</li>
     * </ul>
     *
     * @param dt temps écoulé depuis la dernière mise à jour
     */
    @Override
    public void update(float dt)
    {
        super.update(dt);

        if (this.getLifespan() <= this.getActivationFrame())
        {
            this.addX(velocity.x);
            this.addY(velocity.y);
        }
        this.setTime(this.getTime() + dt);
        if (this.getTime() > (1.f / 10.f))
        {
            this.decLifespan();
            if (this.getLifespan() <= this.getActivationFrame())
            {
                this.getCollision().setbActive(true);
                this.addX(velocity.x);
                this.addY(velocity.y);
            }
            this.setTime(0.f);
        }
        if (this.getPosition().x < 0 || this.getPosition().y < 0 || this.getPosition().x > 1920 || this.getPosition().y > 1080)
        {
            Level.getActualRoom().getCollisionsToRemove().add(this.getCollision());
            Level.getActualRoom().removeObject(this);
        }
    }

    /**
     * @return la vitesse du projectile.
     */
    public V2D getVelocity() {
        return this.velocity;
    }
}
