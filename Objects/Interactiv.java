package Objects;

import Utilitary.V2D;

/**
 * Représente un élément d’environnement interactif.
 *  
 * Cette classe sert de base à tous les objets avec lesquels le joueur
 * ou les entités peuvent interagir : leviers, portes, coffres, mécanismes,
 * pièges réarmables, plateformes mobiles, etc.
 *  
 * Elle hérite d’{@link Environment} et fournit plusieurs constructeurs
 * permettant de définir un sprite simple, animé, ou une hitbox personnalisée.
 */
public abstract class Interactiv extends Environment
{
    /**
     * Crée un objet interactif doté d’un sprite animé.
     *
     * @param size       dimensions visuelles
     * @param position   position de l’objet
     * @param spriteName nom du sprite
     * @param animName   animation associée
     */
    public Interactiv(V2D size, V2D position, String spriteName, String animName)
    {
        super(size, position, spriteName, animName);
    }

    /**
     * Crée un objet interactif avec hitbox personnalisée.
     *
     * @param size          taille visuelle
     * @param position      position dans la salle
     * @param collisionSize taille de la hitbox
     * @param spriteName    nom du sprite
     */
    public Interactiv(V2D size, V2D position, V2D collisionSize, String spriteName)
    {
        super(size, position, collisionSize, spriteName);
    }

    /**
     * Crée un objet interactif avec hitbox personnalisée et animation dédiée.
     *
     * @param size          dimensions visuelles
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     * @param spriteName    nom du sprite à utiliser
     * @param animName      nom de l’animation associée
     */
    public Interactiv(V2D size, V2D position, V2D collisionSize, String spriteName, String animName)
    {
        super(size, position, collisionSize, spriteName, animName);
    }
}
