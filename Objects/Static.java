package Objects;

import Utilitary.V2D;

/**
 * Représente un élément d’environnement statique.
 *  
 * Cette classe sert de base aux objets immobiles du décor :
 * murs, piliers, colonnes, meubles, obstacles fixes, statues,
 * ou tout autre élément qui ne se déplace jamais et ne possède
 * aucun comportement dynamique.
 *
 * Elle hérite d’{@link Environment} et propose différentes formes
 * d’initialisation selon que l’objet utilise un sprite simple,
 * une animation ou une hitbox personnalisée.
 */
public abstract class Static extends Environment
{
    /**
     * Crée un élément statique doté d’un sprite animé.
     *
     * @param size       taille visuelle de l’objet
     * @param position   position dans la salle
     * @param spriteName nom du sprite
     * @param animName   nom de l’animation associée
     */
    public Static(V2D size, V2D position, String spriteName, String animName)
    {
        super(size, position, spriteName, animName);
    }

    /**
     * Crée un élément statique possédant une hitbox personnalisée.
     *
     * @param size          taille visuelle
     * @param position      position
     * @param collisionSize dimensions de la zone de collision
     * @param spriteName    nom du sprite
     */
    public Static(V2D size, V2D position, V2D collisionSize, String spriteName)
    {
        super(size, position, collisionSize, spriteName);
    }

    /**
     * Crée un élément statique avec hitbox personnalisée et animation.
     *
     * @param size          taille visuelle
     * @param position      position initiale
     * @param collisionSize dimensions de la zone de collision
     * @param spriteName    nom du sprite
     * @param animName      nom de l’animation associée
     */
    public Static(V2D size, V2D position, V2D collisionSize, String spriteName, String animName)
    {
        super(size, position, collisionSize, spriteName, animName);
    }
}
