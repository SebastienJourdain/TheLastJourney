package Objects;

import Utilitary.V2D;

/**
 * Représente un ennemi de type boss.
 * Cette classe sert de base à toutes les créatures majeures du jeu,
 * plus puissantes que les monstres standards ({@link Classic}) et
 * disposant en général de comportements, attaques ou mécaniques spécifiques.
 *
 * Elle hérite de {@link NonPlayerCharacter}, ce qui lui permet d’utiliser
 * une intelligence artificielle dédiée ainsi qu’un système de caractéristiques
 * et d’interactions complet.
 */
public abstract class Boss extends NonPlayerCharacter
{
    /**
     * Constructeur d’un boss avec sprite statique.
     *
     * @param size          taille visuelle du boss
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     * @param spriteName    nom du sprite associé
     */
    public Boss(V2D size, V2D position, V2D collisionSize, String spriteName)
    {
        super(size, position, collisionSize, spriteName);
        this.setGold(5);
    }

    /**
     * Constructeur d’un boss avec sprite animé.
     *
     * @param size          taille visuelle du boss
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     * @param spriteName    nom du sprite associé
     * @param animName      animation à utiliser par défaut
     */
    public Boss(V2D size, V2D position, V2D collisionSize, String spriteName, String animName)
    {
        super(size, position, collisionSize, spriteName, animName);
        this.setGold(5);
    }

    /**
     * Déplace le monstre sur l’axe X uniquement s’il n’est pas en action.
     *
     * @param x déplacement voulu sur l’axe X
     */
    @Override
    public void addX(double x)
    {
        if (!this.isbAction()) super.addX(x);
    }

    /**
     * Déplace le monstre sur l’axe Y uniquement s’il n’est pas en action.
     *
     * @param y déplacement voulu sur l’axe Y
     */
    @Override
    public void addY(double y)
    {
        if (!this.isbAction()) super.addY(y);
    }

    public void attackDash(V2D dir)
    {
        super.addX(dir.x);
        super.addY(dir.y);
    }

    /**
     * Appelé à la fin d’une animation.
     * Permet au monstre de redevenir disponible pour une nouvelle action.
     */
    @Override
    public void onAnimationEnd()
    {
        this.setbAction(false);
    }

    /**
     * Déclenche une attaque standard.
     * L’attaque ne peut être effectuée que si aucune autre action n’est en cours.
     * Active l’animation « attack_bot » et verrouille les actions jusqu’à la fin de l’animation.
     */
    public void attack1()
    {
        if (this.isbAction()) return;
        this.getSprite().setAnimName("attack1_bot");
        this.setbAction(true);
    }
    public void attack2()
    {
        if (this.isbAction()) return;
        this.getSprite().setAnimName("attack2_bot");
        this.setbAction(true);
    }
}
