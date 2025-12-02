package AI;

import java.util.HashMap;
import java.util.Map;

import Objects.Character;
import Objects.Necromancer;
import Objects.Player;
import Utilitary.V2D;

public class NecromancerBossAI extends AI {

    private final Node root;

    // === CONSTANTES ===
    private static final int CD_ATTACK1 = 400;   
    private static final int CD_ATTACK2 = 280;   
    private static final double SAFE_DISTANCE = 200;
    private static final double FLEE_SPEED = 2.3;

    // === ETAT ===
    private static class BossState {
        int attack1Ticks = 0;
        int attack2Ticks = 0;

        boolean bigSummonDone = false;
        boolean berzerk = false;

        boolean freezeAfterSummon = false;
        int freezeTicks = 0;

        boolean repositioning = false;
        int repositionTicks = 0;

        // --- NEW : first summon au début du combat ---
        boolean firstSummonDone = false;
        int firstSummonDelay = 120; // 2 sec à 60 FPS
    }

    private final Map<Character, BossState> states = new HashMap<>();
    private BossState current;

    private BossState ensureState(Character c) {
        return states.computeIfAbsent(c, k -> new BossState());
    }

    // ======================
    // === CONSTRUCTEUR ====
    // ======================
    public NecromancerBossAI() {

        this.root = new Selector()

            // ======================================================
            // 0) FREEZE après invocation (déjà existant)
            // ======================================================
            .add(new Action("FreezeAfterSummon", (ch) -> {
                if (!(ch instanceof Necromancer boss)) return Node.Status.FAIL;

                if (current.freezeAfterSummon) {
                    current.freezeTicks--;

                    if (current.freezeTicks <= 0) {
                        current.freezeAfterSummon = false;
                        return Node.Status.FAIL;
                    }

                    return Node.Status.RUNNING; 
                }

                return Node.Status.FAIL;
            }))

            // ======================================================
            // 0b) FIRST SUMMON AU DEBUT DU COMBAT — AJOUT NOUVEAU
            // ======================================================
            .add(new Action("First Summon at start", (ch) -> {
                if (!(ch instanceof Necromancer boss)) return Node.Status.FAIL;

                if (current.firstSummonDone) return Node.Status.FAIL; 

                current.firstSummonDelay--;
                if (current.firstSummonDelay > 0)
                    return Node.Status.RUNNING; // attendre

                // Lancer l'invocation initiale
                boss.setNbrSummoned(3);
                boss.attack1();
                current.attack1Ticks = 0;

                current.freezeAfterSummon = true;
                current.freezeTicks = 40;

                current.firstSummonDone = true;

                
                return Node.Status.SUCCESS;
            }))

            // ======================================================
            // 1) Repositionnement
            // ======================================================
            .add(new Sequence()

                .add(new Action("Repositioning", (ch) -> {
                    if (!(ch instanceof Necromancer boss)) return Node.Status.FAIL;

                    if (!current.repositioning) return Node.Status.FAIL;

                    Player p = Player.getPlayer1();
                    if (p == null) return Node.Status.FAIL;

                    current.repositionTicks++;

                    if (current.repositionTicks >= 35) {
                        current.repositioning = false;
                        return Node.Status.SUCCESS;
                    }

                    double dx = boss.getPosition().x - p.getPosition().x;
                    double dy = boss.getPosition().y - p.getPosition().y;
                    double d = Math.sqrt(dx*dx + dy*dy);

                    double dirX = dx / Math.max(1, d);
                    double dirY = dy / Math.max(1, d);

                    if (!boss.getCollision().forwardBlockCheck(dirX * 3, 0))
                        boss.addX(dirX * FLEE_SPEED);

                    if (!boss.getCollision().forwardBlockCheck(0, dirY * 3))
                        boss.addY(dirY * FLEE_SPEED);

                    return Node.Status.RUNNING;
                }))
            )

            .add(new Action("Check distance for repositioning", (ch) -> {
                if (!(ch instanceof Necromancer boss)) return Node.Status.FAIL;

                Player p = Player.getPlayer1();
                if (p == null) return Node.Status.FAIL;

                double dist = boss.getPosition().distance(p.getPosition());

                if (dist < SAFE_DISTANCE && !current.repositioning && !current.berzerk) {
                    current.repositioning = true;
                    current.repositionTicks = 0;
                    return Node.Status.SUCCESS;
                }

                return Node.Status.FAIL;
            }))

            // ======================================================
            // 2) Mode Berserk
            // ======================================================
            .add(
                new Sequence()

                    .add(new Action("GoBerzerk?", (ch) -> {
                        if (!(ch instanceof Necromancer boss)) return Node.Status.FAIL;

                        if (!current.berzerk && boss.getPercentHP() <= 0.5f)
                            return Node.Status.SUCCESS;

                        return Node.Status.FAIL;
                    }))

                    .add(new Action("Big Summon", (ch) -> {
                        if (!(ch instanceof Necromancer boss)) return Node.Status.FAIL;

                        if (!current.bigSummonDone) {
                            boss.setNbrSummoned(8);
                            boss.attack1(); 
                            current.bigSummonDone = true;

                            current.freezeAfterSummon = true;
                            current.freezeTicks = 40;
                        }

                        return Node.Status.SUCCESS;
                    }))

                    .add(new Action("Activate Berzerk", (ch) -> {
                        current.berzerk = true;
                        return Node.Status.SUCCESS;
                    }))
            )

            // ======================================================
            // 3) Attack 1 — petites invocations uniquement hors berserk
            // ======================================================
            .add(new Sequence()

                .add(new Action("CheckSummonAllowed", (ch) -> {
                    return current.berzerk ? Node.Status.FAIL : Node.Status.SUCCESS;
                }))

                .add(new Action("CD Attack1", (ch) -> {
                    return current.attack1Ticks >= CD_ATTACK1
                            ? Node.Status.SUCCESS
                            : Node.Status.FAIL;
                }))

                .add(new Action("Range Summon", (ch) -> {
                    if (!(ch instanceof Necromancer boss)) return Node.Status.FAIL;
                    Player p = Player.getPlayer1();

                    double dist = boss.getPosition().distance(p.getPosition());
                    return (dist <= 450) ? Node.Status.SUCCESS : Node.Status.FAIL;
                }))

                .add(new Action("Summon Squelettes", (ch) -> {
                    if (!(ch instanceof Necromancer boss)) return Node.Status.FAIL;

                    boss.setNbrSummoned(3);
                    boss.attack1();
                    current.attack1Ticks = 0;

                    current.freezeAfterSummon = true;
                    current.freezeTicks = 40;

                    return Node.Status.SUCCESS;
                }))
            )

            // ======================================================
            // 4) Attack 2 — AoE (spam en berserk)
            // ======================================================
            .add(new Sequence()

                .add(new Action("CanCastAoE?", (ch) -> {
                    if (current.berzerk) return Node.Status.SUCCESS;

                    return current.attack2Ticks >= CD_ATTACK2
                            ? Node.Status.SUCCESS
                            : Node.Status.FAIL;
                }))

                .add(new Action("Cast AoE", (ch) -> {
                    if (!(ch instanceof Necromancer boss)) return Node.Status.FAIL;

                    boss.attack2();

                    if (!current.berzerk)
                        current.attack2Ticks = 0;

                    return Node.Status.SUCCESS;
                }))
            )

            // ======================================================
            // 5) Follow player (déplacement normal)
            // ======================================================
            .add(new Action("Follow Player", (ch) -> {
                if (!(ch instanceof Necromancer boss)) return Node.Status.FAIL;

                Player p = Player.getPlayer1();
                if (p == null) return Node.Status.FAIL;

                V2D from = boss.getPosition();
                V2D to = p.getPosition();

                if (AI.rayCast(from, to))
                    moveToward(boss, to, 1.3);
                else
                    navigateAround(boss, to, 1.3);

                return Node.Status.RUNNING;
            }));
    }

    // === TICK PRINCIPAL ===
    @Override
    public void tick(Character boss) {
        current = ensureState(boss);
        current.attack1Ticks++;
        current.attack2Ticks++;
        root.execute(boss);
    }
}
