package Objects;

/**
 * Définit l’ensemble des effets qu’un équipement peut déclencher
 * selon les événements de gameplay affectant le joueur.
 *
 * Les implémentations typiques (voir {@link Equipment})
 * utilisent ces méthodes comme des hooks permettant
 * d’exécuter des actions conditionnelles :
 * <ul>
 *     <li>bonus temporaires,</li>
 *     <li>récupération de points de vie,</li>
 *     <li>modification d’attributs,</li>
 *     <li>déclenchement d’effets visuels,</li>
 *     <li>alteration du comportement du joueur.</li>
 * </ul>
 */
public interface EquipmentEffect
{
    /**
     * Appelé lorsque le joueur termine une salle.
     *
     */
    public void onRoomCleared();

    /**
     * Appelé lorsque le joueur subit des dégâts.
     *
     */
    public void onHit();

    /**
     * Appelé lorsqu’un ennemi est vaincu par le joueur.
     *
     */
    public void onEnemyKilled();

    /**
     * Appelé lorsque le joueur touche un ennemi.
     *
     */
    public void onEnemyHit();

    /**
     * Appelé lorsque le joueur effectue un dash.
     *
     */
    public void onDash();
}
