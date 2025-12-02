package UI;

import Objects.Equipment;
import Objects.Mage;
import Objects.Player;
import Objects.Warrior;
import Utilitary.V2D;
import java.awt.Color;

public class Inventory extends UI {
        private int indexItem = 0;
        private String itemName = "";
        private String itemDesc = "";

        public Inventory(){

                //Fond
                this.elements.add(new UIElement(new V2D(160, 35), new V2D(2, 1.5), "inventory", false));
                this.elements.add(new UIElement(new V2D(490, 175), new V2D(2, 2), "HERO", true, Color.decode("#BF4040")));
                this.elements.add(new UIElement(new V2D(1050, 175), new V2D(2, 2), "INVENTAIRE", true, Color.decode("#BF4040")));

                //Gauche
                this.elements.add(new UIElement(new V2D(450, 200), new V2D(12, 12), "market_items_placeholder", false));
                this.elements.add(new UIElement(new V2D(490, 220), new V2D(5.5, 5.5), "Warrior", "buste_warrior"));
                this.elements.add(new UIElement(new V2D(350, 500), new V2D(1, 1), "Renaud le chevalier", true, Color.decode("#BF4040")));
                //this.elements.add(new UIElement(new V2D(370, 500), new V2D(1, 1), "Rogier le sorcier", true, Color.decode("#BF4040")));
                //this.elements.add(new UIElement(new V2D(370, 500), new V2D(1, 1), "Celinia l'archère", true, Color.decode("#BF4040")));
                //STATS
                //COLONNE DE GAUCHE VIE/ARMURE/COINS
                this.elements.add(new UIElement(new V2D(400, 515), new V2D(3, 3), "market_items_placeholder", false));
                        this.elements.add(new UIElement(new V2D(405, 521), new V2D(0.21, 0.21), "heart", false));
                        this.elements.add(new UIElement(new V2D(475, 560), new V2D(1.15, 1.15), "x000", true, Color.decode("#4C2D2F")));

                this.elements.add(new UIElement(new V2D(400, 585), new V2D(3, 3), "market_items_placeholder", false));
                        this.elements.add(new UIElement(new V2D(405, 591), new V2D(0.21, 0.21), "shield_heart", false));
                        this.elements.add(new UIElement(new V2D(475, 630), new V2D(1.15, 1.15), "x000", true, Color.decode("#4C2D2F")));

                this.elements.add(new UIElement(new V2D(400, 655), new V2D(3, 3), "market_items_placeholder", false));
                        this.elements.add(new UIElement(new V2D(406, 660), new V2D(3.5, 3.5), "static_coin", false));
                        this.elements.add(new UIElement(new V2D(475, 700), new V2D(1.15, 1.15), "x000", true, Color.decode("#4C2D2F")));

                //COLONNE DE DROITE CRIT/SPEED/DAMAGE
                this.elements.add(new UIElement(new V2D(600, 515), new V2D(2, 2), "damage_up", false));
                        this.elements.add(new UIElement(new V2D(675, 560), new V2D(1.15, 1.15), "100", true, Color.decode("#4C2D2F")));

                this.elements.add(new UIElement(new V2D(600, 585), new V2D(2, 2), "speed_up", false));
                        this.elements.add(new UIElement(new V2D(675, 630), new V2D(1.15, 1.15), "100%", true, Color.decode("#4C2D2F")));

                this.elements.add(new UIElement(new V2D(600, 655), new V2D(2, 2), "critical_up", false));
                        this.elements.add(new UIElement(new V2D(675, 700), new V2D(1.15, 1.15), "100%", true, Color.decode("#4C2D2F")));

                //Droite
                //Items
                this.elements.add(new UIButton(new V2D(975, 225), new V2D(9, 9), "market_items_placeholder", null));
                        this.elements.add(new UIElement(new V2D(998, 245), new V2D(5, 5), "bracelet", false));

                this.elements.add(new UIButton(new V2D(1175, 225), new V2D(9, 9), "market_items_placeholder", null));
                        this.elements.add(new UIElement(new V2D(1205, 240), new V2D(5, 5), "book", false));

                this.elements.add(new UIButton(new V2D(1375, 225), new V2D(9, 9), "market_items_placeholder", null));
                        this.elements.add(new UIElement(new V2D(1400, 247), new V2D(5, 5), "ring", false));

                //Flèches directionelles
                this.elements.add(new UIButton(new V2D(1375, 425), new V2D(2, 2), "left_arrow", null));
                this.elements.add(new UIElement(new V2D(1375, 425), new V2D(0.8, 0.8), "00/00", true, Color.decode("#BF4040")));
                this.elements.add(new UIButton(new V2D(1375, 425), new V2D(2, 2), "right_arrow", null));

                //Infos d'items
                this.elements.add(new UIElement(new V2D(975, 500), new V2D(1.25, 1.25), "ITEM NAME", true, Color.decode("#BF4040")));
                this.elements.add(new UIElement(new V2D(975, 550), new V2D(1.10, 1.10), "ITEM DESCRIPTION", true, Color.decode("#4C2D2F")));

        }
  
