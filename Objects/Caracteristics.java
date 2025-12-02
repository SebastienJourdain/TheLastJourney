package Objects;

/**
 * Représente l’ensemble des caractéristiques fondamentales d’un personnage
 * (joueur, monstre ou boss).  
 *
 * Cette classe regroupe les valeurs suivantes :
 * <ul>
 *   <li>Points de vie maximum et actuels</li>
 *   <li>Armure maximum et actuelle</li>
 *   <li>Dégâts de base</li>
 *   <li>Vitesse de déplacement</li>
 *   <li>Taux de coup critique</li>
 * </ul>
 *
 * Les caractéristiques sont stockées simplement et disposent de mutateurs
 * garantissant l’absence de valeurs négatives.
 */
public class Caracteristics
{
    /** Points de vie maximum. */
    protected int maxHP = 0;

    /** Points de vie actuels. */
    protected int actHP = 0;

    /** Armure maximum. */
    protected int maxArmor = 0;

    /** Armure actuelle. */
    protected int actArmor = 0;

    /** Dégâts infligés par défaut. */
    protected int damage = 0;

    /** Vitesse de déplacement. */
    protected float speed = 1.f;

    /** Chance de coup critique (entre 0 et 1). */
    protected float critical = 0.01f;

    /**
     * Construit un ensemble de caractéristiques complètes.
     *
     * @param HP        points de vie initiaux (max et actuels)
     * @param armor     armure initiale (max et actuelle)
     * @param damage    dégâts de base
     * @param speed     vitesse de déplacement
     * @param critical  taux de critique
     */
    public Caracteristics(int HP, int armor, int damage, float speed, float critical)
    {
        this.maxHP = this.actHP = HP;
        this.maxArmor = this.actArmor = armor;
        this.damage = damage;
       	this.speed = speed;
        this.critical = critical;
    }

    /**
     * Constructeur par copie.
     *
     * @param c instance de caractéristiques à dupliquer
     */
    public Caracteristics(Caracteristics c)
    {
        this.maxHP = c.maxHP;
        this.actHP = c.actHP;
        this.maxArmor = c.maxArmor;
        this.actArmor = c.actArmor;
        this.damage = c.damage;
        this.speed = c.speed;
        this.critical = c.critical;
    }

    /** @return points de vie maximum */
    public int getMaxHP() { return this.maxHP; }

    /** @return points de vie actuels */
    public int getActHP() { return this.actHP; }

    /** @return armure maximum */
    public int getMaxArmor() { return this.maxArmor; }

    /** @return armure actuelle */
    public int getActArmor() { return this.actArmor; }

    /** @return dégâts de base */
    public int getDamage() { return this.damage; }

    /** @return vitesse de déplacement */
    public float getSpeed() { return this.speed; }

    /** @return taux de coup critique */
    public float getCritical() { return this.critical; }

    /**
     * Définit les points de vie maximum.
     * Empêche toute valeur négative.
     */
    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
       	this.maxHP = this.maxHP < 0 ? 0 : this.maxHP;
    }

    /**
     * Définit les points de vie actuels.
     * Empêche toute valeur négative.
     */
    public void setActHP(int actHP) {
        this.actHP = actHP;
       	this.actHP = this.actHP < 0 ? 0 : (this.actHP > this.maxHP ? this.maxHP : this.actHP);
    }

    /**
     * Définit l’armure maximum.
     * Empêche toute valeur négative.
     */
    public void setMaxArmor(int armor) {
        this.maxArmor = armor;
       	this.maxArmor = this.maxArmor < 0 ? 0 : this.maxArmor;
    }

    /**
     * Définit l’armure actuelle.
     * Empêche toute valeur négative.
     */
    public void setActArmor(int armor) {
        this.actArmor = armor;
       	this.actArmor = this.actArmor < 0 ? 0 : this.actArmor;
    }

    /** Définit les dégâts de base. */
    public void setDamage(int damage) { this.damage = damage; }

    /** Définit la vitesse de déplacement. */
    public void setSpeed(float speed) { this.speed = speed; }

    /** Définit le taux de critique. */
    public void setCritical(float critical) { this.critical = critical; }

    public void add(Caracteristics c)
    {
        this.maxArmor += c.maxArmor;
        this.actArmor += c.actArmor;
        this.maxHP += c.maxHP;
        this.actHP += c.actHP;
        this.damage += c.damage;
        this.speed += c.speed;
        this.critical += c.critical;
    }
}
