package Objects;

import java.util.Random;

import Level.Level;
import Utilitary.V2D;

/**
 * Représente un personnage générique du jeu, doté de caractéristiques,
 * d'une quantité d'or, d'un indicateur d'action et d'un état de blessure.
 * Les personnages peuvent se déplacer, subir des dégâts et mourir.
 */
public abstract class Character extends Actor
{
    /** Caractéristiques du personnage (points de vie, armure, vitesse, etc.). */
    private Caracteristics caracteristics = null;

    /** Quantité d'or possédée par le personnage. */
    private int gold = 0;

    /** Indique si le personnage est en train d'effectuer une action. */
    private boolean bAction = false;

    /** Durée restante de l'état de blessure, décrémentée à chaque mise à jour. */
    private int hit = 0;

    protected final Random random = new Random();

    /**
     * Crée un personnage avec sprite statique.
     *
     * @param size          taille visuelle
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     * @param spriteName    nom du sprite
     */
    public Character(V2D size, V2D position, V2D collisionSize, String spriteName)
    {
        super(size, position, collisionSize, spriteName);
        this.caracteristics = new Caracteristics(3, 0, 1, 1.f, 0.01f);
    }

    /**
     * Crée un personnage avec sprite animé.
     *
     * @param size          taille visuelle
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     * @param spriteName    nom du sprite
     * @param animName      animation à charger
     */
    public Character(V2D size, V2D position, V2D collisionSize, String spriteName, String animName)
    {
        super(size, position, collisionSize, spriteName, animName);
        this.caracteristics = new Caracteristics(3, 0, 1, 1.f, 0.01f);
    }

    /**
     * Déclenche la mort du personnage.
     * La collision est détruite et l'objet est retiré de la salle actuelle.
     */
    public void onDeath()
    {
        this.getCollision().destroy();
        Level.getActualRoom().removeObject(this);
    };

    /**
     * Applique des dégâts au personnage.
     * L'armure absorbe d'abord une partie des dégâts ; le surplus affecte les points de vie.
     * En cas de mort, {@link #onDeath()} est appelé. Sinon, l'état de blessure est défini.
     *
     * @param inDamage dégâts reçus
     */
    public void takeDamage(int inDamage)
    {
        int remainingDamage = inDamage - this.caracteristics.getActArmor();
        this.caracteristics.setActArmor(this.caracteristics.getActArmor() - inDamage);

        if (remainingDamage <= 0) return;
        
        this.caracteristics.setActHP(this.caracteristics.getActHP() - inDamage);
        if (this.caracteristics.getActHP() == 0) this.onDeath();
        else this.setHit(10);
    }

    /**
     * Mise à jour du personnage à chaque frame.
     * Met à jour l'animation et décrémente éventuellement l’état de blessure.
     *
     * @param dt temps écoulé depuis la dernière mise à jour
     */
    @Override
    public void update(float dt)
    {
        super.update(dt);
        if (this.hit > 0) this.hit--;
    }

    /**
     * Déplacement horizontal affecté par la vitesse du personnage.
     *
     * @param x déplacement horizontal brut
     */
    @Override
    public void addX(double x) { super.addX(x * this.caracteristics.speed); }

    /**
     * Déplacement vertical affecté par la vitesse du personnage.
     *
     * @param y déplacement vertical brut
     */
    @Override
    public void addY(double y) { super.addY(y * this.caracteristics.speed); }

    /** @return les caractéristiques du personnage */
    public Caracteristics getCaracteristics() {
        return this.caracteristics;
    }

    /** @return la quantité d'or possédée */
    public int getGold() {
        return this.gold;
    }

    /** @return vrai si le personnage est en action */
    public boolean isbAction() {
        return this.bAction;
    }

    /**
     * @return le pourcentage de points de vie restants entre 0 et 1
     */
    public float getPercentHP()
    {
        return (float)this.getCaracteristics().getActHP() / (float)this.getCaracteristics().getMaxHP();
    }

    /**
     * Définit la quantité d'or.
     *
     * @param gold nouvelle valeur d'or
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * Définit si le personnage est en action.
     *
     * @param bAction nouvel état d'action
     */
    public void setbAction(boolean bAction) {
        this.bAction = bAction;
    }

    /** @return durée restante de l'état de blessure */
    public int getHit() {
        return this.hit;
    }

    /**
     * Modifie la durée de l'état de blessure.
     *
     * @param hit durée avant disparition de l'effet
     */
    public void setHit(int hit) {
        this.hit = hit;
    }

    public void setCaracteristics(Caracteristics caracteristics) { this.caracteristics = caracteristics; }
}
