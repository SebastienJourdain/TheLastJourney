package Objects;

import Graphism.Animation;
import Level.Level;
import Utilitary.V2D;

public class SummoningEffect extends Effect
{
    private int activationFrame = 0;
    private float time = 0.f;
    private float animSpeed = 1.f;

    public SummoningEffect(V2D size, V2D position, V2D collisionSize, String animName, int activationFrame)
    {
        super(size, position, collisionSize, animName);
        this.activationFrame = activationFrame;
        this.animSpeed = Animation.get("Effects", animName).getAnimationSpeed();
    }

    @Override
    public void update(float dt)
    {
        this.getSprite().updateAnim(dt);
        this.time += dt;
        if (this.time > (1.f / (10.f * this.animSpeed)))
        {
            this.time = 0.f;
            this.activationFrame--;
            if (this.activationFrame == 0)
            {
                Skeleton s = new Skeleton(new V2D(1.25, 1.25), new V2D(this.getPosition().x + 40, this.getPosition().y + 32), new V2D(28.0, 26.0));
                Level.getActualRoom().addObject(s);
            }
        }
    };
}