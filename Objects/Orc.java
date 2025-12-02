package Objects;

import AI.OrcAI;
import Graphism.Animation;
import Level.Level;
import Utilitary.V2D;

/**
 * Représente un monstre de type orc.
 * L’orc hérite du comportement des monstres classiques ({@link Classic}),
 * et possède une IA spécifique ainsi qu’une attaque de mêlée lourde,
 * caractérisée par une large zone d’impact.
 */
public class Orc extends Classic
{
    /**
     * Crée un orc avec ses dimensions, sa position et sa zone de collision.
     * Initialise son IA dédiée, son animation de marche et sa vitesse réduite
     * (car un orc est puissant mais moins agile qu’un gobelin ou un squelette).
     *
     * @param size          taille visuelle de l’orc
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     */
    public Orc(V2D size, V2D position, V2D collisionSize)
    {
        super(size, position, collisionSize, "Orc");
        this.getSprite().setAnimName("walk_bot");
        this.getCollision().setPosition(new V2D(position.x - 20.0, position.y));
        this.setAi(new OrcAI());
        this.setCaracteristics(new Caracteristics(4 * Level.getFloor(), 0, 1 + (Level.getFloor() / 2), 1.5f, 0.f));
        this.setGold(2);
    }

    /**
     * Attaque de mêlée de l’orc.
     * L’orc, créature lourde et brutale, effectue une attaque à forte zone d’impact,
     * différente selon l’orientation : haut, bas, gauche ou droite.
     * Une instance de {@link Melee} est créée avec une large hitbox,
     * puis synchronisée avec l’animation correspondante.
     *  
     * L’attaque ne peut être déclenchée que si aucune action n’est en cours.
     */
    @Override
    public void attack()
    {
        if (this.isbAction()) return;
        super.attack();

        V2D dir = new V2D(  Player.getPlayer1().getPosition().x - this.getPosition().x,
                            Player.getPlayer1().getPosition().y - this.getPosition().y);
        dir.normalize();
        double x = dir.x;
        double y = dir.y;

        if (Math.abs(dir.x) < 0.01f && Math.abs(dir.y) < 0.01) return;

        if (Math.abs(y) > Math.abs(x))
        {
            if (y < 0)
            {
                Melee damage = new Melee(new V2D(1, 1), new V2D(this.getPosition().x - 30, this.getPosition().y - 30), new V2D(80, 30), this.getCaracteristics().getDamage(), 6, this, 4);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "attack_top");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.getSprite().setAnimName("attack_top");
            }
            else if (y > 0)
            {
                Melee damage = new Melee(new V2D(1, 1), new V2D(this.getPosition().x - 30, this.getPosition().y + 20), new V2D(80, 30), this.getCaracteristics().getDamage(), 6, this, 4);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "attack_bot");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.getSprite().setAnimName("attack_bot");
            }
        }
        else
        {
            if (x < 0)
            {
                Melee damage = new Melee(new V2D(1, 1), new V2D(this.getPosition().x - 60, this.getPosition().y - 20), new V2D(40, 60), this.getCaracteristics().getDamage(), 6, this, 4);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "attack_left");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.getSprite().setAnimName("attack_left");
            }
            else if (x > 0)
            {
                Melee damage = new Melee(new V2D(1, 1), new V2D(this.getPosition().x + 25, this.getPosition().y - 20), new V2D(40, 60), this.getCaracteristics().getDamage(), 6, this, 4);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "attack_right");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.getSprite().setAnimName("attack_right");
            }
        }
    }
}
