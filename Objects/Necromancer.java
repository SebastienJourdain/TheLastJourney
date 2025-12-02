package Objects;

import AI.NecromancerBossAI;
import Graphism.Animation;
import Level.Level;
import Utilitary.V2D;

public class Necromancer extends Boss
{
    private int nbrSummoned = 2;
    public Necromancer(V2D size, V2D position, V2D collisionSize)
    {
        super(size, position, collisionSize, "Necromancer", "walk_bot");
        this.getCollision().setPosition(new V2D(position.x - 20.0, position.y));
        this.setAi(new NecromancerBossAI());
        this.setCaracteristics(new Caracteristics(25 * Level.getFloor(), 0, 1 * Level.getFloor(), 1.5f, 0.f));
    }

    public Necromancer(V2D position)
    {
        super(new V2D(1.75, 1.75), position, new V2D(25, 25), "Necromancer", "walk_bot");
        this.getCollision().setPosition(new V2D(position.x - 18.0, position.y + 15.0));
        this.setAi(new NecromancerBossAI());
        this.setCaracteristics(new Caracteristics(25 * Level.getFloor(), 0, 1 * Level.getFloor(), 1.5f, 0.f));
    }

    @Override
    public void attack1()
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
            if (y < 0) this.getSprite().setAnimName("attack1_top");
            else if (y > 0) this.getSprite().setAnimName("attack1_bot");
        }
        else
        {
            if (x < 0) this.getSprite().setAnimName("attack1_left");
            else if (x > 0) this.getSprite().setAnimName("attack1_right");
        }

        int offsetX = 500;
        int offsetY = 400;
        for (int i = 0; i < this.nbrSummoned; i++)
        {
            boolean inObstacle = true;
            while (inObstacle)
            {
                V2D pos = new V2D(random.nextInt(0, 900) + offsetX, random.nextInt(0, 280) + offsetY);
                Skeleton s = new Skeleton(new V2D(1.25, 1.25), new V2D(pos.x + 40, pos.y + 32), new V2D(28.0, 26.0));
                SummoningEffect se = new SummoningEffect(new V2D(1.25, 1.25), pos, new V2D(-1,-1), "summoning", 6);
                if (!s.getCollision().forwardBlockCheck(0, 0))
                {
                    //Ajouter effet qui fera pop le squelette
                    Level.getActualRoom().addObject(se);
                    inObstacle = false;
                }
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

        ZoneDamage damage = new ZoneDamage(new V2D(3.0, 3.0), new V2D(Player.getPlayer1().getPosition()), new V2D(16, 16), this.getCaracteristics().getDamage(), 16, 10, this, "blood_spell", 30.f, true);
        damage.setAnimSpeed(Animation.get("Effects", "blood_spell").getAnimationSpeed());
        Level.getActualRoom().addObject(damage);
    }

    public int getNbrSummoned() {
        return this.nbrSummoned;
    }

    public void setNbrSummoned(int nbrSummoned) {
        this.nbrSummoned = nbrSummoned;
    }
}