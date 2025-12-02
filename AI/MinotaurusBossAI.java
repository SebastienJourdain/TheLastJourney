package AI;

import java.util.HashMap;
import java.util.Map;

import Objects.Character;
import Objects.Minotaurus;
import Objects.Player;
import Utilitary.V2D;

public class MinotaurusBossAI extends AI {

    private final Node root;

    // === COOLDOWNS ===
    private static final int CD_LIGHTNING = 200;   
    private static final int CD_MELEE = 40;

    // === CONSTANTES ===
    private static final double SAFE_DISTANCE = 160;
    private static final double NORMAL_SPEED = 1.6;
    private static final double BERZERK_SPEED = 2.5;
    private static final int CD_DASH = 180; 
    private static final double DASH_SPEED = 6.0; 


    // === THRESHOLD HP ===
    private static final float BERSERK_THRESHOLD = 0.50f;

    // === ÉTAT ===
    private static class BossState {
        int lightningTicks = 0;
        int meleeTicks = 0;
        boolean berzerk = false;
        int dashTicks = 0;
    }

    private final Map<Character, BossState> states = new HashMap<>();
    private BossState current;

    private BossState ensureState(Character c) {
        return states.computeIfAbsent(c, k -> new BossState());
    }


    // ---- CONSTRUCTEUR ----

    public MinotaurusBossAI() {

        this.root = new Selector()
            .add(buildBerzerkPhase())
            .add(buildNormalPhase());
    }


    
    // ---- PHASE NORMALE ----
    

    private Node buildNormalPhase() {
    return new Sequence()

        // -------- Check HP ----------
        .add(new Action("Check normal phase", (ch) -> {
            if (!(ch instanceof Minotaurus boss)) return Node.Status.FAIL;

            current.berzerk = boss.getPercentHP() <= BERSERK_THRESHOLD;

            // Si berserk → phase normale impossible
            return current.berzerk ? Node.Status.FAIL : Node.Status.SUCCESS;
        }))

        // -------- Recule si trop proche ----------
        .add(new Action("Too close: flee", (ch) -> {
            if (!(ch instanceof Minotaurus boss)) return Node.Status.FAIL;
            Player p = Player.getPlayer1();
            if (p == null) return Node.Status.FAIL;

            double dist = boss.getPosition().distance(p.getPosition());

            if (dist < SAFE_DISTANCE) {
                double dx = boss.getPosition().x - p.getPosition().x;
                double dy = boss.getPosition().y - p.getPosition().y;
                double d = Math.sqrt(dx*dx + dy*dy);
                double dirX = dx / Math.max(1, d);
                double dirY = dy / Math.max(1, d);

                if (!boss.getCollision().forwardBlockCheck(dirX * 3, 0))
                    boss.addX(dirX * NORMAL_SPEED);

                if (!boss.getCollision().forwardBlockCheck(0, dirY * 3))
                    boss.addY(dirY * NORMAL_SPEED);

                return Node.Status.RUNNING;
            }

            return Node.Status.SUCCESS;
        }))

        // -------- Approche si trop loin pour cast ----------
        .add(new Action("Approach to attack range", (ch) -> {
            if (!(ch instanceof Minotaurus boss)) return Node.Status.FAIL;

            Player p = Player.getPlayer1();
            if (p == null) return Node.Status.FAIL;

            double dist = boss.getPosition().distance(p.getPosition());

            // Distance d'attaque idéale : entre 100 et 300 px
            if (dist > 240) {

                V2D from = boss.getPosition();
                V2D to = p.getPosition();

                if (AI.rayCast(from, to)) {
                    moveToward(boss, to, NORMAL_SPEED);
                } else {
                    navigateAround(boss, to, NORMAL_SPEED);
                }

                return Node.Status.RUNNING;
            }

            return Node.Status.SUCCESS;
        }))

        // ----- Dash si à bonne distance -----
        .add(new Action("Check dash conditions", (ch) -> {
            if (!(ch instanceof Minotaurus boss)) return Node.Status.FAIL;

            Player p = Player.getPlayer1();
            if (p == null) return Node.Status.FAIL;

            double dist = boss.getPosition().distance(p.getPosition());

            // Dash uniquement si entre 140 et 250 px
            if (dist < 140 || dist > 250) return Node.Status.FAIL;

            // CHECK DU CD
            if (current.dashTicks < CD_DASH) return Node.Status.FAIL;

            return Node.Status.SUCCESS;
        }))
        .add(new Action("Dash toward player", (ch) -> {
            if (!(ch instanceof Minotaurus boss)) return Node.Status.FAIL;

            Player p = Player.getPlayer1();
            if (p == null) return Node.Status.FAIL;

            double dx = p.getPosition().x - boss.getPosition().x;
            double dy = p.getPosition().y - boss.getPosition().y;
            double d = Math.sqrt(dx*dx + dy*dy);

            double dirX = dx / Math.max(d, 1);
            double dirY = dy / Math.max(d, 1);

            // On dash 1 seule fois
            if (!boss.getCollision().forwardBlockCheck(dirX*8, 0))
                boss.addX(dirX * DASH_SPEED);
            if (!boss.getCollision().forwardBlockCheck(0, dirY*8))
                boss.addY(dirY * DASH_SPEED);

            current.dashTicks = 0; // reset du CD

            
            boss.getSprite().setAnimName("dash");

            return Node.Status.SUCCESS;
        }))


        // -------- Attaque éclair ----------
        .add(new Action("Check Lightning CD", (ch) -> {
            return (current.lightningTicks >= CD_LIGHTNING)
                ? Node.Status.SUCCESS
                : Node.Status.FAIL;
        }))

        .add(new Action("Cast Lightning", (ch) -> {
            if (!(ch instanceof Minotaurus boss)) return Node.Status.FAIL;

            boss.attack2();  
            current.lightningTicks = 0;

            return Node.Status.SUCCESS;
        }));
}


    
    // -------- PHASE BERSERK ---------
   

