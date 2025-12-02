package UI;

import java.awt.Color;

import Level.Level;
import Objects.CObject;
import Objects.Merchant;
import Objects.Player;
import Sound.SoundPlayer;
import Utilitary.V2D;

public class UIMerchant extends UI {

    public Merchant m = null;

    public UIMerchant(){

        //Interface
        this.elements.add(new UIElement(new V2D(470, 100), new V2D(8, 7), "market_interface", false));
        this.elements.add(new UIElement(new V2D(510, 165), new V2D(1.25, 1.25), "LES BONS PLANS DU MARCHAND", true, Color.decode("#42271C")));

        //Croix de fermeture
        this.elements.add(new UIButton(new V2D(1295, 125), new V2D(4, 4), "close_cross", ()-> { UI.ActualState = UI.EGameState.InGame; }));

        //Items + cadres
        this.elements.add(new UIButton(new V2D(570, 250), new V2D(7, 7), "market_items_placeholder", ()-> { UI.ActualState = UI.EGameState.InGame; }));
        this.elements.add(new UIElement(new V2D(585, 265), new V2D(4, 4), "bracelet", false));
        this.elements.add(new UIElement(new V2D(745, 315), new V2D(1.25, 1.25), "ITEM NAME", true, Color.decode("#5A4021")));
        this.elements.add(new UIElement(new V2D(745, 355), new V2D(1, 1), "item description", true, Color.decode("#8E7352")));
        this.elements.add(new UIElement(new V2D(745, 385), new V2D(1, 1), "item cost", true, Color.decode("#8E7352")));
        this.elements.add(new UIButton(new V2D(570, 450), new V2D(7, 7), "market_items_placeholder", ()-> { UI.ActualState = UI.EGameState.InGame; }));
        this.elements.add(new UIElement(new V2D(600, 465), new V2D(4, 4), "book", false));
        this.elements.add(new UIElement(new V2D(745, 515), new V2D(1.25, 1.25), "ITEM NAME", true, Color.decode("#5A4021")));
        this.elements.add(new UIElement(new V2D(745, 555), new V2D(1, 1), "item description", true, Color.decode("#8E7352")));
        this.elements.add(new UIElement(new V2D(745, 585), new V2D(1, 1), "item cost", true, Color.decode("#8E7352")));
        this.elements.add(new UIButton(new V2D(570, 650), new V2D(7, 7), "market_items_placeholder", ()-> { UI.ActualState = UI.EGameState.InGame; }));
        this.elements.add(new UIElement(new V2D(585, 665), new V2D(4, 4), "ring", false));
        this.elements.add(new UIElement(new V2D(745, 715), new V2D(1.25, 1.25), "ITEM NAME", true, Color.decode("#5A4021")));
        this.elements.add(new UIElement(new V2D(745, 755), new V2D(1, 1), "item description", true, Color.decode("#8E7352")));
        this.elements.add(new UIElement(new V2D(745, 785), new V2D(1, 1), "item cost", true, Color.decode("#8E7352")));
    }
  
    @Override
    public void update(float dt)
    {
        if (this.m == null)
        {
            for (CObject o : Level.getActualRoom().getObjects())
            {
                if (o instanceof Merchant m2)
                {
                    this.m = m2;
                    break;
                }
            }
        }
        if (this.m == null) return;

        this.elements.clear();

        //Interface
        this.elements.add(new UIElement(new V2D(470, 100), new V2D(8, 7), "market_interface", false));
        this.elements.add(new UIElement(new V2D(510, 165), new V2D(1.25, 1.25), "LES BONS PLANS DU MARCHAND", true, Color.decode("#42271C")));

        // -----------------------------
        // Coins
        // -----------------------------
        this.elements.add(new UIElement(new V2D(50, 90), new V2D(2, 2), "coin", false));
        this.elements.add(new UIElement(
                new V2D(90, 120),
                new V2D(1, 1),
                "x" + Player.getPlayer1().getGold(),
                true
        ));

        //Croix de fermeture
        this.elements.add(new UIButton(new V2D(1295, 125), new V2D(4, 4), "close_cross", ()-> { UI.ActualState = UI.EGameState.InGame; }));

        //Items + cadres
        for (int i = 0; i < m.getEquipments().size(); i++)
        {
            final int id = i;

            this.elements.add(new UIButton(new V2D(570, 250 + i * 200), new V2D(7, 7), "market_items_placeholder", ()-> { this.buy(id); }));
            this.elements.add(new UIElement(new V2D(585, 265 + i * 200), new V2D(4, 4), m.getEquipments().get(i).getType().name().toLowerCase(), false));
            this.elements.add(new UIElement(new V2D(745, 305 + i * 200), new V2D(1, 1.25), m.getEquipments().get(i).getName(), true, Color.decode("#5A4021")));
            this.elements.add(new UIElement(new V2D(745, 345 + i * 200), new V2D(0.7, 1), m.getEquipments().get(i).getDescription(), true, Color.decode("#8E7352")));
            this.elements.add(new UIElement(new V2D(745, 375 + i * 200), new V2D(0.7, 1), Integer.toString(m.getEquipments().get(i).getPrice()), true, Color.decode("#8E7352")));
            this.elements.add(new UIElement(new V2D(790, 350 + i * 200), new V2D(2, 2), "static_coin", false));

        }
    }

    private void buy(int id)
    {
        if (Player.getPlayer1().getGold() < m.getEquipments().get(id).getPrice()) return;
        Player.getPlayer1().setGold(Player.getPlayer1().getGold() - m.getEquipments().get(id).getPrice());
        Player.getPlayer1().equip(m.getEquipments().get(id));
        this.m.getEquipments().remove(id);
        UI.ActualState = UI.EGameState.InGame;
        SoundPlayer.playSFX("Buy.wav");
    }
}
