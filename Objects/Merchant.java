package Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import UI.UI;
import UI.UIElement;
import Utilitary.Gamepad;
import Utilitary.V2D;

/**
 * Représente un marchand présent dans l’environnement.
 *  
 * Ce personnage interactif ne se déplace pas, ne possède pas d’IA
 * et sert généralement de point d’achat, d’échange ou de dialogue.
 *  
 * Le marchand joue simplement son animation d’attente (« idle »).
 */
public class Merchant extends Interactiv
{
    private final List<Equipment> equipments = new ArrayList<>();
    private final UIElement popup;
    /**
     * Crée un marchand avec une hitbox personnalisée.
     * L’animation par défaut est définie sur « idle ».
     *
     * @param size          taille visuelle du marchand
     * @param position      position dans la salle
     * @param collisionSize dimensions de la zone de collision
     */
    public Merchant(V2D size, V2D position, V2D collisionSize)
    {
        super(size, position, new V2D(90, 60), "Merchant");
        this.getSprite().setAnimName("idle");
        this.getCollision().setPosition(new V2D(position.x - 20, position.y + 45));
        Random rand = new Random();
        for (int i = 0; i < 3; i++)
        {
            int limit = 0;

            while (equipments.size() < i + 1 && limit < 100)
            {
                Equipment e = new Equipment(Equipment.getEquipmentsNames().get(rand.nextInt(Equipment.getEquipmentsNames().size())));
                if (e.getPrice() <= (i + 1) * 20 && e.getPrice() > (i * 20))
                {
                    boolean alreadyBought = false;
                    for (Equipment e2 : Player.getPlayer1().getEquipments())
                    {
                        if (e2.getName().equals(e.getName()))
                        {
                            alreadyBought = true;
                            break;
                        }
                    }
                    if (!alreadyBought)
                    {
                        equipments.add(e);
                        break;
                    }
                }
                limit++;
            }
        }
        this.popup = new UIElement(new V2D(575, 950), new V2D(1,1), "Appuyer sur " + (Gamepad.gamepad.isConnected() ? "B" : "E") + " pour marchander", true);
    }

    /**
     * Met à jour l’animation du marchand.
     *  
     * Le marchand ne se déplaçant pas, seule son animation est rafraîchie.
     *
     * @param dt temps écoulé depuis la dernière mise à jour
     */
    @Override
    public void update(float dt)
    {
        if (this != null) this.getSprite().updateAnim(dt);
    }

    @Override
    public void onCollisionBegin(CObject o)
    {
        if (o instanceof Player) UI.UIs.get(UI.ActualState).getElements().add(this.popup);
    }

    @Override
    public void onCollisionEnd(CObject o)
    {
        if (o instanceof Player) UI.UIs.get(UI.ActualState).getElements().remove(this.popup);
    }

    public List<Equipment> getEquipments() { return this.equipments; }
}