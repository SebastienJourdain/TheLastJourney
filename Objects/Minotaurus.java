package Objects;

import java.util.List;

import AI.MinotaurusBossAI;
import Graphism.Animation;
import Level.Level;
import Utilitary.V2D;



public class Minotaurus extends Boss
{
    public Minotaurus(V2D size, V2D position, V2D collisionSize)
    {
        super(size, position, collisionSize, "Minotaurus", "walk_bot");
        this.getCollision().setPosition(new V2D(position.x - 20.0, position.y));
        this.setAi(new MinotaurusBossAI());
        this.setCaracteristics(new Caracteristics(30 * Level.getFloor(), 0, 1 * Level.getFloor(), 1.5f, 0.f));
    }
    public Minotaurus(V2D position)
    {
        super(new V2D(3, 3), position, new V2D(20, 20), "Minotaurus", "walk_bot");
        this.getCollision().setPosition(new V2D(position.x - 30.0, position.y + 5.0));
        this.setAi(new MinotaurusBossAI());
        this.getCaracteristics().setSpeed(1.5f);
        this.setCaracteristics(new Caracteristics(30 * Level.getFloor(), 0, 1 * Level.getFloor(), 1.5f, 0.f));
    }

    @Override
    public void attack1()
    {
        if (this.isbAction()) return;
        super.attack1();

        V2D dir = new V2D(  Player.getPlayer1().getPosition().x - this.getPosition().x,
                            Player.getPlayer1().getPosition().y - this.getPosition().y);
        dir.normalize();
        double x = dir.x;
        double y = dir.y;

        if (Math.abs(dir.x) < 0.01f && Math.abs(dir.y) < 0.01) return;

        if (Math.abs(y) > Math.abs(x))
        {
            if (y < 0)
            {
                Melee damage = new Melee(new V2D(1, 1), new V2D(this.getPosition().x - 75, this.getPosition().y - 60), new V2D(150, 60), this.getCaracteristics().getDamage(), 6, this, 4);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "attack1_top");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.getSprite().setAnimName("attack1_top");
            }
            else if (y > 0)
            {
                Melee damage = new Melee(new V2D(1, 1), new V2D(this.getPosition().x - 75, this.getPosition().y + 40), new V2D(150, 60), this.getCaracteristics().getDamage(), 6, this, 4);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "attack1_bot");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.getSprite().setAnimName("attack1_bot");
            }
        }
        else
        {
            if (x < 0)
            {
                Melee damage = new Melee(new V2D(1, 1), new V2D(this.getPosition().x - 85, this.getPosition().y - 60), new V2D(60, 150), this.getCaracteristics().getDamage(), 6, this, 4);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "attack1_left");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.getSprite().setAnimName("attack1_left");
            }
            else if (x > 0)
            {
                Melee damage = new Melee(new V2D(1, 1), new V2D(this.getPosition().x + 25, this.getPosition().y - 60), new V2D(60, 150), this.getCaracteristics().getDamage(), 6, this, 4);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "attack1_right");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.getSprite().setAnimName("attack1_right");
            }
        }
    }
    @Override
    public void attack2()
    {
        if (this.isbAction()) return;
        super.attack2();

        V2D dir = new V2D(  Player.getPlayer1().getPosition().x - this.getPosition().x,
                            Player.getPlayer1().getPosition().y - this.getPosition().y);
        dir.normalize();
        double x = dir.x;
        double y = dir.y;

        if (Math.abs(dir.x) < 0.01f && Math.abs(dir.y) < 0.01) return;

        if (Math.abs(y) > Math.abs(x))
        {
            if (y < 0) this.getSprite().setAnimName("attack2_top");
            else if (y > 0) this.getSprite().setAnimName("attack2_bot");
        }
        else
        {
            if (x < 0) this.getSprite().setAnimName("attack2_left");
            else if (x > 0) this.getSprite().setAnimName("attack2_right");
        }

        for (V2D thunderDir : List.of(new V2D(150,0), new V2D(-150,0), new V2D(0,150), new V2D(0,-150)))
        {
            ZoneDamage damage = new ZoneDamage(new V2D(3.0, 3.0), new V2D(this.getPosition().x + thunderDir.x, this.getPosition().y + thunderDir.y), new V2D(32, 32), this.getCaracteristics().getDamage(), 12, 8, this, "thunder", 60.f, true);
            damage.setAnimSpeed(Animation.get("Minotaurus", "attack2_top").getAnimationSpeed());
            Level.getActualRoom().addObject(damage);
        }
    }
}