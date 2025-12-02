package UI;

import java.awt.Color;

import Graphism.Window;
import Level.Level;
import Sound.SoundPlayer;
import Utilitary.V2D;

public class GameMenu extends UI {

    public GameMenu(){

        //Fond opaque
        this.elements.add(new UIElement(new V2D(0, 0), new V2D(192, 108), "background", false));

        //Fond du menu
        this.elements.add(new UIElement(new V2D(460, 110), new V2D(5, 4), "menu_background", false));

        //Textes du menu
        this.elements.add(new UIElement(new V2D(825, 310), new V2D(2, 2), "MENU", true));

        this.elements.add(new UIButton(new V2D(660, 440), new V2D(55, 4), "invisible_input",
                () -> { UI.ActualState = UI.EGameState.InGame; }));
        this.elements.add(new UIElement(new V2D(660, 460), new V2D(1.5, 1.5), "REVENIR AU JEU", true));

        this.elements.add(new UIButton(new V2D(530, 550), new V2D(80, 4), "invisible_input",
                () -> { UI.ActualState = UI.EGameState.CharacterChoice; Level.dischargeLevel(); SoundPlayer.stopTech(); SoundPlayer.playMusicLoop("MusicDebut.wav");}));
        this.elements.add(new UIElement(new V2D(530, 590), new V2D(1.5, 1.5), "RECOMMENCER UNE PARTIE", true));

        this.elements.add(new UIButton(new V2D(660, 680), new V2D(55, 4), "invisible_input",
                () -> { Window.window.getFrame().dispose(); System.exit(0); }));
        this.elements.add(new UIElement(new V2D(660, 720), new V2D(1.5, 1.5), "QUITTER LE JEU", true));

        //Croix de fermeture
        this.elements.add(new UIButton(new V2D(1275, 150), new V2D(4, 4), "close_cross",
                ()-> { UI.ActualState = UI.EGameState.InGame; }));

        //Réglage du son
        // Bouton + : augmente volume musique
        this.elements.add(new UIButton(
                new V2D(1100, 140),
                new V2D(2, 2),
                "sound_set",
                () -> { SoundPlayer.increaseVolume(); }
        ));

        // Bouton - : réduit volume musique
        this.elements.add(new UIButton(
                new V2D(1175, 140),
                new V2D(2, 2),
                "sound_set",
                () -> { SoundPlayer.decreaseVolume(); }
        ));

        //Textes du volume
        this.elements.add(new UIElement(new V2D(1110, 175), new V2D(0.60, 0.60), "VOL", true, Color.decode("#FFFFFF")));
        this.elements.add(new UIElement(new V2D(1123, 190), new V2D(0.60, 0.60), "+", true, Color.decode("#FFFFFF")));
        this.elements.add(new UIElement(new V2D(1185, 175), new V2D(0.60, 0.60), "VOL", true, Color.decode("#FFFFFF")));
        this.elements.add(new UIElement(new V2D(1198, 190), new V2D(0.60, 0.60), "-", true, Color.decode("#FFFFFF")));

        // //TechMode
        // this.elements.add(new UIButton(new V2D(1000, 145), new V2D(2, 2), "tech_mode", () -> {SoundPlayer.playMusicLoop("ModeTech.wav");}));
    }
}
