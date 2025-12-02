package Objects;

import AI.OrcBossAI;
import Graphism.Animation;
import Level.Level;
import Utilitary.V2D;

public class OrcBoss extends Boss
{
    public OrcBoss(V2D size, V2D position, V2D collisionSize)
    {
        super(size, position, collisionSize, "OrcBoss", "walk_bot");
        this.getCollision().setPosition(new V2D(position.x - 35.0, position.y));
        this.setAi(new OrcBossAI());
        this.setCaracteristics(new Caracteristics(40 * Level.getFloor(), 0, 1 * Level.getFloor(), 1.5f, 0.f));
    }

    public OrcBoss(V2D position)
    {
        super(new V2D(4, 4), position, new V2D(15, 15), "OrcBoss", "walk_bot");
        this.getCollision().setPosition(new V2D(position.x - 30.0, position.y - 10.0));
        this.setAi(new OrcBossAI());
        this.setCaracteristics(new Caracteristics(40 * Level.getFloor(), 0, 1 * Level.getFloor(), 1.5f, 0.f));
    }

    /**
     * Attaque de mêlée de l’orc.
     * L’orc, créature lourde et brutale, effectue une attaque à forte zone d’impact,
     * différente selon l’orientation : haut, bas, gauche ou droite.
     * Une instance de {@link Melee} est créée avec une large hitbox,
     * puis synchronisée avec l’animation correspondante.
     *  
     * L’attaque ne peut être déclenchée que si aucune action n’est en cours.
     */
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
                Melee damage = new Melee(new V2D(1, 1), new V2D(this.getPosition().x - 75, this.getPosition().y - 80), new V2D(150, 60), this.getCaracteristics().getDamage(), 6, this, 4);
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
    /**
     * Attaque de dash du boss orc.
     * L’orc, créature lourde et brutale, effectue une attaque à forte zone d’impact,
     * différente selon l’orientation : haut, bas, gauche ou droite.
     * Une instance de {@link Melee} est créée avec une large hitbox,
     * puis synchronisée avec l’animation correspondante.
     *  
     * L’attaque ne peut être déclenchée que si aucune action n’est en cours.
     */
    @Override
    public void attack2()
    {
        if (this.isbAction()) return;
        super.attack2();

        V2D dir = new V2D(  Player.getPlayer1().getPosition().x - this.getPosition().x,
                            Player.getPlayer1().getPosition().y - this.getPosition().y);
        V2D normDir = new V2D(dir);
        normDir.normalize();
        double x = normDir.x;
        double y = normDir.y;
        double len = Math.sqrt(dir.x * dir.x + dir.y * dir.y);
        if (len <= 40.0) { }
        else
        {
            double scale = (len - 40.0) / len;
            dir.x *= scale;
            dir.y *= scale;
        }

        if (Math.abs(x) < 0.01f && Math.abs(y) < 0.01) return;

        if (Math.abs(y) > Math.abs(x))
        {
            if (y < 0)
            {
                DashMelee damage = new DashMelee(new V2D(1, 1), new V2D(this.getPosition().x - 75, this.getPosition().y - 80), new V2D(150, 60), this.getCaracteristics().getDamage(), 6, this, 4, dir);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "attack2_top");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.getSprite().setAnimName("attack2_top");
            }
            else if (y > 0)
            {
                DashMelee damage = new DashMelee(new V2D(1, 1), new V2D(this.getPosition().x - 75, this.getPosition().y + 40), new V2D(150, 60), this.getCaracteristics().getDamage(), 6, this, 4, dir);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "attack2_bot");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.getSprite().setAnimName("attack2_bot");
            }
        }
        else
        {
            if (x < 0)
            {
                DashMelee damage = new DashMelee(new V2D(1, 1), new V2D(this.getPosition().x - 85, this.getPosition().y - 60), new V2D(60, 150), this.getCaracteristics().getDamage(), 6, this, 4, dir);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "attack2_left");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.getSprite().setAnimName("attack2_left");
            }
            else if (x > 0)
            {
                DashMelee damage = new DashMelee(new V2D(1, 1), new V2D(this.getPosition().x + 25, this.getPosition().y - 60), new V2D(60, 150), this.getCaracteristics().getDamage(), 6, this, 4, dir);
                Animation A = Animation.get(this.getSprite().getAtlasName(), "attack2_right");
                damage.setAnimSpeed(A.getAnimationSpeed());
                Level.getActualRoom().addObject(damage);
                this.getSprite().setAnimName("attack2_right");
            }
        }
    }
}