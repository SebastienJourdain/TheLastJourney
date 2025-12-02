package Graphism;

import Objects.CObject;
import Utilitary.V2D;

public class ObjectSprite extends Sprite {

    private final CObject object; 
    private int animIndex;
    private String animName;
    private String baseAnimName;
    private boolean bAnimRunning;
    private float time = 0.f;

    public ObjectSprite(String atlasName, V2D size, CObject object) {
        super(atlasName, size);
        this.object = object;
        this.animName = this.baseAnimName = "";
    }
    public ObjectSprite(String atlasName, V2D size, CObject object, String animName) {
        super(atlasName, size);
        this.object = object;
        this.baseAnimName = animName;
        this.animName = this.baseAnimName;
    }

    public CObject getObject() {
        return this.object;
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
                    this.object.onAnimationEnd();
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
}
