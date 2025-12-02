package Objects;

import Utilitary.V2D;

/**
 * Représente un élément du décor ou de l’environnement du jeu.
 *  
 * Cette classe sert de base à tous les objets environnementaux :
 * murs, obstacles, éléments interactifs, décorations, pièges, etc.
 *  
 * Elle hérite de {@link CObject}, et offre plusieurs constructeurs permettant
 * de définir un sprite simple, un sprite animé ou encore une hitbox personnalisée.
 */
public abstract class Environment extends CObject
{
    /**
     * Crée un élément d’environnement avec sprite animé.
     *
     * @param size       taille visuelle
     * @param position   position initiale
     * @param spriteName nom du sprite
     * @param animName   animation associée
     */
    public Environment(V2D size, V2D position, String spriteName, String animName)
    {
        super(size, position, spriteName, animName);
    }

    /**
     * Crée un élément d’environnement possédant une hitbox personnalisée.
     *
     * @param size          taille visuelle
     * @param position      position
     * @param collisionSize taille de la zone de collision
     * @param spriteName    nom du sprite
     */
    public Environment(V2D size, V2D position, V2D collisionSize, String spriteName)
    {
        super(size, position, collisionSize, spriteName);
    }

    /**
     * Crée un élément d’environnement avec hitbox personnalisée et animation.
     *
     * @param size          taille visuelle
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     * @param spriteName    nom du sprite
     * @param animName      nom de l’animation
     */
    public Environment(V2D size, V2D position, V2D collisionSize, String spriteName, String animName)
    {
        super(size, position, collisionSize, spriteName, animName);
    }
}
