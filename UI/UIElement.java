package UI;

import java.awt.Color;

import Graphism.UISprite;
import Utilitary.V2D;


public class UIElement {
    protected V2D position;
    protected V2D size;
    protected UISprite sprite = null;
    protected String text = "";
    protected boolean BText;
    protected Color textColor = Color.WHITE;

    public UIElement(V2D position, V2D size, String UIName, boolean BText) {
        this.position = position;
        this.size = size;
        this.BText = BText;
        if (BText) this.text = UIName;
        else this.sprite = new UISprite("UI", size, UIName);
    }

    public UIElement(V2D position, V2D size, String UIName, boolean BText, Color color) {
        this.position = position;
        this.size = size;
        this.BText = BText;
        if (BText) this.text = UIName;
        else this.sprite = new UISprite("UI", size, UIName);
        this.textColor = color;
    }

    public UIElement(V2D position, V2D size, String AtlasName, String animName) {
        this.position = position;
        this.size = size;
        this.BText = false;
        this.sprite = new UISprite(AtlasName, size, animName);
    }

    public V2D getPosition() {
        return position;
    }

    public V2D getSize() {
        return size;
    }

    public UISprite getSprite() {
        return sprite;
    }

    public String getText() {
        return text;
    }

    public boolean isBText() {
        return BText;
    }

    public void update(float dt)
    {
        if (this.sprite != null) this.sprite.updateAnim(dt);
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public void addX(double x) { this.position.x += x; }
    public void addY(double y) { this.position.y += y; }

}
