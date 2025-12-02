package Objects;

import Level.Level;
import Utilitary.V2D;

/**
 * Représente un effet visuel temporaire (particules, fumée, impact, dash, etc.).
 *  
 * Un {@code Effect} est un objet animé qui n’interagit pas physiquement :
 * sa collision est immédiatement planifiée pour suppression dès sa création.
 * Lorsqu’il termine son animation, il se détruit automatiquement.
 *  
 * Les effets permettent d’ajouter des animations visuelles sans interférer
 * avec les mécaniques de gameplay.
 */
public class Effect extends Actor
{
    /** Indique si l’effet doit être affiché en inversion horizontale. */
    private boolean bReverse = false;
    
    /**
     * Crée un effet visuel.
     * La collision est automatiquement marquée pour suppression afin que
     * l’effet n’interagisse pas avec d’autres éléments du jeu.
     *
     * @param size          taille visuelle de l’effet
     * @param position      position initiale
     * @param collisionSize taille de collision (valeur souvent ignorée)
     * @param animName      nom de l’animation associée
     */
    public Effect(V2D size, V2D position, V2D collisionSize, String animName)
    {
        super(size, position, collisionSize, "Effects", animName);
        Level.getActualRoom().getCollisionsToRemove().add(this.getCollision());
    }

    /**
     * Appelé à la fin de l’animation.
     * Retire immédiatement l’effet de la salle et réinitialise l’animation.
     */
    @Override
    public void onAnimationEnd()
    {
        Level.getActualRoom().removeObject(this);
        this.getSprite().setBaseAnimName("");
    }

    /** @return vrai si l’effet est inversé horizontalement. */
    public boolean isbReverse() {
        return this.bReverse;
    }

    /** Définit si l’effet doit être affiché en miroir horizontal. */
    public void setbReverse(boolean bReverse) {
        this.bReverse = bReverse;
    }
}
