package Level;

import java.util.ArrayList;
import java.util.List;

import Objects.CObject;
import Objects.Collision;
import Objects.Door;
import Objects.NonPlayerCharacter;
import Objects.Player;
import Sound.SoundPlayer;
import UI.UI;
import Utilitary.V2D;

/**
 * Représente une salle abstraite d’un niveau.
 * <p>
 * Une salle gère :
 * <ul>
 *   <li>la liste de ses objets ({@link CObject}) et de leurs collisions associées,</li>
 *   <li>les ajouts / suppressions différés d’objets et de collisions,</li>
 *   <li>son état de « nettoyée » (tous les ennemis vaincus) et l’ouverture éventuelle des portes,</li>
 *   <li>la mise à jour logique des entités qu’elle contient et la détection des collisions.</li>
 * </ul>
 * Les implémentations concrètes (salles classiques, de boss, de marchand, etc.) héritent de cette classe.
*/
public abstract class Room
{
    private final List<Collision> collisions = new ArrayList<>();
    private final List<Collision> collisionsToRemove = new ArrayList<>();
    private final List<Collision> collisionsToAdd = new ArrayList<>();
    
    private final List<CObject> objects = new ArrayList<>();
    private final List<CObject> objectsToRemove = new ArrayList<>();
    private final List<CObject> objectsToAdd = new ArrayList<>();

    private boolean bCleared = false;
    private boolean bDoorOpened = false;

    private int powerUpTick = 60;

    public Room()
    {
        final int mapWidth = 1024;
        final int mapHeight = 744;
        final int offsetX = 415;
        final int offsetY = 108;

        this.collisionsToAdd.add(new Collision(new V2D(mapWidth + offsetX * 2, offsetY), new V2D(0,0), null, true));
        this.collisionsToAdd.add(new Collision(new V2D(mapWidth + offsetX * 2, offsetY * 2), new V2D(0,mapHeight + offsetY - 10), null, true));
        this.collisionsToAdd.add(new Collision(new V2D(offsetX, mapHeight + offsetY * 2), new V2D(0,0), null, true));
        this.collisionsToAdd.add(new Collision(new V2D(offsetX, mapHeight + offsetY * 2), new V2D(mapWidth + offsetX,0), null, true));
    }

    /**
     * Met à jour l’état de la salle pour une frame de jeu.
     * <p>
     * La méthode :
     * <ul>
     *   <li>applique les suppressions d’objets et de collisions en attente,</li>
     *   <li>applique les ajouts d’objets et de collisions en attente,</li>
     *   <li>met à jour chaque objet de la salle,</li>
     *   <li>détecte la présence d’ennemis encore en vie,</li>
     *   <li>ouvre les portes lorsque la salle est marquée comme nettoyée,</li>
     *   <li>marque la salle comme nettoyée quand tous les ennemis sont vaincus,</li>
     *   <li>déclenche la logique de progression (boss, effets d’équipements, etc.),</li>
     *   <li>et lance enfin la détection des collisions pour ce tick.</li>
     * </ul>
     *
     * @param dt temps écoulé depuis la dernière mise à jour (delta time)
    */
    public void update(float dt)
    {
        for (CObject o : this.objectsToRemove) this.objects.remove(o);
        Collision.removeCollisions();
        objectsToRemove.clear();
        collisionsToRemove.clear();

        for (CObject o : this.objectsToAdd) if (!this.objects.contains(o)) this.objects.add(o);
        for (Collision c : this.collisionsToAdd) if (!this.collisions.contains(c)) this.collisions.add(c);
        objectsToAdd.clear();
        collisionsToAdd.clear();

        boolean npcAlive = false;
        boolean opening = false;
        for (CObject o : this.objects)
        {
            if (o instanceof NonPlayerCharacter) npcAlive = true;
            if (!this.bDoorOpened && this.bCleared && o instanceof Door d)
            {
                opening = true;
                d.open();
            }
            if (o != null) o.update(dt);
        }
        if (this.powerUpTick < 60 && this.powerUpTick > 0) this.powerUpTick--;
        else if (this.powerUpTick == 0)
        {
            UI.ActualState = UI.EGameState.PowerUp;
            this.setbCleared(true);
            Level.decRoomsBeforeBoss();
            if (this instanceof BossRoom br)
            {
                Player.getPlayer1().getCaracteristics().setActHP(Player.getPlayer1().getCaracteristics().getMaxHP());
                br.openLevelEnd();
                SoundPlayer.stopMusic();
            }
            this.powerUpTick = -1;
            Level.setNbrRoomCleaned(Level.getNbrRoomCleaned() + 1);
        }
        if (opening) this.bDoorOpened = true;
        if (!npcAlive && !this.bCleared && this.powerUpTick == 60)
        {
            if (!(this instanceof MerchantRoom)) this.powerUpTick--;
            else
            {
                Level.decRoomsBeforeBoss();
                this.setbCleared(true);
            }
            Player.getPlayer1().getCaracteristics().setActArmor(Player.getPlayer1().getCaracteristics().getMaxArmor());
            Player.getPlayer1().equipmentEffect("roomCleared");
        }
        Collision.checkCollisions();
    }

