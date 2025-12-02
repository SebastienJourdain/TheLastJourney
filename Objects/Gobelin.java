package Objects;

import AI.GoblinAI;
import Level.Level;
import Utilitary.V2D;

/**
 * Représente un monstre de type gobelin.
 * Le gobelin hérite du comportement des monstres standards ({@link Classic})
 * et possède une IA dédiée ainsi qu’une attaque à distance utilisant une petite bille.
 */
public class Gobelin extends Classic
{
    /**
     * Crée un gobelin avec sa taille, sa position et sa zone de collision.
     * Initialise son IA, ajuste son sprite et sa vitesse.
     *
     * @param size          taille visuelle du gobelin
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     */
    public Gobelin(V2D size, V2D position, V2D collisionSize)
    {
        super(size, position, collisionSize, "Gobelin");
        this.getSprite().setAnimName("walk_bot");
        this.getCollision().setPosition(new V2D(position.x - 12.0, position.y));
        this.setAi(new GoblinAI());
        this.setCaracteristics(new Caracteristics(2 * Level.getFloor(), 0, 1 + (Level.getFloor() / 2), 2.5f, 0.f));
    }

    /**
     * Attaque spécifique du gobelin.
     * Le gobelin effectue d’abord son attaque standard (animation « attack_bot »),
     * puis lance un petit projectile ({@link Ball}) en direction du joueur.
     * Le projectile ne s’active que si aucune action n’était déjà en cours.
     */
    @Override
    public void attack()
    {
        if (this.isbAction()) return;
        super.attack();

        V2D dir = new V2D(  Player.getPlayer1().getPosition().x - this.getPosition().x,
                            Player.getPlayer1().getPosition().y - this.getPosition().y);
        dir.normalize();
        Ball damage = new Ball(new V2D(this.getPosition().x, this.getPosition().y), this.getCaracteristics().getDamage(), this, 7, new V2D(dir.x * 2.f, dir.y * 2.f));
        Level.getActualRoom().addObject(damage);
    }
}
