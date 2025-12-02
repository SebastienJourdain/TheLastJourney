package Graphism;

import Utilitary.V2D;

public class UISprite extends Sprite {

    private int animIndex;
    private String animName;
    private String baseAnimName;
    private boolean bAnimRunning;
    private float time = 0.f;

    public UISprite(String atlasName, V2D size) {
        super(atlasName, size);
        this.animName = this.baseAnimName = "";
    }
    public UISprite(String atlasName, V2D size, String animName) {
        super(atlasName, size);
        this.baseAnimName = animName;
        this.animName = this.baseAnimName;
    }

    public int getAnimIndex() {
        return this.animIndex;
    }

    public String getAnimName() {
        return this.animName;
    }

    public boolean isbAnimRunning() {
        return this.bAnimRunning;
    }

    public void updateAnim(float dt)
    {
        this.time += dt;
        Animation A = Animation.get(this.getAtlasName(), this.animName);
        if (A == null) return;
        if (this.time > (1.f / (10.f * A.getAnimationSpeed())))
        {
            this.time = 0.f;
            if (A.isbInfinite()) this.animIndex = this.animIndex + 1 == A.getFrameNbr() ? 0 : this.animIndex + 1;
            else
            {
                this.animIndex++;
                if (this.animIndex == A.getFrameNbr())
                {
                    this.animName = this.baseAnimName;
                    this.animIndex = 0;
                }
            }
        }
    }

    public void setAnimName(String animName) {
        this.animName = animName;
        this.animIndex = 0;
    }

    public void setBaseAnimName(String baseAnimName) {
        this.baseAnimName = baseAnimName;
    }

    public void setAnimIndex(int animIndex) {
        this.animIndex = animIndex;
    }
}
