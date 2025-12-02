package Objects;

import Utilitary.V2D;

/**
 * Représente un acteur abstrait du jeu, doté d'une orientation et héritant de {@link CObject}.
 * Les acteurs disposent d'une position, d'une taille, d'une éventuelle zone de collision
 * et peuvent être mis à jour à chaque frame.
 */
public abstract class Actor extends CObject
{
    /**
     * Vecteur d’orientation de l’acteur. Par défaut dirigé vers le haut (0, 1).
     */
    V2D orientation = new V2D(0.0, 1.0);
    
    /**
     * Construit un acteur avec taille, position, sprite et animation.
     *
     * @param size         dimensions de l’acteur
     * @param position     position initiale
     * @param spriteName   nom du sprite associé
     * @param animName     nom de l’animation associée
     */
    public Actor(V2D size, V2D position, String spriteName, String animName)
    {
        super(size, position, spriteName, animName);
    }

    /**
     * Construit un acteur avec taille, position, zone de collision et sprite.
     *
     * @param size          dimensions de l’acteur
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     * @param spriteName    nom du sprite associé
     */
    public Actor(V2D size, V2D position, V2D collisionSize, String spriteName)
    {
        super(size, position, collisionSize, spriteName);
    }

    /**
     * Construit un acteur avec taille, position, zone de collision, sprite et animation.
     *
     * @param size          dimensions de l’acteur
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     * @param spriteName    nom du sprite
     * @param animName      nom de l’animation
     */
    public Actor(V2D size, V2D position, V2D collisionSize, String spriteName, String animName)
    {
        super(size, position, collisionSize, spriteName, animName);
    }

    /**
     * Met à jour l’acteur. Si un sprite est présent, son animation avance selon le delta‐temps.
     *
     * @param dt durée écoulée depuis la dernière mise à jour (delta time)
     */
    @Override
    public void update(float dt)
    {
        if (this != null) this.getSprite().updateAnim(dt);
    };

    /**
     * Retourne l’orientation actuelle de l’acteur.
     *
     * @return vecteur d’orientation
     */
    public V2D getOrientation() {
        return this.orientation;
    }

    /**
     * Définit l’orientation de l’acteur.
     *
     * @param orientation nouvelle orientation
     */
    public void setOrientation(V2D orientation) { if (this != null) this.orientation = orientation; }

    /**
     * Modifie uniquement l’axe X du vecteur d’orientation.
     *
     * @param x nouvelle composante X
     */
    public void setOrientationX(double x) { if (this != null) this.orientation.x = x; }

    /**
     * Modifie uniquement l’axe Y du vecteur d’orientation.
     *
     * @param y nouvelle composante Y
     */
    public void setOrientationY(double y) { if (this != null) this.orientation.y = y; }
}