        @Override
        public void update(float dt)
        {
                Player p = Player.getPlayer1();

                this.elements.clear();

                //Fond
                this.elements.add(new UIElement(new V2D(160, 35), new V2D(2, 1.5), "inventory", false));
                this.elements.add(new UIElement(new V2D(490, 175), new V2D(2, 2), "HERO", true, Color.decode("#BF4040")));
                this.elements.add(new UIElement(new V2D(1050, 175), new V2D(2, 2), "INVENTAIRE", true, Color.decode("#BF4040")));

                //Gauche
                this.elements.add(new UIElement(new V2D(450, 200), new V2D(12, 12), "market_items_placeholder", false));
                if (p instanceof Warrior)
                {
                        this.elements.add(new UIElement(new V2D(490, 220), new V2D(5.5, 5.5), "Warrior", "buste_warrior"));
                        this.elements.add(new UIElement(new V2D(350, 500), new V2D(1, 1), "Renaud le chevalier", true, Color.decode("#BF4040")));
                }
                else if (p instanceof Mage)
                {
                        this.elements.add(new UIElement(new V2D(490, 287), new V2D(5.5, 5.5), "Mage", "buste_mage"));
                        this.elements.add(new UIElement(new V2D(370, 500), new V2D(1, 1), "Rogier le sorcier", true, Color.decode("#BF4040")));
                }
                else
                {
                        this.elements.add(new UIElement(new V2D(505, 287), new V2D(5.5, 5.5), "Archer", "buste_archer"));
                        this.elements.add(new UIElement(new V2D(370, 500), new V2D(1, 1), "Celinia l'archère", true, Color.decode("#BF4040")));
                }

                //STATS
                //COLONNE DE GAUCHE VIE/ARMURE/COINS
                this.elements.add(new UIElement(new V2D(400, 515), new V2D(3, 3), "market_items_placeholder", false));
                        this.elements.add(new UIElement(new V2D(405, 521), new V2D(0.21, 0.21), "heart", false));
                        this.elements.add(new UIElement(new V2D(475, 560), new V2D(1.15, 1.15), "x" + String.format("%03d", p.getCaracteristics().getMaxHP()), true, Color.decode("#4C2D2F")));

                this.elements.add(new UIElement(new V2D(400, 585), new V2D(3, 3), "market_items_placeholder", false));
                        this.elements.add(new UIElement(new V2D(405, 591), new V2D(0.21, 0.21), "shield_heart", false));
                        this.elements.add(new UIElement(new V2D(475, 630), new V2D(1.15, 1.15), "x" + String.format("%03d", p.getCaracteristics().getMaxArmor()), true, Color.decode("#4C2D2F")));

                this.elements.add(new UIElement(new V2D(400, 655), new V2D(3, 3), "market_items_placeholder", false));
                        this.elements.add(new UIElement(new V2D(406, 660), new V2D(3.5, 3.5), "static_coin", false));
                        this.elements.add(new UIElement(new V2D(475, 700), new V2D(1.15, 1.15), "x" + String.format("%03d", p.getGold()), true, Color.decode("#4C2D2F")));

                //COLONNE DE DROITE CRIT/SPEED/DAMAGE
                this.elements.add(new UIElement(new V2D(600, 515), new V2D(2, 2), "damage_up", false));
                        this.elements.add(new UIElement(new V2D(675, 560), new V2D(1.15, 1.15), Integer.toString(p.getCaracteristics().getDamage()), true, Color.decode("#4C2D2F")));

                this.elements.add(new UIElement(new V2D(600, 585), new V2D(2, 2), "speed_up", false));
                        this.elements.add(new UIElement(new V2D(675, 630), new V2D(1.15, 1.15), String.format("%.2f", p.getCaracteristics().getSpeed()), true, Color.decode("#4C2D2F")));

                this.elements.add(new UIElement(new V2D(600, 655), new V2D(2, 2), "critical_up", false));
                        this.elements.add(new UIElement(new V2D(675, 700), new V2D(1.15, 1.15), Integer.toString((int)(p.getCaracteristics().getCritical() * 100)), true, Color.decode("#4C2D2F")));

                //Droite
                //Items
                this.elements.add(new UIButton(new V2D(975, 235), new V2D(9, 9), "market_items_placeholder", () -> displayDesc(0)));

                this.elements.add(new UIButton(new V2D(1175, 235), new V2D(9, 9), "market_items_placeholder", () -> displayDesc(1)));

                this.elements.add(new UIButton(new V2D(1375, 235), new V2D(9, 9), "market_items_placeholder", () -> displayDesc(2)));

                for (int i = this.indexItem; i < indexItem + 3; i++)
                {
                        if (i >= p.getEquipments().size()) break;
                        Equipment e = p.getEquipments().get(i);
                        if (i == this.indexItem) this.elements.add(new UIElement(new V2D(1000, 255), new V2D(5, 5), e.getType().name().toLowerCase(), false));
                        else if (i == this.indexItem + 1) this.elements.add(new UIElement(new V2D(1200, 255), new V2D(5, 5), e.getType().name().toLowerCase(), false));
                        else if (i == this.indexItem + 2) this.elements.add(new UIElement(new V2D(1400, 255), new V2D(5, 5), e.getType().name().toLowerCase(), false));
                }

                //Flèches directionelles
                this.elements.add(new UIButton(new V2D(1350, 455), new V2D(2, 2), "right_arrow", () -> { incIndex();}));
                this.elements.add(new UIElement(new V2D(1230, 490), new V2D(0.8, 0.8), String.format("%02d", this.indexItem / 3) + "/" + String.format("%02d", (p.getEquipments().size() / 3)), true, Color.decode("#BF4040")));
                this.elements.add(new UIButton(new V2D(1150, 455), new V2D(2, 2), "left_arrow", () -> { decIndex();}));

                //Infos d'items
                this.elements.add(new UIElement(new V2D(975, 550), new V2D(1, 1), this.itemName, true, Color.decode("#BF4040")));
                this.elements.add(new UIElement(new V2D(975, 600), new V2D(0.8, 0.8), this.itemDesc, true, Color.decode("#4C2D2F")));
        }

        private void displayDesc(int id)
        {
                Player p = Player.getPlayer1();
                if (id + this.indexItem >= p.getEquipments().size()) return;

                this.itemName = p.getEquipments().get(id + this.indexItem).getName();
                this.itemDesc = p.getEquipments().get(id + this.indexItem).getDescription();
        }

        private void incIndex()
        {
                Player p = Player.getPlayer1();
                if (this.indexItem + 3 >= p.getEquipments().size()) return;
                this.indexItem += 3;
        }
        private void decIndex()
        {
                if (this.indexItem - 3 < 0) return;
                this.indexItem -= 3;
        }
}
