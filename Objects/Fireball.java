package Objects;

import Graphism.Animation;
import Level.Level;
import Sound.SoundPlayer;
import Utilitary.V2D;

/**
 * Représente une boule de feu magique se déplaçant à grande vitesse
 * et explosant à l’impact.
 *  
 * Une Fireball fonctionne en deux phases distinctes :
 * <ul>
 *   <li><b>phase de vol</b> : déplacement continu selon un vecteur de vitesse ;</li>
 *   <li><b>impact</b> : à la première collision pertinente, la boule explose,
 *       affiche une animation dédiée, crée un effet visuel, puis génère une
 *       {@link ZoneDamage} circulaire.</li>
 * </ul>
 *
 * Une fois en état d’explosion, la Fireball cesse de se déplacer et se limite
 * à jouer son animation finale avant d’être détruite.
 */
public class Fireball extends Projectile
{
    private boolean Sound = false;
    /** Indique si l’impact a déjà eu lieu (afin d’éviter les collisions multiples). */
    private boolean hit = false;

    /**
     * Crée une boule de feu prête à être lancée.
     *
     * @param position  position de départ (centre du lanceur)
     * @param damage    dégâts initiaux de l’impact
     * @param owner     créateur de la boule de feu
     * @param lifespan  durée de vie maximale
     * @param velocity  direction et vitesse de déplacement
     */
    public Fireball(V2D position, int damage, CObject owner, int lifespan, V2D velocity)
    {
        super(  new V2D(1.0, 1.0),
                new V2D(position.x + (velocity.x * 10.f), position.y + (velocity.y * 10.f)),
                new V2D(32, 32), 
                damage, lifespan, owner, 0, 
                new V2D(velocity.x * 3.f, velocity.y * 3.f), 
                "Effects");
        this.getSprite().setAnimName("energy_ball");

        // Direction normalisée du projectile
        V2D dir = new V2D(velocity.x, velocity.y);
        dir.normalize();

        double offset = -10.0; // ~10 px devant le projectile

        // Centre de la collision : position du projectile + offset dans la direction du tir
        this.getCollision().setPosition(new V2D(
            this.getPosition().x + dir.x * offset,
            this.getPosition().y + dir.y * offset
        ));
        this.getCollision().setbCircle(true);
        this.getCollision().setR(16.f);
    }


    /**
     * Gère l’impact de la boule de feu.
     *  
     * Lors de la première collision significative :
     * <ul>
     *   <li>la collision du projectile est supprimée ;</li>
     *   <li>l’animation passe en mode explosion ;</li>
     *   <li>un effet visuel d’explosion est généré ;</li>
     *   <li>une {@link ZoneDamage} est créée au centre de l’impact ;</li>
     *   <li>le projectile entre en phase d’explosion (immobile).</li>
     * </ul>
     */
    @Override
    public void onCollisionBegin(CObject o)
    {
        if (o == this.getOwner() || o instanceof Damage) {}
        else if (!this.hit)
        {
            //Suppression de la collision
            Level.getActualRoom().getCollisionsToRemove().add(this.getCollision());
            //Changement de l'animation
            this.getSprite().setAnimName("energy_ball_explode");
            //Augmentation de la taille pour l'explosion
            this.setSize(new V2D(1.0, 1.0));
            this.hit = true;
            Animation A = Animation.get(this.getSprite().getAtlasName(), "energy_ball_explode");
            Effect explode = new Effect(new V2D(3.0, 3.0), new V2D(this.getPosition().x - (A.getFrameSize().x / 2), this.getPosition().y - (A.getFrameSize().y / 2)), new V2D(-1,-1), "blue_explosion");
            Level.getActualRoom().addObject(explode);
            //Ajout de la zone de dégats
            ZoneDamage damage = new ZoneDamage(new V2D(0.0, 0.0), this.getPosition(), new V2D(32, 32), this.getDamage(), 1, 1, this.getOwner(), "", 64.f);
            Level.getActualRoom().addObject(damage);
        }
    }

    /**
     * Détruit la Fireball lorsque son animation d’explosion est terminée.
     */
    @Override
    public void onAnimationEnd()
    {
        Level.getActualRoom().removeObject(this);
    }

    /**
     * Met à jour la Fireball.
     *  
     * <ul>
     *   <li>En phase de vol : utilise la logique de {@link Projectile#update(float)}.</li>
     *   <li>En phase d’explosion : ne bouge plus et se contente de jouer l’animation.</li>
     * </ul>
     */
    @Override
    public void update(float dt)
    {
        if (this.getOwner() instanceof Mage c && !this.getCollision().isbActive())
        {
            V2D movement = new V2D(this.getPosition());
            this.getVelocity().x = c.getAim().x * 20.f;
            this.getVelocity().y = c.getAim().y * 20.f;
            this.setPosition(new V2D(c.getPosition().x + (this.getVelocity().x * 2.f), c.getPosition().y + (this.getVelocity().y * 2.f)));
            movement.x = this.getPosition().x - movement.x;
            movement.y = this.getPosition().y - movement.y;
            this.getCollision().addX(movement.x);
            this.getCollision().addY(movement.y);
        }
        else if (this.getOwner() instanceof Mage c && this.getCollision().isbActive() && !Sound)
        {
            if (c.getSprite().getAnimName().startsWith("fast"))SoundPlayer.playSFX("MageBall.wav");
            Sound = true;
        }

        if (!this.hit) super.update(dt);
        else this.getSprite().updateAnim(dt);
    }
}
