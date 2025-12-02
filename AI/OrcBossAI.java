package AI;

import java.util.HashMap;
import java.util.Map;

import Level.Level;
import Objects.Character;
import Objects.Effect;
import Objects.OrcBoss;
import Objects.Player;
import Utilitary.V2D;

public class OrcBossAI extends AI {

    private final Node root;

    // === COOLDOWNS ===
    private static final int CD_ATTACK1 = 120; 
    private static final int CD_ATTACK2 = 300; 

    private static final float REGEN_THRESHOLD = 0.30f; // 30%

    private static class BossState {
        int attack1Ticks = 0;
        int attack2Ticks = 0;
        boolean regenerating = false;

        // === Nouvelle gestion de la regen ===
        boolean regenDone = false;      // ne peut le faire qu'une fois
        int regenTick = 0;              // tick interne de regen
        int regenDuration = 0;          // durée totale écoulée
        static final int MAX_REGEN_TIME = 8 * 60; // 8 sec à 60 FPS
    }

    private final Map<Character, BossState> states = new HashMap<>();
    private BossState current;

    private BossState ensureState(Character c) {
        return states.computeIfAbsent(c, k -> new BossState());
    }

    // === CONSTRUCTEUR ===
    public OrcBossAI() {

        this.root = new Selector()

            // --- ATTACK 2 ---
            .add(
                new Sequence()
                    .add(new Action("Attack2 in range", (ch) -> {
                        if (!(ch instanceof OrcBoss boss) || current.regenerating) return Node.Status.FAIL;

                        Player p = Player.getPlayer1();
                        if (p == null) return Node.Status.FAIL;

                        double dist = boss.getPosition().distance(p.getPosition());
                        return (dist <= 200) ? Node.Status.SUCCESS : Node.Status.FAIL;
                    }))
                    .add(new Action("Attack2 ready", (ch) -> {
                        return (current.attack2Ticks >= CD_ATTACK2)
                            ? Node.Status.SUCCESS
                            : Node.Status.FAIL;
                    }))
                    .add(new Action("Do Attack2", (ch) -> {
                        if (ch instanceof OrcBoss boss) boss.attack2();
                        current.attack2Ticks = 0;
                        return Node.Status.SUCCESS;
                    }))
            )

            // --- ATTACK 1 ---
            .add(
                new Sequence()
                    .add(new Action("Attack1 in range", (ch) -> {
                        if (!(ch instanceof OrcBoss boss) || current.regenerating) return Node.Status.FAIL;

                        Player p = Player.getPlayer1();
                        if (p == null) return Node.Status.FAIL;

                        double dist = boss.getPosition().distance(p.getPosition());
                        return (dist <= 100) ? Node.Status.SUCCESS : Node.Status.FAIL;
                    }))
                    .add(new Action("Attack1 ready", (ch) -> {
                        return (current.attack1Ticks >= CD_ATTACK1)
                            ? Node.Status.SUCCESS
                            : Node.Status.FAIL;
                    }))
                    .add(new Action("Do Attack1", (ch) -> {
                        if (ch instanceof OrcBoss boss) boss.attack1();
                        current.attack1Ticks = 0;
                        return Node.Status.SUCCESS;
                    }))
            )

            // --- REGENERATION HP (version limitée 8s) ---
            .add(
                new Sequence()

                    // Entrée en regen
                    .add(new Action("Check low HP", (ch) -> {
                        if (!(ch instanceof OrcBoss boss)) return Node.Status.FAIL;

                        if (current.regenDone) return Node.Status.FAIL; // déjà utilisée une fois

                        float hp = boss.getPercentHP();

                        if (hp < REGEN_THRESHOLD || current.regenerating) {
                            current.regenerating = true;
                            return Node.Status.SUCCESS;
                        }

                        return Node.Status.FAIL;
                    }))

                    // Process regen
                    .add(new Action("Regenerate", (ch) -> {
                        if (!(ch instanceof OrcBoss boss)) return Node.Status.FAIL;

                        if (!current.regenerating)
                            return Node.Status.FAIL;

                        // Timer global
                        current.regenDuration++;

                        // Regen +4 HP chaque seconde
                        if (current.regenTick < 60) {
                            current.regenTick++;
                        } else {
                            int hp = boss.getCaracteristics().getActHP();
                            int max = boss.getCaracteristics().getMaxHP();
                            Effect effect = new Effect(new V2D(boss.getSize()), boss.getPosition(), new V2D(-1,-1), "healing");
                            Level.getActualRoom().addObject(effect);
                            boss.getCaracteristics().setActHP(Math.min(max, hp + 4));
                            current.regenTick = 0;
                        }

                        // Fin si full HP OU timer dépassé
                        boolean fullHP = boss.getPercentHP() >= 1.0f;
                        boolean timerExpired = current.regenDuration >= BossState.MAX_REGEN_TIME;

                        if (fullHP || timerExpired) {
                            current.regenerating = false;
                            current.regenDone = true;
                            current.regenTick = 0;
                            current.regenDuration = 0;
                            return Node.Status.SUCCESS;
                        }

                        return Node.Status.RUNNING;
                    }))
            )

            // --- CHASE PLAYER ---
            .add(new Action("Chase", (ch) -> {
                if (!(ch instanceof OrcBoss boss)) return Node.Status.FAIL;

                Player p = Player.getPlayer1();
                if (p == null) return Node.Status.FAIL;

                V2D from = boss.getPosition();
                V2D to = p.getPosition();

                if  (boss.getPosition().distance(p.getPosition()) < 60)
                    return Node.Status.SUCCESS;

                if (AI.rayCast(from, to)) {
                    moveToward(boss, to, 1.5);
                } else {
                    navigateAround(boss, to, 1.5);
                }

                return Node.Status.RUNNING;
            }));
    }

    // === TICK ===
    @Override
    public void tick(Character boss) {
        current = ensureState(boss);

        current.attack1Ticks++;
        current.attack2Ticks++;

        root.execute(boss);
    }
}
