package Objects;

import java.util.HashSet;

import Utilitary.V2D;

/**
 * Représente un objet générant des dégâts temporaires dans le monde de jeu.
 *  
 * Cette classe abstraite sert de base aux différents types de dégâts :
 * projectiles, attaques de mêlée, zones d’impact, explosions, etc.
 *  
 * Un objet {@code Damage} possède :
 * <ul>
 *   <li>une quantité de dégâts infligés ;</li>
 *   <li>une durée de vie (lifespan) décrémentée à chaque mise à jour ;</li>
 *   <li>un propriétaire ({@link CObject}) responsable du coup ;</li>
 *   <li>un délai d’activation (activationFrame) pour synchroniser l’impact
 *       avec une animation ;</li>
 *   <li>une vitesse d’animation propre ;</li>
 *   <li>une collision initialement désactivée (hitbox inactive).</li>
 * </ul>
 *
 * Les classes filles doivent gérer :
 * <ul>
 *   <li>la mise à jour,</li>
 *   <li>l’activation de la hitbox,</li>
 *   <li>et l’application des dégâts aux cibles.</li>
 * </ul>
 */
public abstract class Damage extends Actor
{
    /** Quantité de dégâts infligés par cet objet. */
    private int damage = 0;

    /** Nombre d’images restant avant la disparition du dégât. */
    private int lifespan = 0;

    /** Détermine à quel moment (image) la hitbox devient active. */
    private int activationFrame = 0;

    /** Objet ayant créé ce dégât (pour éviter l’auto-dégât). */
    private CObject owner = null;

    /** Temps écoulé depuis la création du dégât. */
    private float time = 0.f;

    /** Vitesse d’animation propre à ce dégât. */
    private float animSpeed = 1.f;

    private boolean ennemyHitted = false;

    private final HashSet<CObject> alreadyHitted = new HashSet<>();

    /**
     * Construit un objet de dégât générique.
     *
     * @param size            taille visuelle
     * @param position        position de l’objet
     * @param collisionSize   taille de la zone de collision
     * @param damage          quantité de dégâts
     * @param lifespan        durée de vie (en frames ou ticks)
     * @param activationFrame moment où la hitbox devient active
     * @param owner           créateur du dégât
     * @param spriteName      nom du sprite associé
     */
    public Damage(V2D size, V2D position, V2D collisionSize, int damage, int lifespan, int activationFrame, CObject owner, String spriteName)
    {
        super(size, position, collisionSize, spriteName);
        this.damage = damage;
        this.lifespan = lifespan;
        this.activationFrame = activationFrame;
        this.owner = owner;
        this.getCollision().setbActive(false);
    }

    /** @return la quantité de dégâts infligée. */
    public int getDamage() { return this.damage; }

    /** @return la durée de vie restante. */
    public int getLifespan() { return this.lifespan; }

    /** Décrémente la durée de vie. */
    public void decLifespan() { this.lifespan--; }

    /** @return l’objet ayant généré ce dégât. */
    public CObject getOwner() { return this.owner; }

    /** @return le temps écoulé depuis la création. */
    public float getTime() { return this.time; }

    /** Définit le temps actuel. */
    public void setTime(float time) { this.time = time; }

    /** @return à partir de quelle image la hitbox s’active. */
    public int getActivationFrame() { return this.activationFrame; }

    /** @return la vitesse d’animation. */
    public float getAnimSpeed() { return this.animSpeed; }

    /** Définit la vitesse d’animation. */
    public void setAnimSpeed(float animSpeed) { this.animSpeed = animSpeed; }

    public HashSet<CObject> getAlreadyHitted() { return this.alreadyHitted; }

    public boolean isEnnemyHitted() {
        return this.ennemyHitted;
    }

    public void setEnnemyHitted(boolean ennemyHitted) {
        this.ennemyHitted = ennemyHitted;
    }
}
