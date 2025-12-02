package Objects;

import Graphism.Animation;
import Utilitary.V2D;

/**
 * Représente un obstacle statique du décor.
 *  
 * Un obstacle :
 * <ul>
 *   <li>est immobile ;</li>
 *   <li>bloque le déplacement des entités (collision bloquante) ;</li>
 *   <li>peut posséder une animation (brasero, coffre, pile d’or, tonneaux, etc.) ;</li>
 *   <li>adapte dynamiquement sa hitbox en fonction du sprite et de l’animation.</li>
 * </ul>
 *
 * Les hitbox par défaut sont recalculées lorsque la taille fournie est nulle :
 * chaque type d’obstacle animé ajuste sa position et sa zone de collision
 * pour correspondre fidèlement à son apparence visuelle.
 */
public class Obstacle extends Static
{
    //private Room nextRoom = null;
    
    /**
     * Crée un obstacle simple avec une collision bloquante.
     *
     * @param size          taille visuelle de l’obstacle
     * @param position      position dans la salle
     * @param collisionSize taille de la hitbox
     * @param spriteName    sprite utilisé
     */
    public Obstacle(V2D size, V2D position, V2D collisionSize, String spriteName)
    {
        super(size, position, collisionSize, spriteName);
        this.getCollision().setBlocking(true);
    }

    /**
     * Crée un obstacle animé et ajuste automatiquement sa hitbox
     * en fonction du sprite et de l’animation fournis.
     *
     * Si la taille de collision initiale vaut (0,0), celle-ci est recalculée selon :
     * <ul>
     *     <li>la taille réelle du frame d’animation,</li>
     *     <li>la taille visuelle de l’objet,</li>
     *     <li>des corrections spécifiques selon le type d’animation
     *         (ex. brasero, coffre, piles d’or, tonneaux…).</li>
     * </ul>
     *
     * @param size          taille visuelle
     * @param position      position
     * @param collisionSize hitbox initiale (peut être 0)
     * @param spriteName    atlas utilisé
     * @param animName      animation associée
     */
    public Obstacle(V2D size, V2D position, V2D collisionSize, String spriteName, String animName)
    {
        super(size, position, collisionSize, spriteName, animName);
        this.getCollision().setBlocking(true);

        if (this.getCollision().getSize().x == 0 && this.getCollision().getSize().y == 0)
        {
            V2D collSize = new V2D(Animation.get(spriteName, animName).getFrameSize());
            collSize.mult(size);

            switch (animName)
            {
                case "brasero" -> {
                    this.getCollision().setPosition(new V2D(this.getPosition().x + 20.0, this.getPosition().y + 80.0));
                    collSize = new V2D(collSize.x - 40.0, collSize.y - 100.0);
                }
                case "gold_pile" -> {
                    this.getCollision().setPosition(new V2D(this.getPosition().x, this.getPosition().y + 20.0));
                    collSize = new V2D(collSize.x, collSize.y - 40.0);
                }
                case "chest" -> {
                    this.getCollision().setPosition(new V2D(this.getPosition().x, this.getPosition().y + 20.0));
                    collSize = new V2D(collSize.x, collSize.y - 40.0);
                }
                case "barrel_1" -> {
                    this.getCollision().setPosition(new V2D(this.getPosition().x, this.getPosition().y + 20.0));
                    collSize = new V2D(collSize.x, collSize.y - 40.0);
                }
                case "barrel_2" -> {
                    this.getCollision().setPosition(new V2D(this.getPosition().x, this.getPosition().y + 20.0));
                    collSize = new V2D(collSize.x, collSize.y - 40.0);
                }
                case "box" -> {
                    this.getCollision().setPosition(new V2D(this.getPosition().x, this.getPosition().y + 20.0));
                    collSize = new V2D(collSize.x, collSize.y - 40.0);
                }
                case "skull_pile" -> {
                    this.getCollision().setPosition(new V2D(this.getPosition().x + 20, this.getPosition().y + 50.0));
                    collSize = new V2D(collSize.x - 40, collSize.y - 50.0);
                }
                case "skulls" -> {
                    this.getCollision().setPosition(new V2D(this.getPosition().x, this.getPosition().y + 20.0));
                    collSize = new V2D(collSize.x, collSize.y - 40.0);
                }
                case "skeleton" -> {
                    this.getCollision().setPosition(new V2D(this.getPosition().x, this.getPosition().y + 20.0));
                    collSize = new V2D(collSize.x, collSize.y - 40.0);
                }
                default -> {
                }
            }
            this.getCollision().setSize(collSize);
        }
    }

    /**
     * Met à jour l’animation de l’obstacle.
     *
     * L’obstacle étant immobile et sans logique dynamique,
     * seule l’animation est concernée.
     */
    @Override
    public void update(float dt)
    {
        this.getSprite().updateAnim(dt);
    }
}
