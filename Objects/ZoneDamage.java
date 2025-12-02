package Objects;

import Level.Level;
import Sound.SoundPlayer;
import Utilitary.V2D;

/**
 * Représente une zone de dégâts, généralement circulaire, utilisée pour des explosions,
 * ondes de choc, auras ou zones persistant un court instant.
 *  
 * Une ZoneDamage peut fonctionner selon deux modes :
 * <ul>
 *   <li><b>activation immédiate</b> : la collision est active dès la création ;</li>
 *   <li><b>activation retardée (timer)</b> : la hitbox ne devient active qu’à partir
 *       d’un certain frame ({@link Damage#getActivationFrame()}).</li>
 * </ul>
 *
 * Cette classe permet de gérer des dégâts de zone simples, ou des explosions
 * synchronisées à une animation (via le paramètre {@code bTimer}).
 */
public class ZoneDamage extends Damage
{
    private boolean Sound = false;
    /** Indique si l’activation de la zone doit être retardée. */
    private boolean bTimer = false;

    /**
     * Crée une zone de dégâts active immédiatement.
     *
     * @param size            taille visuelle de l’effet
     * @param position        position de la zone
     * @param collisionSize   dimensions de la hitbox (inutile si collision circulaire)
     * @param damage          dégâts infligés
     * @param lifespan        durée de vie totale
     * @param activationFrame frame d’activation (non utilisé ici, car active dès le début)
     * @param owner           créateur de la zone
     * @param spriteName      animation visuelle à utiliser
     * @param r               rayon de la collision circulaire
     */
    public ZoneDamage(V2D size, V2D position, V2D collisionSize, int damage, int lifespan, int activationFrame, CObject owner, String spriteName, float r)
    {
        super(size, position, collisionSize, damage, lifespan, activationFrame, owner, "Effects");
        this.getSprite().setAnimName(spriteName);
        this.getCollision().setbActive(true);
        this.getCollision().setbCircle(true);
        this.getCollision().setR(r);
    }

    /**
     * Crée une zone de dégâts dont l’activation peut être retardée.
     *
     * @param size            taille visuelle
     * @param position        position
     * @param collisionSize   dimensions de la hitbox
     * @param damage          dégâts infligés
     * @param lifespan        durée de vie totale
     * @param activationFrame frame à partir de laquelle la hitbox peut s’activer
     * @param owner           créateur
     * @param spriteName      animation visuelle
     * @param r               rayon de la collision circulaire
     * @param bTimer          indique si l’activation doit dépendre du timer interne
     */
    public ZoneDamage(V2D size, V2D position, V2D collisionSize, int damage, int lifespan, int activationFrame, CObject owner, String spriteName, float r, boolean bTimer)
    {
        super(size, position, collisionSize, damage, lifespan, activationFrame, owner, "Effects");
        this.getSprite().setAnimName(spriteName);
        this.bTimer = bTimer;
        this.getCollision().setbActive(false);
        this.getCollision().setbCircle(true);
        this.getCollision().setR(r);
        if (spriteName.equals("thunder"))
        {
            this.getCollision().setPosition(new V2D(position.x, position.y + 60.0));
        }
        else if (spriteName.equals("blood_spell"))
        {
            this.getCollision().setPosition(new V2D(position.x, position.y + 30.0));
        }
    }

    /**
     * Gère les collisions entrantes avec la zone de dégâts.
     *  
     * Si la zone est active :
     * <ul>
     *   <li>Ignore son propriétaire ;</li>
     *   <li>Inflige des dégâts aux {@link Character} touchés.</li>
     * </ul>
     */
    @Override
    public void onCollisionBegin(CObject o)
    {
        if (o == this.getOwner()) {}
        else if (!this.getAlreadyHitted().contains(o))
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
    }

    /**
     * Ne réalise aucune action lorsqu’un objet quitte la zone de dégâts.
     */
    @Override
    public void onCollisionEnd(CObject o)
    {
        if (o == this.getOwner()) {}
    }

    /**
     * Met à jour la zone de dégâts.
     *  
     * Fonctionnement :
     * <ul>
     *   <li>Met à jour l’animation si elle existe ;</li>
     *   <li>Découpe le temps en “frames” virtuelles pour gérer lifespan ;</li>
     *   <li>Active la hitbox au moment approprié si {@code bTimer = true} ;</li>
     *   <li>Supprime la collision si nécessaire ;</li>
     *   <li>Détruit l’objet lorsque son lifespan atteint zéro.</li>
     * </ul>
     *
     * @param dt temps écoulé depuis la dernière update
     */
    @Override
    public void update(float dt)
    {
        if (!"".equals(this.getSprite().getAnimName())) this.getSprite().updateAnim(dt);
        this.setTime(this.getTime() + dt);

        if (this.getTime() > (1.f / (10.f * this.getAnimSpeed())))
        {
            this.decLifespan();
            this.setTime(0.f);

            if (this.getLifespan() <= this.getActivationFrame() && this.bTimer)
            {
                this.getCollision().setbActive(true);
                if (this.getSprite().getAnimName().equals("big_explosion") && !Sound)
                {
                    SoundPlayer.playSFX("MageExplosion.wav");
                    Sound = true;
                }
            }
            if (this.getLifespan() <= this.getActivationFrame() - 1 && this.bTimer)
            {
                Level.getActualRoom().getCollisionsToRemove().add(this.getCollision());
            }
            if (this.getLifespan() == 0)
            {
                Level.getActualRoom().getCollisionsToRemove().add(this.getCollision());
                Level.getActualRoom().removeObject(this);
            }
        }
    }
}
