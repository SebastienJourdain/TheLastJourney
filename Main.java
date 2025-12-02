import javax.swing.Timer;

import Graphism.Window;
import Level.Level;
import Sound.SoundPlayer;
import UI.UI;
import Utilitary.Gamepad;

public class Main {

    public static void main(String[] args)
    {
        Window.initialize();
        SoundPlayer.initialize();
        SoundPlayer.playMusicLoop("MusicDebut.wav");
        
        Timer timer = new Timer(16, e -> {
            if (Level.getActualRoom() != null && UI.ActualState == UI.EGameState.InGame) Level.getActualRoom().update(0.016f);
            UI.update(0.016f, Window.window.getFrame());
            Gamepad.gamepad.update(0.016f);
            Window.window.getPanel().repaint();
        });
        timer.start();
    }
}
