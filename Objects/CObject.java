package Objects;

import Graphism.ObjectSprite;
import Utilitary.V2D;

/**
 * Représente la classe de base de tous les objets du jeu.
 * Un {@code CObject} possède une taille, une position, un sprite et éventuellement
 * une zone de collision. Cette classe fournit l’ossature que les objets dérivés pourront
 * spécialiser (acteurs, projectiles, éléments interactifs, etc.).
 */
public abstract class CObject
{
    /** Taille visuelle de l’objet. */
    private V2D size = new V2D(0.0,0.0);

    /** Position absolue de l’objet dans le monde. */
    private V2D position = new V2D(0.0,0.0);

    /** Zone de collision optionnelle associée à l’objet. */
    private Collision collision = null;

    /** Sprite représentant l’objet à l’écran. */
    private ObjectSprite sprite = null;

    /**
     * Construit un objet disposant d’un sprite et d’une animation, mais sans collision.
     *
     * @param size       dimensions visuelles
     * @param position   position initiale
     * @param spriteName nom du sprite utilisé
     * @param animName   nom de l’animation à jouer
     */
    public CObject(V2D size, V2D position, String spriteName, String animName)
    {
        this.size = size;
        this.position = position;
        this.sprite = new ObjectSprite(spriteName, size, this, animName);
    }

    /**
     * Construit un objet avec une zone de collision et un sprite sans animation.
     * La taille de la collision est multipliée par la taille visuelle pour obtenir
     * une échelle cohérente.
     *
     * @param size          dimensions visuelles
     * @param position      position initiale
     * @param collisionSize dimensions de la zone de collision (avant mise à l’échelle)
     * @param spriteName    nom du sprite utilisé
     */
    public CObject(V2D size, V2D position, V2D collisionSize, String spriteName)
    {
        this.size = size;
        this.position = position;
        collisionSize.mult(size);
        this.collision = new Collision(collisionSize, position, this);
        this.sprite = new ObjectSprite(spriteName, size, this);
    }

    /**
     * Construit un objet avec zone de collision, sprite et animation.
     * La zone de collision est ajustée automatiquement à l’échelle de l’objet.
     *
     * @param size          dimensions visuelles
     * @param position      position initiale
     * @param collisionSize dimensions de collision (avant mise à l’échelle)
     * @param spriteName    nom du sprite
     * @param animName      nom de l’animation
     */
    public CObject(V2D size, V2D position, V2D collisionSize, String spriteName, String animName)
    {
        this.size = size;
        this.position = position;
        collisionSize.mult(size);
        this.collision = new Collision(collisionSize, position, this);
        this.sprite = new ObjectSprite(spriteName, size, this, animName);
    }

    /**
     * Appelé lors du début d’une collision avec un autre objet.
     *
     * @param o objet entrant en collision
     */
    public void onCollisionBegin(CObject o) {};

    /**
     * Appelé lorsque la collision se termine.
     *
     * @param o objet sortant de la collision
     */
    public void onCollisionEnd(CObject o) {};

    /**
     * Appelé au début d’une animation du sprite.
     */
    public void onAnimationBegin() {};

    /**
     * Appelé à la fin d’une animation du sprite.
     */
    public void onAnimationEnd() {};

    /** @return la taille de l’objet */
    public V2D getSize() {
        return this.size;
    }

    /** @return la position actuelle de l’objet */
    public V2D getPosition() {
        return this.position;
    }

    /**
     * Définit une nouvelle position pour l’objet.
     *
     * @param position nouvelle position
     */
    public void setPosition(V2D position) { this.position = position; }

    /** @return la zone de collision associée, ou {@code null} si absente */
    public Collision getCollision() {
        return this.collision;
    }

    /**
     * Déplace l’objet sur l’axe X et met à jour la collision si elle existe.
     *
     * @param x delta à ajouter sur l’axe X
     */
    public void addX(double x) { position.x += x; this.collision.addX(x); }

    /**
     * Déplace l’objet sur l’axe Y et met à jour la collision si elle existe.
     *
     * @param y delta à ajouter sur l’axe Y
     */
    public void addY(double y) { position.y += y; this.collision.addY(y); }

    /**
     * Modifie la taille visuelle de l’objet.
     *
     * @param size nouvelle taille
     */
    public void setSize(V2D size) { this.size = size; }

    /** @return le sprite associé à l’objet */
    public ObjectSprite getSprite() {
        return this.sprite;
    }

    /**
     * Méthode de mise à jour appelée à chaque frame.
     * Les classes dérivées doivent redéfinir cette méthode pour implémenter
     * un comportement propre.
     *
     * @param dt temps écoulé depuis la dernière mise à jour (delta time)
     */
    public void update(float dt) {};
}
