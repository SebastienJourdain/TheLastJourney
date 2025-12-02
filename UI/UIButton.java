package UI;

import Graphism.Animation;
import Sound.SoundPlayer;
import Utilitary.V2D;

public class UIButton extends UIElement
{
    private final Runnable onClick;

    public UIButton(V2D position, V2D size, String spriteName, Runnable onClick) {
        super(position, size, spriteName, false);
        this.onClick = onClick;
    }

    public void click() {
        if (onClick != null) onClick.run();
        SoundPlayer.playSFX("click.wav");
    }

    public boolean contains(int mx, int my)
    {
        Animation a = Animation.get(this.getSprite().getAtlasName(), this.getSprite().getAnimName());
        return mx >= position.x &&
               my >= position.y &&
               mx <= position.x + (a.getFrameSize().x * this.size.x) &&
               my <= position.y + (a.getFrameSize().y * this.size.y);
    }
}
