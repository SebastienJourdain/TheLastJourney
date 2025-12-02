package Objects;

import AI.SkeletonAI;
import Graphism.Animation;
import Level.Level;
import Utilitary.V2D;

/**
 * Représente un monstre de type squelette.
 * Le squelette hérite des comportements des monstres standards ({@link Classic}),
 * possède sa propre IA et effectue une attaque de mêlée directionnelle
 * synchronisée avec des animations dédiées.
 */
public class Skeleton extends Classic
{
    /**
     * Crée un squelette avec sa taille, sa position et sa zone de collision.
     * Initialise son IA, ajuste son animation de marche et règle sa vitesse.
     *
     * @param size          taille visuelle du squelette
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     */
    public Skeleton(V2D size, V2D position, V2D collisionSize)
    {
        super(size, position, collisionSize, "Skeleton");
        this.getSprite().setAnimName("walk_bot");
        this.getCollision().setPosition(new V2D(position.x - 16.0, position.y));
        this.setAi(new SkeletonAI());
        this.setCaracteristics(new Caracteristics(1 * Level.getFloor(), 0, 1 * Level.getFloor(), 2.f, 0.f));
    }

    /**
     * Attaque de mêlée du squelette.
     * L’attaque est directionnelle : le squelette détermine d'abord
     * la direction vers le joueur, puis crée un {@link Melee} adapté
     * à cette orientation (haut, bas, gauche ou droite).
     * Chaque direction possède sa propre animation et un décalage de hitbox spécifique.
     * L’attaque ne s’exécute que si aucune autre action n’est en cours.
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
                Melee damage = new Melee(new V2D(1, 1), new V2D(this.getPosition().x - 12, this.getPosition().y - 25), new V2D(30, 25), this.getCaracteristics().getDamage(), 6, this, 4);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "attack_top");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.getSprite().setAnimName("attack_top");
            }
            else if (y > 0)
            {
                Melee damage = new Melee(new V2D(1, 1), new V2D(this.getPosition().x - 12, this.getPosition().y + 25), new V2D(30, 40), this.getCaracteristics().getDamage(), 6, this, 4);
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
                Melee damage = new Melee(new V2D(1, 1), new V2D(this.getPosition().x - 40, this.getPosition().y), new V2D(25, 30), this.getCaracteristics().getDamage(), 6, this, 4);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "attack_left");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.getSprite().setAnimName("attack_left");
            }
            else if (x > 0)
            {
                Melee damage = new Melee(new V2D(1, 1), new V2D(this.getPosition().x + 15, this.getPosition().y), new V2D(25, 30), this.getCaracteristics().getDamage(), 6, this, 4);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "attack_right");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.getSprite().setAnimName("attack_right");
            }
        }
    }
}
