package Utilitary;

import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_1;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetGamepadState;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwJoystickIsGamepad;
import static org.lwjgl.glfw.GLFW.glfwJoystickPresent;
import org.lwjgl.glfw.GLFWGamepadState;

import Objects.Collision;
import Objects.Merchant;
import Objects.Player;
import UI.UI;
import UI.UIButton;
import UI.UIElement;


public class Gamepad
{
    public static Gamepad gamepad = new Gamepad();
    /** État courant de la manette (GLFW). */
    private final GLFWGamepadState state = GLFWGamepadState.create();

    /** Dernier état du trigger droit de la manette (utilisé pour détecter le relâchement). */
    private int previousLTriggerState = GLFW_RELEASE;
    private int previousRTriggerState = GLFW_RELEASE;
    private int previousButton0State = GLFW_RELEASE;
    private int previousButton1State = GLFW_RELEASE;
    private int previousButtonStartState = GLFW_RELEASE;
    private int previousButtonBackState = GLFW_RELEASE;

    public Gamepad()
    {
        if (!glfwInit()) throw new IllegalStateException("GLFW init failed");
    }

    public void update(float dt)
    {
        if (glfwGetGamepadState(GLFW_JOYSTICK_1, state))
        {
            // Axes
            V2D or = new V2D(0.0, 0.0);
            V2D dir = new V2D(0.0, 0.0);
            for (int i = 0; i < state.axes().limit(); i++)
            {
                //Axe 2 = Joystick droit -> Gauche (-1), Droite (1)
                //Axe 3 = Joystick droite -> Haut (-1), Bas (1)

                //Axe 0 = Joystick gauche -> Gauche (-1), Droite (1)
                //Axe 1 = Joystick gauche -> Haut (-1), Bas (1)
                float val = state.axes().get(i);
                if ((val < -0.05f || val > 0.05f))
                {
                    if (UI.ActualState == UI.EGameState.InGame)
                    {
                        switch (i)
                        {
                            case 0 -> dir.x = val;
                            case 1 -> dir.y = val;
                            case 2 -> or.x = val;
                            case 3 -> or.y = val;
                            default -> {}
                        }
                    }
                    else
                    {
                        switch (i)
                        {
                            case 0 -> UI.addMouseX(val * 10.f);
                            case 1 -> UI.addMouseY(val * 10.f);
                            default -> {}
                        }
                    }
                }
            }
            if (UI.ActualState == UI.EGameState.InGame)
            {
                or.normalize();
                dir.normalize();

                Player p = Player.getPlayer1();
                if (dir.x != 0 || dir.y != 0) p.setOrientation(dir);
                if (or.x != 0 || or.y != 0) p.setAim(or);
                if ((dir.x > 0.05f || dir.x < -0.05f) && !p.getCollision().forwardBlockCheck(dir.x * p.getCaracteristics().getSpeed(), 0.f)) p.addX(dir.x);
                if ((dir.y > 0.05f || dir.y < -0.05f) && !p.getCollision().forwardBlockCheck(0.f, dir.y * p.getCaracteristics().getSpeed())) p.addY(dir.y);

                // Boutons
                if (!p.isbAction() && state.buttons().get(0) == GLFW_RELEASE && this.previousButton0State == GLFW_PRESS)
                {
                    for (Collision c : p.getCollision().getColliding())
                    {
                        if (c.getObject() instanceof Merchant)
                        {
                            UI.ActualState = UI.EGameState.UIMerchant;
                            this.previousButton0State = state.buttons().get(0);
                            return;
                        }
                    }
                    p.gamepadButton0();
                }
                this.previousButton0State = state.buttons().get(0);
                if (!p.isbAction() && state.buttons().get(1) == GLFW_RELEASE && this.previousButton1State == GLFW_PRESS) p.gamepadButton1();
                this.previousButton1State = state.buttons().get(1);
                if (!p.isbAction() && state.buttons().get(5) == GLFW_RELEASE && this.previousRTriggerState == GLFW_PRESS) p.gamepadButtonRT();
                this.previousRTriggerState = state.buttons().get(5);
                if (!p.isbAction() && state.buttons().get(4) == GLFW_RELEASE && this.previousLTriggerState == GLFW_PRESS) p.gamepadButtonLT();
                this.previousLTriggerState = state.buttons().get(4);
                if (state.buttons().get(7) == GLFW_RELEASE && this.previousButtonStartState == GLFW_PRESS) UI.ActualState = UI.EGameState.GameMenu;
                this.previousButtonStartState = state.buttons().get(7);
                if (state.buttons().get(6) == GLFW_RELEASE && this.previousButtonBackState == GLFW_PRESS) UI.ActualState = UI.EGameState.Inventory;
                this.previousButtonBackState = state.buttons().get(6);
                
            }
            else
            {
                if (state.buttons().get(0) == GLFW_RELEASE && this.previousButton0State == GLFW_PRESS)
                {
                    for (UIElement el : UI.UIs.get(UI.ActualState).getElements())
                    {
                        if (el != null)
                        {
                            if (el instanceof UIButton btn && btn.contains(UI.getMouseX(), UI.getMouseY()))
                            {
                                btn.click();
                                break;
                            }
                        }
                    }
                }
                this.previousButton0State = state.buttons().get(0);

                if (state.buttons().get(1) == GLFW_RELEASE && this.previousButton1State == GLFW_PRESS)
                {
                    switch (UI.ActualState)
                    {
                        case Inventory, GameMenu:
                            UI.ActualState = UI.EGameState.InGame;
                            break;
                        default:
                            break;
                    }
                }
                this.previousButton1State = state.buttons().get(1);
            }
        }
    }

    public boolean isConnected()
    {
        return (glfwJoystickPresent(GLFW_JOYSTICK_1) && glfwJoystickIsGamepad(GLFW_JOYSTICK_1));
    }
}