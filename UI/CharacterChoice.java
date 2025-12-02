package UI;

import java.awt.Color;

import Graphism.Window;
import Level.Level;
import Objects.Archer;
import Objects.Mage;
import Objects.Player;
import Objects.Warrior;
import Sound.SoundPlayer;
import Utilitary.V2D;

public class CharacterChoice extends UI {

    public CharacterChoice(){

        //Texte de choix
        this.elements.add(new UIElement(new V2D(550, 200), new V2D(2, 2), "Choisis ton héros", true, Color.decode("#FFFFFF") ));
        this.elements.add(new UIElement(new V2D(285, 800), new V2D(1.5, 1.5), "Renaud", true, Color.decode("#FFFFFF") ));
        this.elements.add(new UIElement(new V2D(180, 850), new V2D(1.5, 1.5), "le chevalier", true, Color.decode("#FFFFFF") ));
        this.elements.add(new UIElement(new V2D(825, 800), new V2D(1.5, 1.5), "Rogier", true, Color.decode("#FFFFFF") ));
        this.elements.add(new UIElement(new V2D(755, 850), new V2D(1.5, 1.5), "le sorcier", true, Color.decode("#FFFFFF") ));
        this.elements.add(new UIElement(new V2D(1325, 800), new V2D(1.5, 1.5), "Celinia", true, Color.decode("#FFFFFF") ));
        this.elements.add(new UIElement(new V2D(1285, 850), new V2D(1.5, 1.5), "l'archère", true, Color.decode("#FFFFFF") ));

        //Bustes des personnages + cadres
        this.elements.add(new UIButton(new V2D(175, 250), new V2D(3, 3), "cadre", () -> { startGame("Warrior"); }));
        this.elements.add(new UIElement(new V2D(245, 298), new V2D(10, 10), "Warrior", "buste_warrior"));
        this.elements.add(new UIButton(new V2D(700, 250), new V2D(3, 3), "cadre",  () -> { startGame("Mage"); }));
        this.elements.add(new UIElement(new V2D(760, 418), new V2D(10, 10), "Mage", "buste_mage"));
        this.elements.add(new UIButton(new V2D(1225, 250), new V2D(3, 3), "cadre",  () -> { startGame("Archer"); }));
        this.elements.add(new UIElement(new V2D(1315, 418), new V2D(10, 10), "Archer", "buste_archer"));

    }

    private void startGame(String c)
    {
        Player p = null;
        switch (c)
        {
            case "Warrior" -> p = new Warrior(new V2D(1.5, 1.5), new V2D(900, 700), new V2D(28, 50));
            case "Mage" -> p = new Mage(new V2D(1.5, 1.5), new V2D(900, 700), new V2D(28, 50));
            case "Archer" -> p = new Archer(new V2D(1.5, 1.5), new V2D(900, 700), new V2D(28, 50));
            default -> { }
        }
        if (p != null)
        {
            Window.window.getPanel().addKeyListener(p);
            Window.window.getPanel().addMouseMotionListener(p);
            Window.window.getPanel().addMouseListener(p);
            
            Level.generateLevel();
            Level.getActualRoom().addObject(p);

            UI.ActualState = UI.EGameState.InGame;
            SoundPlayer.stopMusic();
        }
    }
  
}
