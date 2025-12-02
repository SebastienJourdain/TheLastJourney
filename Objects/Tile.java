package Objects;

import Utilitary.V2D;

/**
 * Représente une tuile du décor, élément statique utilisé pour composer
 * le sol, les murs ou diverses surfaces de l’environnement.
 *  
 * Une tuile peut posséder un sprite simple ou une animation,
 * mais ne dispose d’aucun comportement dynamique.
 */
public class Tile extends Static
{
    //private Room nextRoom = null;
    
    /**
     * Crée une tuile avec un sprite fixe.
     *
     * @param size       dimensions visuelles de la tuile
     * @param position   position dans la salle
     * @param spriteName nom du sprite associé
     */
    public Tile(V2D size, V2D position, String spriteName)
    {
        super(size, position, spriteName, "");
        if (this.getCollision() != null) this.getCollision().setbActive(false);
    }

    /**
     * Crée une tuile pouvant posséder une animation.
     *
     * @param size       dimensions visuelles de la tuile
     * @param position   position dans la salle
     * @param spriteName nom du sprite
     * @param animName   animation associée
     */
    public Tile(V2D size, V2D position, String spriteName, String animName)
    {
        super(size, position, spriteName, animName);
        if (this.getCollision() != null) this.getCollision().setbActive(false);
    }
}
