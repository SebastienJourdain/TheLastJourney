package UI;

import java.awt.Color;

import Sound.SoundPlayer;
import Utilitary.V2D;

public class GameOver extends UI {

    public GameOver(){

        this.elements.add(new UIElement(new V2D(500, 450), new V2D(4, 4), "GAME OVER", true, Color.decode("#6D0000") ));
        this.elements.add(new UIButton(new V2D(675, 485), new V2D(55, 4), "invisible_input", ()-> { UI.ActualState = UI.EGameState.MainMenu; SoundPlayer.stopTech(); SoundPlayer.playMusicLoop("MusicDebut.wav");}));
        this.elements.add(new UIElement(new V2D(675, 525), new V2D(1.5, 1.5), "Menu principal", true, Color.decode("#FFFFFF")));


    }
  
}
