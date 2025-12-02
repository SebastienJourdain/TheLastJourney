package Objects;

import java.util.ArrayList;
import java.util.List;

import Level.Level;
import Utilitary.V2D;

/**
 * Gère la collision d’un objet du jeu.
 * Une collision peut être rectangulaire ou circulaire, bloquante ou non,
 * active ou désactivée.  
 *  
 * La classe fournit :
 *  - des tests de collisions rectangle/cercle et cercle/cercle,  
 *  - un suivi des collisions en cours et terminées,  
 *  - un système de suppression différée,  
 *  - un outil de projection (« dash ») utilisant une recherche dichotomique.  
 */
public class Collision
{
    /** Dimensions de la hitbox si rectangulaire. */
    private V2D size = new V2D(0.0,0.0);

    /** Position de la hitbox (coin supérieur-gauche ou centre si cercle). */
    private V2D position = new V2D(0.0,0.0);

    /** Rayon si la hitbox est circulaire. */
    private float r = 0.f;

    /** Objet auquel cette collision appartient. */
    private CObject object;

    /** Indique si cet objet bloque les déplacements. */
    private boolean bBlocking = false;

    /** Indique si la hitbox est active. */
    private boolean bActive = true;

    /** Type de hitbox : cercle si vrai, rectangle sinon. */
    private boolean bCircle = false;

    /** Collisions en cours pendant ce frame. */
    private final List<Collision> colliding = new ArrayList<>();

    /** Collisions du frame précédent. */
    private List<Collision> oldColliding = new ArrayList<>();

    /**
     * Constructeur pour hitbox circulaire.
     *
     * @param r       rayon du cercle
     * @param position position du centre
     * @param object   objet possédant cette collision
     */
    public Collision(float r, V2D position, CObject object)
    {
        this.r = r;
        this.position = position;
        this.object = object;
        this.bCircle = true;
    }

    /**
     * Constructeur pour hitbox rectangulaire.
     *
     * @param size     taille du rectangle
     * @param position position du rectangle
     * @param object   objet possédant cette collision
     */
    public Collision(V2D size, V2D position, CObject object)
    {
        this.size = size;
        this.position = position;
        this.object = object;
    }

    /**
     * Constructeur permettant de définir si la hitbox est bloquante.
     *
     * @param size       dimensions
     * @param position   position
     * @param object     objet parent
     * @param bBlocking  indique si la collision bloque le déplacement
     */
    public Collision(V2D size, V2D position, CObject object, boolean bBlocking)
    {
        this.size = size;
        this.position = position;
        this.object = object;
        this.bBlocking = bBlocking;
    }

    /**
     * Marque cette collision pour suppression.
     * La suppression réelle est effectuée par {@link #removeCollisions()}.
     */
    public void destroy()
    {
        if (!Level.getActualRoom().getCollisionsToRemove().contains(this))
            Level.getActualRoom().getCollisionsToRemove().add(this);
    }

    /** Déplace la collision sur l’axe X. */
    public void addX(double x) { position.x += x; }
    /** Déplace la collision sur l’axe Y. */
    public void addY(double y) { position.y += y; }

    /** Définit si la collision est bloquante. */
    public void setBlocking(boolean bBlocking) { this.bBlocking = bBlocking; }

    /** Modifie la position de la hitbox. */
    public void setPosition(V2D position) { this.position = position; }

    /** @return dimensions rectangulaires (null si cercle). */
    public V2D getSize() { return this.size; }

    /** @return position de la hitbox. */
    public V2D getPosition() { return this.position; }

    /** @return la liste des collisions en cours. */
    public List<Collision> getColliding() { return this.colliding; }

    /** Définit la taille rectangulaire. */
    public void setSize(V2D size) { this.size = size; }

    /**
     * Teste la collision entre un cercle et un rectangle.
     * @return vrai si les deux formes se chevauchent
     */
    private static boolean circleRectCollision(Collision circle, Collision rect)
    {
        double circleX = circle.getPosition().x;
        double circleY = circle.getPosition().y;

        double rectX = rect.getPosition().x;
        double rectY = rect.getPosition().y;
        double rectW = rect.getSize().x;
        double rectH = rect.getSize().y;

        double closestX = clamp(circleX, rectX, rectX + rectW);
        double closestY = clamp(circleY, rectY, rectY + rectH);

        double dx = circleX - closestX;
        double dy = circleY - closestY;

        return dx * dx + dy * dy <= circle.r * circle.r;
    }

