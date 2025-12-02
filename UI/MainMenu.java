package UI;

import java.awt.Color;

import Utilitary.V2D;

public class MainMenu extends UI {

    public MainMenu(){

        //Titre du jeu
        this.elements.add(new UIElement(new V2D(525, 130), new V2D(6, 6), "TileSet", "skullking"));
        this.elements.add(new UIElement(new V2D(150, 560), new V2D(4, 4), "THE LAST JOURNEY", true, Color.decode("#E0C61A") ));
        this.elements.add(new UIButton(new V2D(650, 620), new V2D(60, 4), "invisible_input", ()-> { UI.ActualState = UI.EGameState.CharacterChoice; }));
        this.elements.add(new UIElement(new V2D(650, 660), new V2D(1.5, 1.5), "Nouvelle partie", true, Color.decode("#E0C61A")));


    }
  
}
