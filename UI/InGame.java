package UI;

import java.awt.Color;

import Level.BossRoom;
import Level.Level;
import Level.MerchantRoom;
import Level.Room;
import Objects.Boss;
import Objects.CObject;
import Objects.Necromancer;
import Objects.OrcBoss;
import Objects.Player;
import Sound.SoundPlayer;
import Utilitary.Gamepad;
import Utilitary.V2D;

public class InGame extends UI {

    public InGame() {

        // Barre de vie
        this.elements.add(new UIElement(new V2D(50, 50), new V2D(0.1, 0.1), "heart", false));
        this.elements.add(new UIElement(new V2D(85, 50), new V2D(0.1, 0.1), "heart", false));
        this.elements.add(new UIElement(new V2D(120, 50), new V2D(0.1, 0.1), "heart", false));

        // Barre de shield
        this.elements.add(new UIElement(new V2D(155, 50), new V2D(0.1, 0.1), "shield_heart", false));
        this.elements.add(new UIElement(new V2D(190, 50), new V2D(0.1, 0.1), "shield_heart", false));
        this.elements.add(new UIElement(new V2D(225, 50), new V2D(0.1, 0.1), "shield_heart", false));

        // Coins
        this.elements.add(new UIElement(new V2D(50, 90), new V2D(2, 2), "coin", false));
        this.elements.add(new UIElement(new V2D(90, 120), new V2D(1, 1), "x0", true));
    }

    @Override
    public void update(float dt) {
        Player p = Player.getPlayer1();
        boolean bMerchant = false;
        // -----------------------------
        // Nettoyage UI dynamique
        // -----------------------------
        this.elements.removeIf(e
                -> !e.getText().equals("Appuyer sur " + (Gamepad.gamepad.isConnected() ? "B" : "E") + " pour marchander")
        );
        bMerchant = !this.elements.isEmpty();

        // -----------------------------
        // Barre de vie / shield
        // -----------------------------
        V2D pos = new V2D(50, 50);

        for (int i = 0; i < p.getCaracteristics().getActHP(); i++) {
            this.elements.add(new UIElement(new V2D(pos), new V2D(0.1, 0.1), "heart", false));
            pos.x += 35;
        }

        for (int i = 0; i < p.getCaracteristics().getActArmor(); i++) {
            this.elements.add(new UIElement(new V2D(pos), new V2D(0.1, 0.1), "shield_heart", false));
            pos.x += 35;
        }

        // -----------------------------
        // Coins
        // -----------------------------
        this.elements.add(new UIElement(new V2D(50, 90), new V2D(2, 2), "coin", false));
        this.elements.add(new UIElement(
                new V2D(90, 120),
                new V2D(1, 1),
                "x" + p.getGold(),
                true
        ));

        // -----------------------------
        // MINI-MAP
        // -----------------------------
        int cellSize = 50;
        int startX = 1600;
        int startY = 75;

        int width = Level.getWidth();
        int height = Level.getHeight();

        int px = Level.getPosX();
        int py = Level.getPosY();

        var grid = Level.getRooms();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                Room r = grid.get(y).get(x);

                boolean isCurrent = (x == px && y == py);
                boolean visited = r.isbCleared() || isCurrent;

                // --------- Sélection de la sprite ---------
                String sprite;

                if (isCurrent) {
                    sprite = "room_current"; 
                } else if (r instanceof MerchantRoom && visited) {
                    sprite = "room_merchant"; 
                } else if (r instanceof BossRoom) {
                    sprite = "room_boss"; 
                } else if (visited) {
                    sprite = "room_visited"; 
                } else {
                    sprite = "room_unknown"; 
                }

                // --------- Placement ---------
                int drawX = startX + x * (cellSize);
                int drawY = startY + y * (cellSize);

                this.elements.add(new UIElement(
                        new V2D(drawX, drawY),
                        new V2D(1, 1),
                        sprite,
                        false
                ));
            }
        }

        //TechMode
        this.elements.add(new UIButton(new V2D(0, 1000), new V2D(8, 8), "invisible_input", () -> {SoundPlayer.playMusicLoop("ModeTech.wav");}));

        if (Level.getActualRoom() instanceof BossRoom)
        {
            double maxBarWidth = 92.8;
            double actualBarWidth = 0.f;

            for (CObject o : Level.getActualRoom().getObjects())
            {
                if (o instanceof Boss b)
                {
                    actualBarWidth = maxBarWidth * b.getPercentHP();
                    if (b instanceof Necromancer) this.elements.add(new UIElement(new V2D(500, 60), new V2D(1.5, 1.5), "Ruzalgal le profanateur", true, Color.decode("#E81616")));
                    else if (b instanceof OrcBoss) this.elements.add(new UIElement(new V2D(515, 60), new V2D(1.5, 1.5), "Goundorf le germanique", true, Color.decode("#E81616")));
                    else this.elements.add(new UIElement(new V2D(500, 60), new V2D(1.5, 1.5), "Astérion l'exterminateur", true, Color.decode("#E81616")));
                }
            }

            this.elements.add(new UIElement(new V2D(466, 75), new V2D(actualBarWidth, 1.6), "boss_health_bar", false));
            this.elements.add(new UIElement(new V2D(450, 75), new V2D(16, 4), "boss_health_bar_frame", false));
        }
        if (!bMerchant)
        {
            this.elements.add(new UIElement(new V2D(550, 950), new V2D(1,1), "Nombre d'étages conquis : " + Integer.toString(Level.getFloor() - 1), true));
            this.elements.add(new UIElement(new V2D(550, 990), new V2D(1,1), "Nombre de salles pacifiées : " + Integer.toString(Level.getNbrRoomCleaned()), true));
        }
    }
}
