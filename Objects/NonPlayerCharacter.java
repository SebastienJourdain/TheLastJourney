package Objects;

import AI.AI;
import Graphism.Animation;
import Level.Level;
import Sound.SoundPlayer;
import Utilitary.V2D;

/**
 * Représente un personnage non-joueur (PNJ).
 * Cette classe étend {@link Character} et ajoute la gestion d’une intelligence
 * artificielle ainsi que des comportements spécifiques lors de la mort.
 */
public abstract class NonPlayerCharacter extends Character
{
    /** Intelligence artificielle associée à ce PNJ. Peut être nulle. */
    private AI ai = null;

    /**
     * Crée un PNJ avec sprite statique.
     *
     * @param size          taille visuelle
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     * @param spriteName    nom du sprite
     */
    public NonPlayerCharacter(V2D size, V2D position, V2D collisionSize, String spriteName)
    {
        super(size, position, collisionSize, spriteName);
        this.setGold(1);
    }

    /**
     * Crée un PNJ avec sprite animé.
     *
     * @param size          taille visuelle
     * @param position      position initiale
     * @param collisionSize taille de la zone de collision
     * @param spriteName    nom du sprite
     * @param animName      nom de l’animation
     */
    public NonPlayerCharacter(V2D size, V2D position, V2D collisionSize, String spriteName, String animName)
    {
        super(size, position, collisionSize, spriteName, animName);
        this.setGold(1);
    }

    /**
     * Distance au joueur (à redéfinir par les classes dérivées).
     *
     * @return distance entre ce PNJ et le joueur
     */
    public float getPlayerDist() { return 0.f; }

    /**
     * Nombre d’alliés autour du PNJ (à redéfinir selon le type d’unité).
     *
     * @return nombre d’alliés détectés
     */
    public int getNbrAllies() { return 0; }

    /**
     * Comportement lorsque le PNJ meurt.
     * Détruit l'objet, ajoute un effet visuel d'explosion de fumée,
     * puis transfère l’or du PNJ au joueur.
     */
    @Override
    public void onDeath()
    {
        super.onDeath();
        Animation A = Animation.get(this.getSprite().getAtlasName(), "walk_top");
        Effect deathEffect = new Effect(this.getSize(), new V2D(this.getPosition().x - (A.getFrameSize().x / 2), this.getPosition().y - (A.getFrameSize().y / 2)), new V2D(-1,-1), "smoke_explosion");
        Level.getActualRoom().addObject(deathEffect);
        SoundPlayer.playSFX("EnnemiDeath.wav");
        if (Player.getPlayer1() != null) Player.getPlayer1().addGold(this.getGold());
    }

    @Override 
    public void takeDamage(int Damage)
    {
        super.takeDamage(Damage);
        SoundPlayer.playSFX("EnnemyHurt.wav");
    }

    /**
     * Met à jour le PNJ à chaque frame.
     * Exécute l’intelligence artificielle si elle est définie.
     *
     * @param dt temps écoulé depuis la dernière mise à jour
     */
    @Override
    public void update(float dt)
    {
        super.update(dt);
        if (this.ai != null) ai.tick(this);
    }

    /**
     * Associe une intelligence artificielle à ce PNJ.
     *
     * @param ai instance d’IA à utiliser
     */
    public void setAi(AI ai) { this.ai = ai; }
}
