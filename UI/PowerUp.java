package UI;

import java.awt.Color;

import Objects.Player;
import Utilitary.V2D;

public class PowerUp extends UI {

    public PowerUp()
    {
        //Fond opaque
        this.elements.add(new UIElement(new V2D(0, 0), new V2D(192, 108), "background", false));

        //Interface
        this.elements.add(new UIElement(new V2D(467, 100), new V2D(8, 7.5), "market_interface", false));
        this.elements.add(new UIElement(new V2D(827, 165), new V2D(1.25, 1.25), "POWER UP", true, Color.decode("#42271C")));

        //Power ups
        this.elements.add(new UIButton(new V2D(532, 230), new V2D(10, 5.5), "click_input", () -> { increaseDamage(1); }));
        this.elements.add(new UIElement(new V2D(632, 265), new V2D(4, 4), "damage_up", false));
        this.elements.add(new UIElement(new V2D(792, 335), new V2D(1.25, 1.25), "Dégâts + 1", true, Color.decode("#5A4021")));

        this.elements.add(new UIButton(new V2D(532, 450), new V2D(10, 5.5), "click_input", () -> { increaseSpeed(0.1f); }));
        this.elements.add(new UIElement(new V2D(632, 485), new V2D(4, 4), "speed_up", false));
        this.elements.add(new UIElement(new V2D(792, 555), new V2D(1.25, 1.25), "Vitesse + 10%", true, Color.decode("#5A4021")));

        this.elements.add(new UIButton(new V2D(532, 670), new V2D(10, 5.5), "click_input", () -> { increaseCritical(0.01f); }));
        this.elements.add(new UIElement(new V2D(632, 705), new V2D(4, 4), "critical_up", false));
        this.elements.add(new UIElement(new V2D(792, 775), new V2D(1.25, 1.25), "Critiques + 1%", true, Color.decode("#5A4021")));
    }
  
    public static void increaseDamage(int damage)
    {
        Player p = Player.getPlayer1();
        p.getCaracteristics().setDamage(damage + p.getCaracteristics().getDamage());
        UI.ActualState = UI.EGameState.InGame;
    }
    public static void increaseSpeed(float speed)
    {
        Player p = Player.getPlayer1();
        p.getCaracteristics().setSpeed(speed + p.getCaracteristics().getSpeed());
        UI.ActualState = UI.EGameState.InGame;
    }
    public static void increaseCritical(float critical)
    {
        Player p = Player.getPlayer1();
        p.getCaracteristics().setCritical(critical + p.getCaracteristics().getCritical());
        UI.ActualState = UI.EGameState.InGame;
    }
}
