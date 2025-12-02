package Objects;

import Level.Level;
import Utilitary.V2D;

/**
 * Représente la fin d’un niveau ou un passage vers l’étage suivant.
 *  
 * Cet objet interactif déclenche automatiquement le changement de niveau
 * lorsqu’un joueur entre en collision avec lui.
 */
public class LevelEnd extends Interactiv
{    
    /**
     * Crée un point de fin de niveau avec une hitbox personnalisée.
     *
     * @param size          taille visuelle de l’objet
     * @param position      position dans la salle
     * @param collisionSize dimensions de la zone de collision
     * @param spriteName    nom du sprite associé
     */
    public LevelEnd(V2D size, V2D position, V2D collisionSize, String spriteName)
    {
        super(size, position, collisionSize, spriteName);
        this.getCollision().setbActive(false);
    }

    /**
     * Crée un point de fin de niveau possédant une animation.
     *
     * @param size          taille visuelle de l’objet
     * @param position      position dans la salle
     * @param collisionSize dimensions de la zone de collision
     * @param spriteName    nom du sprite
     * @param animName      animation associée
     */
    public LevelEnd(V2D size, V2D position, V2D collisionSize, String spriteName, String animName)
    {
        super(size, position, collisionSize, spriteName, animName);
        this.getCollision().setbActive(false);
    }

    /**
     * Déclenché lorsqu’un objet entre en collision avec le point de sortie.
     *  
     * Si cet objet est un {@link Player}, le niveau suivant est immédiatement chargé.
     *
     * @param o objet entrant en collision
     */
    @Override
    public void onCollisionBegin(CObject o)
    {
        if (o instanceof Player) Level.nextFloor();
    }
}
