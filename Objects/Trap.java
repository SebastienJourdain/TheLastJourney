package Objects;

import java.util.Random;

import Level.Level;
import Utilitary.V2D;

/**
 * Représente un piège au sol, capable de s’activer aléatoirement.
 *  
 * Un piège alterne entre deux états :
 * <ul>
 *     <li><b>inactif</b> : animé via « inactive_trap », sans danger ;</li>
 *     <li><b>actif</b> : animé via « active_trap », provoquant immédiatement un
 *     coup de zone ({@link Melee}) au-dessus de la tuile.</li>
 * </ul>
 *
 * L’activation survient aléatoirement selon une probabilité faible à chaque mise à jour.
 */
public class Trap extends Actor
{
    /** Générateur aléatoire pour l’activation du piège. */
    private final static Random rand = new Random();

    /** Indique si le piège est actuellement actif. */
    private boolean bActive = false;

    /**
     * Crée un piège au sol.
     * Le piège commence toujours dans son animation inactive.
     *
     * @param size     dimensions visuelles du piège
     * @param position position du piège dans la salle
     */
    public Trap(V2D size, V2D position)
    {
        super(size, position, "TileSet", "inactive_trap");
    }

    /**
     * Appelé à la fin de l’animation de piège actif.
     *  
     * Le piège repasse automatiquement à l’état inactif.
     */
    @Override
    public void onAnimationEnd()
    {
        this.bActive = false;
        this.getSprite().setAnimName("inactive_trap");
    }

    /**
     * Met à jour le piège.  
     *  
     * Si le piège est inactif, il possède une faible chance de s’activer :
     * <ul>
     *   <li>animation « active_trap » déclenchée</li>
     *   <li>génération immédiate d’une hitbox {@link Melee} représentant
     *   l’impact/sursaut du piège</li>
     * </ul>
     *
     * @param dt temps écoulé depuis la dernière mise à jour
     */
    @Override
    public void update(float dt)
    {
        super.update(dt);

        if (!this.bActive && rand.nextInt(100) < 2)
        {
            this.bActive = true;
            this.getSprite().setAnimName("active_trap");
            Melee damage = new Melee(this.getSize(), new V2D(this.getPosition().x, this.getPosition().y + 26.0), new V2D(14.0, 16.0), 1, 5, this, 3);
            Level.getActualRoom().addObject(damage);
        }
    }
}
