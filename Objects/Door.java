package Objects;

import Level.BossRoom;
import Level.Level;
import Level.MerchantRoom;
import Sound.SoundPlayer;
import UI.UI;
import Utilitary.V2D;

/**
 * Représente une porte reliant deux salles du niveau.
 *  
 * Une porte est un objet interactif et bloquant tant qu’elle n’est pas ouverte.
 * Elle possède :
 * <ul>
 *   <li>une destination (coordonnées du prochain étage/salle),</li>
 *   <li>une hitbox de transition générée dynamiquement lors de l’ouverture,</li>
 *   <li>une animation d’ouverture définie par « door » puis « door_opened ».</li>
 * </ul>
 *
 * Lorsqu’un joueur entre en collision avec elle, la porte téléporte ce dernier
 * dans la salle correspondante.
 */
public class Door extends Interactiv
{
    /** Position X de la salle vers laquelle mène cette porte. */
    private int nextPosX = 0;

    /** Position Y de la salle vers laquelle mène cette porte. */
    private int nextPosY = 0;

    /** Collision générée afin de détecter l’entrée dans la salle suivante. */
    private Collision nextRoomCollision = null;
    
    /**
     * Crée une porte statique, bloquante, sans animation.
     *
     * @param size          taille visuelle
     * @param position      position de la porte
     * @param collisionSize taille de la zone de collision
     * @param spriteName    nom du sprite
     */
    public Door(V2D size, V2D position, V2D collisionSize, String spriteName)
    {
        super(size, position, collisionSize, spriteName);
        this.getCollision().setBlocking(true);
        this.getCollision().setObject(null);
    }

    /**
     * Crée une porte bloquante dotée d’une animation.
     *
     * @param size          taille visuelle
     * @param position      position
     * @param collisionSize hitbox
     * @param spriteName    sprite utilisé
     * @param animName      animation de la porte
     */
    public Door(V2D size, V2D position, V2D collisionSize, String spriteName, String animName)
    {
        super(size, position, collisionSize, spriteName, animName);
        this.getCollision().setBlocking(true);
        this.getCollision().setObject(null);
    }

    /**
     * Crée une porte animée avec une destination prédéfinie.
     *
     * @param size          taille visuelle
     * @param position      position de la porte
     * @param collisionSize taille de la hitbox
     * @param spriteName    nom du sprite
     * @param animName      animation associée
     * @param nextPosX      position X de la prochaine salle
     * @param nextPosY      position Y de la prochaine salle
     */
    public Door(V2D size, V2D position, V2D collisionSize, String spriteName, String animName, int nextPosX, int nextPosY)
    {
        super(size, position, collisionSize, spriteName, animName);
        this.getCollision().setBlocking(true);
        this.getCollision().setObject(null);
        this.nextPosX = nextPosX;
        this.nextPosY = nextPosY;
    }

    /**
     * Déclenché lorsqu’un objet entre en collision avec la porte.
     *  
     * Si cet objet est un {@link Player}, il est téléporté dans la salle
     * correspondante, son sprite repositionné selon la direction d’entrée,
     * puis ajouté à la salle de destination.
     */
    @Override
    public void onCollisionBegin(CObject o)
    {
        if (o instanceof Player p)
        {
            if (this.nextPosX < Level.getPosX()) p.addX(825.f / p.getCaracteristics().getSpeed());
            else if (this.nextPosX > Level.getPosX()) p.addX(-825.f / p.getCaracteristics().getSpeed());
            else if (this.nextPosY < Level.getPosY()) p.addY(450.f / p.getCaracteristics().getSpeed());
            else if (this.nextPosY > Level.getPosY()) p.addY(-450.f / p.getCaracteristics().getSpeed());

            Level.setPosX(nextPosX);
            Level.setPosY(nextPosY);
            Level.getActualRoom().addObject(o);
            if (Level.getActualRoom() instanceof BossRoom) UI.ActualState = UI.EGameState.InGame;
            this.getCollision().getColliding().clear();

            if (Level.getActualRoom() instanceof MerchantRoom)
            {
                SoundPlayer.playMusicLoop("MerchantBreath.wav");
            }
            else if (Level.getActualRoom() instanceof BossRoom)
            {
                SoundPlayer.playMusicLoop("BossFight.wav");
            }
            else {
                SoundPlayer.stopMusic();
            }

            

        }
    }

    /**
     * Appelé lorsque l’animation en cours se termine.
     *  
     * Une porte une fois ouverte passe à l’animation « door_opened ».
     */
    @Override
    public void onAnimationEnd()
    {
        if (this.getSprite().getAnimName().equals("door")) this.getSprite().setBaseAnimName("door_opened");
        else this.getSprite().setBaseAnimName("bossdoor_opened");
    }

    /**
     * Met à jour l’animation de la porte.
     *  
     * La porte n’a pas de logique dynamique autre que l’animation.
     */
    @Override
    public void update(float dt)
    {
        if (this != null) this.getSprite().updateAnim(dt);
    };

    /**
     * Ouvre la porte :
     * <ul>
     *   <li>Si elle est fermée, lance l’animation « door »</li>
     *   <li>Crée une hitbox secondaire permettant d’entrer dans la salle suivante</li>
     *   <li>Ajoute cette hitbox à la liste des collisions à insérer</li>
     * </ul>
     *
     * La position et la taille de la hitbox sont calculées selon la direction
     * entre la salle actuelle et la salle de destination.
     */
    public void open()
    {
        if (this.getSprite().getAnimName().equals("door_closed")) this.getSprite().setAnimName("door");
        else if (this.getSprite().getAnimName().equals("bossdoor_closed")) this.getSprite().setAnimName("bossdoor");

        V2D collSize = new V2D(0.0, 0.0);
        V2D collPos = new V2D(0.0, 0.0);

        if (this.nextPosX < Level.getPosX())
        {
            collSize = new V2D(20.0, this.getCollision().getSize().y);
            collPos = new V2D(this.getPosition().x + this.getCollision().getSize().x, this.getPosition().y);
        }
        else if (this.nextPosX > Level.getPosX())
        {
            collSize = new V2D(20.0, this.getCollision().getSize().y);
            collPos = new V2D(this.getPosition().x - 20.0, this.getPosition().y);
        }
        else if (this.nextPosY < Level.getPosY())
        {
            collSize = new V2D(this.getCollision().getSize().x, 20.0);
            collPos = new V2D(this.getPosition().x, this.getPosition().y + this.getCollision().getSize().y);
        }
        else if (this.nextPosY > Level.getPosY())
        {
            collSize = new V2D(this.getCollision().getSize().x, 20.0);
            collPos = new V2D(this.getPosition().x, this.getPosition().y - 20.0);
        }

        this.nextRoomCollision = new Collision(collSize, collPos, this);
        Level.getActualRoom().getCollisionsToAdd().add(this.nextRoomCollision);
    }
}