    private Node buildBerzerkPhase() {
    return new Sequence()

        // 1) Vérifier si le boss est en mode berserk
        .add(new Action("Check berzerk", (ch) -> {
            if (!(ch instanceof Minotaurus boss)) return Node.Status.FAIL;

            current.berzerk = boss.getPercentHP() <= BERSERK_THRESHOLD;
            return current.berzerk ? Node.Status.SUCCESS : Node.Status.FAIL;
        }))

        // 2) Logique berserk
        .add(
            new Selector()

                // ---  Attaque seulement si à porté ---
                .add(new Sequence()

                    .add(new Action("Check melee range", (ch) -> {
                        if (!(ch instanceof Minotaurus boss)) return Node.Status.FAIL;
                        Player p = Player.getPlayer1();
                        if (p == null) return Node.Status.FAIL;

                        double dist = boss.getPosition().distance(p.getPosition());

                        return (dist <= 70)      
                            ? Node.Status.SUCCESS
                            : Node.Status.FAIL;
                    }))

                    .add(new Action("Check melee CD", (ch) -> {
                        return (current.meleeTicks >= CD_MELEE)
                            ? Node.Status.SUCCESS
                            : Node.Status.FAIL;
                    }))

                    .add(new Action("Berserk Axe Attack", (ch) -> {
                        if (!(ch instanceof Minotaurus boss)) return Node.Status.FAIL;

                        boss.attack1();  
                        current.meleeTicks = 0;

                        return Node.Status.SUCCESS;
                    }))
                )

                // ---  Sinon : rush en continu ---
                .add(new Action("Rush player", (ch) -> {
                    if (!(ch instanceof Minotaurus boss)) return Node.Status.FAIL;

                    Player p = Player.getPlayer1();
                    if (p == null) return Node.Status.FAIL;

                    V2D from = boss.getPosition();
                    V2D to = p.getPosition();

                    if (AI.rayCast(from, to)) {
                        moveToward(boss, to, BERZERK_SPEED);
                        return Node.Status.RUNNING;
                    }

                    navigateAround(boss, to, BERZERK_SPEED);
                    return Node.Status.RUNNING;
                }))
        );
}



    
    // ---- TICK PRINCIPAL -----
    

    @Override
    public void tick(Character boss) {
        current = ensureState(boss);
        current.lightningTicks++;
        current.meleeTicks++;
        current.dashTicks++;

        root.execute(boss);
    }
}