    /** Fonction utilitaire limitant une valeur dans un intervalle. */
    private static double clamp(double value, double min, double max)
    {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    /**
     * Teste une collision entre deux hitbox, qu’elles soient rectangulaires ou circulaires.
     *
     * @return vrai s'il y a collision
     */
    private static boolean checkCollision(Collision a, Collision b)
    {
        boolean collide;
        if (!a.isbCircle() && !b.isbCircle())
            collide = a.getPosition().x < b.getPosition().x + b.getSize().x &&
                      a.getPosition().x + a.getSize().x > b.getPosition().x &&
                      a.getPosition().y < b.getPosition().y + b.getSize().y &&
                      a.getPosition().y + a.getSize().y > b.getPosition().y;

        else if (a.isbCircle() && !b.isbCircle())
            collide = circleRectCollision(a, b);

        else if (!a.isbCircle() && b.isbCircle())
            collide = circleRectCollision(b, a);

        else
        {
            double dx = a.getPosition().x - b.getPosition().x;
            double dy = a.getPosition().y - b.getPosition().y;
            double distSq = dx * dx + dy * dy;
            double radiusSum = a.r + b.r;
            collide = distSq <= radiusSum * radiusSum;
        }
        return collide;
    }

    /**
     * Vérifie si un déplacement prévu est bloqué par un obstacle.
     *
     * @param x déplacement en X
     * @param y déplacement en Y
     * @return vrai si un obstacle bloquant sera touché
     */
    public boolean forwardBlockCheck(double x, double y)
    {
        for (Collision b : Level.getActualRoom().getCollisions())
        {
            if (b != this && b.bBlocking && b.bActive)
            {
                boolean collide =   this.getPosition().x + x < b.getPosition().x + b.getSize().x &&
                                    this.getPosition().x + x + this.getSize().x > b.getPosition().x &&
                                    this.getPosition().y + y < b.getPosition().y + b.getSize().y &&
                                    this.getPosition().y + y + this.getSize().y > b.getPosition().y;
                if (collide) return true;
            }
        }
        return false;
    }

    /**
     * Calcule la distance maximale d’un dash dans une direction donnée
     * sans traverser d’obstacle.  
     *  
     * Utilise une recherche dichotomique décroissante.
     *
     * @param dir direction normalisée
     * @param maxRange distance maximale testée
     * @return un vecteur correspondant au déplacement sûr maximal
     */
    public V2D getLongestDash(V2D dir, double maxRange)
    {
        if ((dir.x == 0.0 && dir.y == 0.0) || maxRange <= 0.0)
            return new V2D(this.position.x, this.position.y);

        double high = maxRange;

        while (high > 1.0)
        {
            double testX = dir.x * high;
            double testY = dir.y * high;

            if (forwardBlockCheck(testX, testY)) high -= high / 4;
            else break;
        }

        double finalDist = high;
        if (high <= 1.0) return new V2D(this.position);

        return new V2D(dir.x * finalDist, dir.y * finalDist);
    }

    /**
     * Met à jour la liste des collisions pour tous les objets de la salle.
     * Gère les événements d'entrée et de sortie de collision.
     */
    public static void checkCollisions()
    {
        for (Collision c : Level.getActualRoom().getCollisions())
        {
            c.oldColliding = new ArrayList<>(c.colliding);
            c.colliding.clear();
        }

        for (int i = 0; i < Level.getActualRoom().getCollisions().size() - 1; i++)
        {
            Collision a = Level.getActualRoom().getCollisions().get(i);
            if (!a.bActive || a.getSize().x == -1) continue;

            for (int j = i + 1; j < Level.getActualRoom().getCollisions().size(); j++)
            {
                Collision b = Level.getActualRoom().getCollisions().get(j);

                if (!b.bActive || b.getSize().x == -1) continue;
                
                boolean collide = checkCollision(a, b);

                if (collide)
                {
                    if (!a.oldColliding.contains(b) && a.object != null) a.object.onCollisionBegin(b.object);
                    a.colliding.add(b);
                    if (!b.oldColliding.contains(a) && b.object != null) b.object.onCollisionBegin(a.object);
                    b.colliding.add(a);
                }
            }
        }

        for (Collision c : Level.getActualRoom().getCollisions())
        {
            for (Collision c2 : c.oldColliding)
            {
                if (!c.colliding.contains(c2) && c.object != null) c.object.onCollisionEnd(c2.object);
            }
        }
    }

    /**
     * Supprime toutes les collisions marquées par {@link #destroy()}.
     * Déclenche les événements de fin de collision correspondants.
     */
    public static void removeCollisions()
    {
        for (Collision c : Level.getActualRoom().getCollisionsToRemove())
        {
            for (Collision c2 : c.colliding)
            {
                if (c2.object != null) c2.object.onCollisionEnd(c.object);
                if (c.object != null) c.object.onCollisionEnd(c2.object);
            }
            Level.getActualRoom().getCollisions().remove(c);
        }

        Level.getActualRoom().getCollisionsToRemove().clear();
    }

    /** Active ou désactive la hitbox. */
    public void setbActive(boolean bActive) { this.bActive = bActive; }

    /** @return vrai si la collision est active. */
    public boolean isbActive() { return this.bActive; }

    /** @return vrai si la hitbox est un cercle. */
    public boolean isbCircle() { return this.bCircle; }

    /** Définit si la hitbox est un cercle. */
    public void setbCircle(boolean bCircle) { this.bCircle = bCircle; }

    /** @return le rayon de la hitbox si circulaire. */
    public float getR() { return this.r; }

    public CObject getObject() { return this.object; }

    /** Modifie le rayon de la hitbox circulaire. */
    public void setR(float r) { this.r = r; }

    /** @return vrai si la collision est bloquante. */
    public boolean isbBlocking() { return this.bBlocking; }

    public void setObject(CObject object) { this.object = object; }
}
