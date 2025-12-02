package Objects;

import Level.Level;
import Utilitary.V2D;

/**
 * Représente un personnage non-joueur standard (monstre classique).
 * Cette classe sert de base à tous les monstres ordinaires du jeu
 * — à l’exception des boss — et définit un comportement commun
 * pour le déplacement, l’animation et l’attaque.
 */
public abstract class Classic extends NonPlayerCharacter
{
    /**
     * Crée un monstre classique avec une animation de base « walk_bot ».
     *
     * @param size          taille visuelle du monstre
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     * @param spriteName    nom du sprite associé
     */
    public Classic(V2D size, V2D position, V2D collisionSize, String spriteName)
    {
        super(size, position, collisionSize, spriteName);
        this.getSprite().setBaseAnimName("walk_bot");
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
    public void attack()
    {
        if (this.isbAction()) return;
        this.getSprite().setAnimName("attack_bot");
        this.setbAction(true);
    }

    public void regen(int regenRate)
    {
        Effect effect = new Effect(new V2D(this.getSize()), this.getPosition(), new V2D(-1,-1), "healing");
        Level.getActualRoom().addObject(effect);
        this.getCaracteristics().setActHP(this.getCaracteristics().getActHP() + regenRate);
    }
}