    /**
     * Programme l’ajout d’un objet dans la salle.
     * <p>
     * Si l’objet possède une collision, celle-ci est également ajoutée dans la liste
     * des collisions à insérer. L’ajout effectif a lieu au début du prochain appel
     * à {@link #update(float)} afin d’éviter les modifications de listes en cours d’itération.
     *
     * @param o objet à ajouter à la salle, ignoré s’il est {@code null} ou déjà présent
    */
    public void addObject(CObject o)
    {
        if (o != null && !objects.contains(o))
        {
            if (o.getCollision() != null) collisionsToAdd.add(o.getCollision());
            objectsToAdd.add(o);
        }
    }

    /**
    * Programme la suppression d’un objet de la salle.
    * <p>
    * L’objet ainsi que sa collision (si elle existe) sont marqués pour suppression
    * et seront retirés au début du prochain appel à {@link #update(float)}.
    *
    * @param o objet à retirer de la salle, ignoré s’il est {@code null}
    */
    public void removeObject(CObject o)
    {
        if (o != null && objects.contains(o)) objectsToRemove.add(o);
        if (o != null && o.getCollision() != null && collisions.contains(o.getCollision())) collisionsToRemove.add(o.getCollision());
    }

    /**
     * Retourne la liste des objets actuellement présents dans la salle.
     *
     * @return la liste interne des {@link CObject} contenus dans la salle
    */
    public List<CObject> getObjects() {
        return this.objects;
    }

    /**
     * Retourne la liste des collisions actuellement actives dans la salle.
     *
     * @return la liste interne des {@link Collision} gérées par cette salle
    */
    public List<Collision> getCollisions() {
        return this.collisions;
    }
    
    /**
     * Retourne la liste des collisions marquées pour suppression.
     * <p>
     * Cette liste est principalement utilisée par le système de collisions
     * pour nettoyer les collisions obsolètes.
     *
     * @return la liste des collisions à retirer
    */
    public List<Collision> getCollisionsToRemove() {
        return this.collisionsToRemove;
    }

    /**
     * Retourne la liste des collisions marquées pour ajout.
     * <p>
     * Cette liste est nettoyée et appliquée au début de {@link #update(float)}.
     *
     * @return la liste des collisions à ajouter
    */
    public List<Collision> getCollisionsToAdd() {
        return this.collisionsToAdd;
    }

    /**
     * Indique si la salle a été nettoyée de tous ses ennemis.
     *
     * @return {@code true} si tous les ennemis ont été vaincus et que la salle est considérée comme nettoyée, {@code false} sinon
    */
    public boolean isbCleared() {
        return this.bCleared;
    }

    
    /**
     * Définit l’état « nettoyé » de la salle.
     *
     * @param bCleared {@code true} pour marquer la salle comme nettoyée, {@code false} sinon
    */
    public void setbCleared(boolean bCleared) {
        this.bCleared = bCleared;
    }
}